package zoombus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoombus.customObj.PassengerResponse;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerDTO implements SuperDTO, PassengerResponse {
    private String Id;
    private String firstName;
    private String  lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String password;
    private String profilePic;
}
