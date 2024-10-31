package zoombus.service;


import zoombus.customObj.PassengerResponse;
import zoombus.dto.PassengerDTO;

import java.util.List;

public interface PassengerService {
    void savePassenger(PassengerDTO passenger);
    void updatePassenger(PassengerDTO passenger);
    void deletePassenger(String Id);
    PassengerResponse getSelectedPassenger(String Id);
    List<PassengerDTO> getAllPassengers();
    public String getOldProfilePicById( String id);

}
