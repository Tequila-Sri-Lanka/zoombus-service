package zoombus.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zoombus.customObj.PartnerResponse;
import zoombus.dto.PartnerGetDTO;
import zoombus.dto.PartnerSaveDTO;
import zoombus.exception.DataPersistFailedException;
import zoombus.exception.PartnerNotFoundException;  // Use the correct exception
import zoombus.service.PartnerService;
import zoombus.service.S3Service;
import zoombus.util.JWTTokenGenerator;

import java.util.List;

@RestController
@RequestMapping("api/v1/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final S3Service s3Service;
    private final JWTTokenGenerator jwtTokenGenerator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> savePartner(@ModelAttribute PartnerSaveDTO partnerSaveDTO
                                           ) {

        try {
            partnerService.savePartner(partnerSaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DataPersistFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePartner(@ModelAttribute PartnerSaveDTO partnerSaveDTO,
                                              @PathVariable("id") String id,
                                              @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!validateJwtToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        partnerSaveDTO.setId(id);
        try {
            partnerService.updatePartner(partnerSaveDTO);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PartnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable("id") String id,
                                              @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!validateJwtToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            partnerService.deletePartner(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PartnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerResponse> getSelectedPartner(@PathVariable("id") String id,
                                                              @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!validateJwtToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PartnerResponse partnerResponse = partnerService.getSelectedPartner(id);
        return ResponseEntity.ok(partnerResponse);
    }

    @GetMapping(value = "allPartners", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerGetDTO>> getAllPartners(@RequestHeader(name = "Authorization") String authorizationHeader) {
        if (!validateJwtToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<PartnerGetDTO> partnerList = partnerService.getAllPartners();
        return ResponseEntity.ok(partnerList);
    }

    private boolean validateJwtToken(String authorizationHeader) {
        return jwtTokenGenerator.validateJwtToken(authorizationHeader);
    }
}
