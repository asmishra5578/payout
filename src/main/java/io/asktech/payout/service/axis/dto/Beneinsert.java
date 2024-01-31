package io.asktech.payout.service.axis.dto;
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beneinsert {
    public String apiVersion;
    public String beneLEI;
    public String beneCode;
    public String beneName;
    public String beneAccNum;
    public String beneIfscCode;
    public String beneAcType;
    public String beneBankName;
    public String beneAddr1;
    public String beneAddr2;
    public String beneAddr3;
    public String beneCity;
    public String beneState;
    public String benePincode;
    public String beneEmailAddr1;
    public String beneMobileNo;
}
