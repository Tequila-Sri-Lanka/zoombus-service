package zoombus.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zoombus.customObj.PartnerErrorResponse;
import zoombus.customObj.PartnerResponse;
import zoombus.dao.PartnerDao;
import zoombus.dto.LoginDTO;
import zoombus.dto.PartnerGetDTO;
import zoombus.dto.PartnerSaveDTO;
import zoombus.entity.PartnerEntity;
import zoombus.exception.PartnerNotFoundException;  // Consider changing to this exception
import zoombus.service.PartnerService;
import zoombus.service.S3Service;
import zoombus.util.Mapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerDao partnerDao;
    private final Mapping mapping;
    private final S3Service s3Service;
    private final BCryptPasswordEncoder passwordEncoder;

    private final String folderName="partner";

    @Override
    public void savePartner(PartnerSaveDTO partnerSaveDTO) {
        String encodedPwd = passwordEncoder.encode(partnerSaveDTO.getPassword());
        try {
            String profilePicUrl = s3Service.uploadFile(partnerSaveDTO.getProfilePic(),folderName);
            PartnerEntity partnerEntity = mapping.convertToPartnerEntity(partnerSaveDTO);
            partnerEntity.setPassword(encodedPwd);
            partnerEntity.setProfilePic(profilePicUrl);
            partnerDao.save(partnerEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public void updatePartner(PartnerSaveDTO partner) {
        String encodedPwd = passwordEncoder.encode(partner.getPassword());
        Optional<PartnerEntity> tmpPartner = partnerDao.findById(partner.getId());

        if (tmpPartner.isEmpty()) {
            throw new PartnerNotFoundException("Partner not found");
        } else {
            try {
                String oldProfilePic = getOldProfilePicById(partner.getId());
                String profilePicUrl = s3Service.updateFile(partner.getProfilePic(), oldProfilePic,folderName);
                PartnerEntity partnerEntity = mapping.convertToPartnerEntity(partner);
                partnerEntity.setPassword(encodedPwd);
                partnerEntity.setProfilePic(profilePicUrl);
                partnerDao.save(partnerEntity);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update profile picture", e);
            }
        }
    }

    @Override
    public void deletePartner(String id) {
        Optional<PartnerEntity> selectedPartnerId = partnerDao.findById(id);

        if (selectedPartnerId.isEmpty()) {
            throw new PartnerNotFoundException("Partner not found");
        } else {
            System.out.println(selectedPartnerId.get().getProfilePic());
            s3Service.deleteFile(selectedPartnerId.get().getProfilePic(),folderName);
            partnerDao.deleteById(id);
        }
    }

    @Override
    public PartnerResponse getSelectedPartner(String id) {
        if (partnerDao.existsById(id)) {
            PartnerEntity partnerEntity = partnerDao.getPartnerEntityById(id);
            partnerEntity.setProfilePic(s3Service.getFileUrl(partnerEntity.getProfilePic()));
            return mapping.convertToPartnerDTO(partnerEntity);
        } else {
            return new PartnerErrorResponse(0, "Partner not found");
        }
    }

    @Override
    public List<PartnerGetDTO> getAllPartners() {
        List<PartnerEntity> partners = partnerDao.findAll();

        for (PartnerEntity partner : partners) {
            partner.setProfilePic(s3Service.getFileUrl(partner.getProfilePic()));
        }

        return mapping.convertPartnerToDTOList(partners);
    }

    @Override
    public String getOldProfilePicById(String id) {
        return partnerDao.getPartnerEntityById(id).getProfilePic();
    }

    @Override
    public PartnerGetDTO partnerLogin(LoginDTO login) {
        PartnerEntity partnerEntity = partnerDao.findByPhoneNumber(login.getPhoneNumber())
                .orElseThrow(() -> new PartnerNotFoundException("Partner not found"));

        boolean isPasswordMatches = passwordEncoder.matches(login.getPassword(), partnerEntity.getPassword());
        return isPasswordMatches ? mapping.convertToPartnerDTO(partnerEntity) : null;
    }
}
