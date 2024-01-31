package io.asktech.payout.service.axis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenfitDetails {

    @JsonProperty("beneBankName")
    private String beneMobileNo;
    @JsonProperty("beneMobileNo")
    private String beneBankName;
    @JsonProperty("beneName")
    private String beneName;
    @JsonProperty("beneIfscCode")
    private String beneIfscCode;
    @JsonProperty("beneCode")
    private String beneCode;
    @JsonProperty("beneEmailAddr1")
    private String beneEmailAddr1;
    @JsonProperty("corpCode")
    private String corpCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("beneAccNum")
    private String beneAccNum;

}
