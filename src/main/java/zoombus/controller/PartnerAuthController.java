package zoombus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoombus.dto.LoginDTO;
import zoombus.dto.PartnerGetDTO;
import zoombus.service.PartnerService;
import zoombus.util.JWTTokenGenerator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/partnerAuth")
public class PartnerAuthController {
    private final JWTTokenGenerator jwtTokenGenerator;
    private final PartnerService partnerService;

    public PartnerAuthController(JWTTokenGenerator jwtTokenGenerator, PartnerService partnerService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.partnerService = partnerService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> postLogin(@RequestBody LoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();

        try {
            PartnerGetDTO partner = partnerService.partnerLogin(loginDTO);
            if (partner == null) {
                response.put("message", "Wrong details");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            } else {
                String token = this.jwtTokenGenerator.generateJwtToken(partner);
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("message", "An error occurred during login");
            // Consider logging the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
