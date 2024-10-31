package zoombus.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zoombus.dto.LoginDTO;
import zoombus.dto.PassengerDTO;
import zoombus.exception.DataPersistFailedException;
import zoombus.service.PassengerService;
import zoombus.service.S3Service;
import zoombus.util.JWTTokenGenerator;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("api/v1/passengerAuth")
public class AuthController {

    private final JWTTokenGenerator jwtTokenGenerator;
    private final PassengerService passengerService;



    public AuthController(PassengerService passengerService,JWTTokenGenerator jwtTokenGenerator) {
        this.passengerService = passengerService;
        this.jwtTokenGenerator = jwtTokenGenerator;



    }
    @PostMapping("/login")
    public Map<String, String> postLogin(@RequestBody LoginDTO loginDTO) {
        PassengerDTO passengerDTO = passengerService.passengerLogin(loginDTO);
        Map<String, String> response = new HashMap<>();
        if (passengerDTO == null) {
            response.put("massage", "wrong details");
        } else {
            String token = this.jwtTokenGenerator.generateJwtToken(passengerDTO);
            response.put("token", token);
        }
        return response;
    }
}
