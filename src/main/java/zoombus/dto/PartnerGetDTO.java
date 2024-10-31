package zoombus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoombus.customObj.PartnerResponse;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerGetDTO implements SuperDTO, PartnerResponse {

    private String Id;
    private String nId;
    private String name;
    private String address;
    private String phoneNumber;
    private String password;
    private String profilePic;
}
