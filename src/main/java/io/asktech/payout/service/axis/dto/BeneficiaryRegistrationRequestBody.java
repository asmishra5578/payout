package io.asktech.payout.service.axis.dto;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRegistrationRequestBody {
    public String channelId;
    public String corpCode;
    public String userId;
    public List<Beneinsert> beneinsert;
    public String checksum;




    // public String createChecsum(){

    //     String checkSumString = channelId + corpCode+userId + beneinsert.get(0).getApiVersion()+ beneinsert.get(0).getBeneCode()+ beneinsert.get(0).getBeneName()+ beneinsert.get(0).getBeneAccNum()+ beneinsert.get(0).getBeneIfscCode();

    //     return checkSumString;
        
    // }
}
