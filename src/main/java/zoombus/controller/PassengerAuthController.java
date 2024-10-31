package zoombus.controller;


import org.springframework.web.bind.annotation.*;
import zoombus.dto.LoginDTO;
import zoombus.dto.PassengerDTO;
import zoombus.service.PassengerService;
import zoombus.util.JWTTokenGenerator;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("api/v1/passengerAuth")
public class PassengerAuthController {

    private final JWTTokenGenerator jwtTokenGenerator;
    private final PassengerService passengerService;



    public PassengerAuthController(PassengerService passengerService, JWTTokenGenerator jwtTokenGenerator) {
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
