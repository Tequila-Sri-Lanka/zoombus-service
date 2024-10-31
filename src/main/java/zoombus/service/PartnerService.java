package zoombus.service;
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
  
 @created 10/31/2024 - 3:49 PM 
*/

import zoombus.customObj.PartnerResponse;
import zoombus.dto.LoginDTO;
import zoombus.dto.PartnerGetDTO;
import zoombus.dto.PartnerSaveDTO;

import java.util.List;

public interface PartnerService {
    void savePartner(PartnerSaveDTO partner);
    void updatePartner(PartnerSaveDTO partner);
    void deletePartner(String Id);
    PartnerResponse getSelectedPartner(String Id);
    List<PartnerGetDTO> getAllPartners();
    String getOldProfilePicById( String id);
    PartnerGetDTO partnerLogin(LoginDTO login);
}
