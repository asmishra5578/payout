package io.asktech.payout.service.axis.dto;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenedetailRes {
    public Object beneMobileNo;
    public Object statusDesc;
    public Object beneBankName;
    public String beneName;
    public String beneIfscCode;
    public String beneCode;
    public Object beneEmailAddr1;
    public String corpCode;
    public String status;
    public String beneAccNum;
}
