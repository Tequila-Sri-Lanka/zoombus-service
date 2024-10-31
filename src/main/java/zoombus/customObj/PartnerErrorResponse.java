package zoombus.customObj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    @author 
      
 (                           )   (        (                            )   (                 (               
 )\ )     (         (     ( /(   )\ )     )\ )       (      (       ( /(   )\ )      (       )\ )     (      
(()/(     )\        )\    )\()) (()/(    (()/(     ( )\     )\      )\()) (()/(      )\     (()/(     )\     
 /(_)) ((((_)(    (((_)  ((_)\   /(_))    /(_))    )((_) ((((_)(   ((_)\   /(_))  ((((_)(    /(_)) ((((_)(   
(_))    )\ _ )\   )\___   _((_) (_))     (_))     ((_)_   )\ _ )\   _((_) (_))_    )\ _ )\  (_))    )\ _ )\  
/ __|   (_)_\(_) ((/ __| | || | |_ _|    / __|     | _ )  (_)_\(_) | \| |  |   \   (_)_\(_) | _ \   (_)_\(_) 
\__ \    / _ \    | (__  | __ |  | |     \__ \     | _ \   / _ \   | .` |  | |) |   / _ \   |   /    / _ \   
|___/   /_/ \_\    \___| |_||_| |___|    |___/     |___/  /_/ \_\  |_|\_|  |___/   /_/ \_\  |_|_\   /_/ \_\  
  
 @created 10/31/2024 - 3:47 PM 
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerErrorResponse implements Serializable,PartnerResponse{
    private int errorCode;
    private String errorMessage;
}
