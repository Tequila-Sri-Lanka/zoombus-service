package zoombus.util;

import java.util.Base64;
import java.util.UUID;

public class AppUtil {
    public static String createPassengerId(){
        return "PASSENGER-"+ UUID.randomUUID();
    }
    public static String toBase64ProfilePic(byte [] profilePic){
        return Base64.getEncoder().encodeToString(profilePic);
    }
}
