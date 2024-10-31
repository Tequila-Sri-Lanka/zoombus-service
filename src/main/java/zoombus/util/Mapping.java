package zoombus.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zoombus.dto.PartnerGetDTO;
import zoombus.dto.PartnerSaveDTO;
import zoombus.dto.PassengerDTO;
import zoombus.entity.PartnerEntity;
import zoombus.entity.PassengerEntity;

import java.util.List;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    //    passenger mapping
    public PassengerEntity convertToPassengerEntity(PassengerDTO passenger) {
        return modelMapper.map(passenger, PassengerEntity.class);
    }

    public PassengerDTO convertToPassengerDTO(PassengerEntity passenger) {
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    public List<PassengerDTO> convertPassengerToDTOList(List<PassengerEntity> passengerEntities) {
        return modelMapper.map(passengerEntities, new TypeToken<List<PassengerDTO>>() {
        }.getType());
    }

    //partner mapping
    public PartnerEntity convertToPartnerEntity(PartnerGetDTO partner) {
        return modelMapper.map(partner, PartnerEntity.class);
    }

    public PartnerEntity convertToPartnerEntity(PartnerSaveDTO partnerSaveDTO) {
        return new PartnerEntity(
                partnerSaveDTO.getId(),
                partnerSaveDTO.getNId(),
                partnerSaveDTO.getName(),
                partnerSaveDTO.getAddress(),
                partnerSaveDTO.getPhoneNumber(),
                null,// Placeholder for password
                null// Placeholder for profilePic
        );
    }
    public PartnerGetDTO convertToPartnerDTO(PartnerEntity partner) {
        return modelMapper.map(partner, PartnerGetDTO.class);
    }

    public List<PartnerGetDTO> convertPartnerToDTOList(List<PartnerEntity> partnerEntities) {
        return modelMapper.map(partnerEntities, new TypeToken<List<PartnerGetDTO>>() {
        }.getType());
    }

}
