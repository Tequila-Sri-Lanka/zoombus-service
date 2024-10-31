package zoombus.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zoombus.dto.PassengerDTO;
import zoombus.service.PassengerService;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenGenerator.class);

    @Value("${zoombus.app.jwtSecret}")
    private String jwtSecret;

    @Value("${zoombus.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final PassengerService passengerService;

    @Autowired
    public JWTTokenGenerator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // Generate a JWT token for a given PassengerDTO
    public String generateJwtToken(PassengerDTO passenger) {
        return Jwts.builder()
                .setId(String.valueOf(passenger.getId()))
                .setSubject(passenger.getFirstName())
                .claim("email", passenger.getEmail())
                .setIssuedAt(new Date()) // Token issued date
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Validate the JWT token
    public boolean validateJwtToken(String authToken) {
        String jwtToken = authToken.substring("Bearer ".length());
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(jwtToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }




}
