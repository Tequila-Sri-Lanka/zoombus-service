package zoombus.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zoombus.customObj.PassengerErrorResponse;
import zoombus.customObj.PassengerResponse;
import zoombus.dao.PassengerDao;
import zoombus.dto.LoginDTO;
import zoombus.dto.PassengerDTO;
import zoombus.entity.PassengerEntity;
import zoombus.exception.DataPersistFailedException;
import zoombus.exception.PassengerNotFoundException;
import zoombus.service.PassengerService;
import zoombus.service.S3Service;
import zoombus.util.AppUtil;
import zoombus.util.Mapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    private final PassengerDao passengerDao;

    @Autowired
    private final Mapping mapping;

    @Autowired
    private S3Service s3Service;
    private final String folderName="passenger";

    @Override
    public void savePassenger(PassengerDTO passenger) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passenger.setId(AppUtil.createPassengerId());
        passenger.setPassword(passwordEncoder.encode(passenger.getPassword()));


        //passenger.setProfilePic();
        PassengerEntity savedPassenger =
                passengerDao.save(mapping.convertToPassengerEntity(passenger));
        if (savedPassenger == null) {
            throw new DataPersistFailedException("Cannot data saved");
        }

    }

    @Override
    public void updatePassenger(PassengerDTO passenger) {
        Optional<PassengerEntity> tmpPassenger = passengerDao.findById(passenger.getId());
        if (!tmpPassenger.isPresent()) {
            throw new PassengerNotFoundException("Passenger not found");
        } else {
            tmpPassenger.get().setFirstName(passenger.getFirstName());
            tmpPassenger.get().setLastName(passenger.getLastName());
            tmpPassenger.get().setGender(passenger.getGender());
            tmpPassenger.get().setEmail(passenger.getEmail());
            tmpPassenger.get().setPhoneNumber(passenger.getPhoneNumber());
            tmpPassenger.get().setPassword(passenger.getPassword());
            tmpPassenger.get().setProfilePic(passenger.getProfilePic());
        }

    }

    @Override
    public void deletePassenger(String Id) {
        Optional<PassengerEntity> selectedPasengerId = passengerDao.findById(Id);
        if (!selectedPasengerId.isPresent()) {
            throw new PassengerNotFoundException("Passenger not found");
        } else {
            //delete image from s3 bucket
            s3Service.deleteFile(passengerDao.getPassengerEntityById(Id).getProfilePic(),folderName);
            passengerDao.deleteById(Id);
        }
    }

    @Override
    public PassengerResponse getSelectedPassenger(String Id) {
        if (passengerDao.existsById(Id)) {
            PassengerEntity passengerEntityByUserId = passengerDao.getPassengerEntityById(Id);
            //set url for send to frontend
            passengerEntityByUserId.setProfilePic(s3Service.getFileUrl(passengerEntityByUserId.getProfilePic()));
            return mapping.convertToPassengerDTO(passengerEntityByUserId);
        } else {
            return new PassengerErrorResponse(0, "Passenger not found");
        }
    }

    @Override
    public List<PassengerDTO> getAllPassengers() {
        List<PassengerEntity> getAllPassengers = passengerDao.findAll();

        // Update profilePic URL for each passenger for sent to frontend
        for (PassengerEntity passenger : getAllPassengers) {
            // Set the URL for the profile picture
            passenger.setProfilePic(s3Service.getFileUrl(passenger.getProfilePic()));
        }

        // Convert to DTOs
        return mapping.convertPassengerToDTOList(getAllPassengers);
    }
    @Override
    public String getOldProfilePicById( String id){
       return passengerDao.getPassengerEntityById(id).getProfilePic();
    }

    @Override
    public PassengerDTO passengerLogin(LoginDTO login) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PassengerEntity passengerEntity = passengerDao.findByEmailOrPhoneNumber(login.getEmail(),login.getPhoneNumber())
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found"));

        boolean isPasswordMatches = passwordEncoder.matches(login.getPassword(), passengerEntity.getPassword());
        return isPasswordMatches ? mapping.convertToPassengerDTO(passengerEntity) : null;
    }
}
