package zoombus.customObj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerErrorResponse implements Serializable,PassengerResponse {
    private int errorCode;
    private String errorMessage;
}
