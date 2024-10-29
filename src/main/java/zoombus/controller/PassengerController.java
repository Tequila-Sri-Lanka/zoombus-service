package zoombus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zoombus.dto.PassengerDTO;
import zoombus.exception.DataPersistFailedException;
import zoombus.service.PassengerService;
import zoombus.util.AppUtil;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    @Autowired
    private final PassengerService passengerService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveUser(
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("gender") String gender,
            @RequestPart("email") String email,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("password") String password,
            @RequestPart("profilePic") MultipartFile profilePic) {

        try {
            // Handle profile pic


            // build the passenger object
            PassengerDTO buildPassengerDto = new PassengerDTO();
            buildPassengerDto.setFirstName(firstName);
            buildPassengerDto.setLastName(lastName);
            buildPassengerDto.setGender(gender);
            buildPassengerDto.setEmail(email);
            buildPassengerDto.setPhoneNumber(phoneNumber);
            buildPassengerDto.setPassword(password);

            //need to set s3 bucket link
           // buildPassengerDto.setProfilePic();

            //send to the service layer
            passengerService.savePassenger(buildPassengerDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
