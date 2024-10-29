package zoombus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zoombus.customObj.PassengerResponse;
import zoombus.dto.PassengerDTO;
import zoombus.exception.DataPersistFailedException;
import zoombus.exception.PassengerNotFoundException;
import zoombus.service.PassengerService;

import java.util.List;

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

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePassenger(
            @PathVariable("id") String id,
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("gender") String gender,
            @RequestPart("email") String email,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("password") String password,
            @RequestPart("profilePic") MultipartFile profilePic

    ) {
        try {
            // Handle profile pic


            // build the passenger object
            PassengerDTO buildPassengerDto = new PassengerDTO();
            buildPassengerDto.setId(id);
            buildPassengerDto.setFirstName(firstName);
            buildPassengerDto.setLastName(lastName);
            buildPassengerDto.setGender(gender);
            buildPassengerDto.setEmail(email);
            buildPassengerDto.setPhoneNumber(phoneNumber);
            buildPassengerDto.setPassword(password);

            //need to set s3 bucket link
            // buildPassengerDto.setProfilePic();

            //send to the service layer
            passengerService.updatePassenger(buildPassengerDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable("id") String Id) {
        try {
            passengerService.deletePassenger(Id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PassengerResponse getSelectedPassenger(@PathVariable("id") String Id) {
        return passengerService.getSelectedPassenger(Id);
    }

    @GetMapping(value = "allPassengers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PassengerDTO> getAllPassengers() {
        return passengerService.getAllPassengers();
    }
}
