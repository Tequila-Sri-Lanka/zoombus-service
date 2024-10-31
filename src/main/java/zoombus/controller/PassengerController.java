package zoombus.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import zoombus.service.S3Service;
import zoombus.util.JWTTokenGenerator;

import java.util.List;

@RestController
@RequestMapping("api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    @Autowired
    private final PassengerService passengerService;
    @Autowired
    private S3Service s3Service;

    @Autowired
    private JWTTokenGenerator jwtTokenGenerator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> savePassenger(
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("gender") String gender,
            @RequestPart("email") String email,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("password") String password,
            @RequestPart("profilePic") MultipartFile profilePic,
            HttpServletRequest request) {


        try {

            // build the passenger object
            PassengerDTO buildPassengerDto = new PassengerDTO();
            buildPassengerDto.setFirstName(firstName);
            buildPassengerDto.setLastName(lastName);
            buildPassengerDto.setGender(gender);
            buildPassengerDto.setEmail(email);
            buildPassengerDto.setPhoneNumber(phoneNumber);
            buildPassengerDto.setPassword(password);


            // Handle profile pic
            String profilePicUrl = s3Service.uploadFile(profilePic);
            buildPassengerDto.setProfilePic(profilePicUrl);

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
            @RequestPart("profilePic") MultipartFile updateProfilePic,
            @RequestHeader(name = "Authorization") String authorizationHeader

    ) {
        try {
            if (!jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            // build the passenger object
            PassengerDTO buildPassengerDto = new PassengerDTO();
            buildPassengerDto.setId(id);
            buildPassengerDto.setFirstName(firstName);
            buildPassengerDto.setLastName(lastName);
            buildPassengerDto.setGender(gender);
            buildPassengerDto.setEmail(email);
            buildPassengerDto.setPhoneNumber(phoneNumber);
            buildPassengerDto.setPassword(password);

            // Handle profile pic
            if (updateProfilePic != null && !updateProfilePic.isEmpty()) {
                String oldProfilePic = passengerService.getOldProfilePicById(id);
                String profilePicUrl = s3Service.updateFile(updateProfilePic, oldProfilePic);
                buildPassengerDto.setProfilePic(profilePicUrl);
            }


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
    public ResponseEntity<Void> deletePassenger(@PathVariable("id") String Id, @RequestHeader(name = "Authorization") String authorizationHeader) {
        // Extract and validate JWT token


        try {
            if (!jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            passengerService.deletePassenger(Id);


            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassengerResponse> getSelectedPassenger(@PathVariable("id") String Id, @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(passengerService.getSelectedPassenger(Id),HttpStatus.OK);
    }

    @GetMapping(value = "allPassengers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerDTO>>  getAllPassengers( @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!jwtTokenGenerator.validateJwtToken(authorizationHeader)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(passengerService.getAllPassengers(),HttpStatus.OK);
    }
}
