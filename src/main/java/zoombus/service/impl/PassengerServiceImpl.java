package zoombus.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoombus.customObj.PassengerErrorResponse;
import zoombus.customObj.PassengerResponse;
import zoombus.dao.PassengerDao;
import zoombus.dto.PassengerDTO;
import zoombus.entity.PassengerEntity;
import zoombus.exception.DataPersistFailedException;
import zoombus.exception.PassengerNotFoundException;
import zoombus.service.PassengerService;
import zoombus.util.AppUtil;
import zoombus.util.Mapping;

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
    @Override
    public void savePassenger(PassengerDTO passenger) {
        passenger.setId(AppUtil.createPassengerId());
        //need impliment s3 bucket link
        //passenger.setProfilePic();
        PassengerEntity savedPassenger =
                passengerDao.save(mapping.convertToPassengerEntity(passenger));
        if(savedPassenger == null ) {
            throw new DataPersistFailedException("Cannot data saved");
        }

    }

    @Override
    public void updatePassenger(PassengerDTO passenger) {
        Optional<PassengerEntity> tmpPassenger = passengerDao.findById(passenger.getId());
        if(!tmpPassenger.isPresent()){
            throw new PassengerNotFoundException("Passenger not found");
        }else {
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
        if(!selectedPasengerId.isPresent()){
            throw new PassengerNotFoundException("Passenger not found");
        }else {
            passengerDao.deleteById(Id);
        }
    }

    @Override
    public PassengerResponse getSelectedPassenger(String Id) {
        if(passengerDao.existsById(Id)){
            PassengerEntity passengerEntityByUserId = passengerDao.getPassengerEntityById(Id);
            return mapping.convertToPassengerDTO(passengerEntityByUserId);
        }else {
            return new PassengerErrorResponse(0, "Passenger not found");
        }
    }

    @Override
    public List<PassengerDTO> getAllPassengers() {
        List<PassengerEntity> getAllPassengers = passengerDao.findAll();
        return mapping.convertPassengerToDTOList(getAllPassengers);

    }
}
