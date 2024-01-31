package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResDto {
    @JsonProperty("STATUS") 
    public String sTATUS;
    @JsonProperty("URN") 
    public String uRN;
    @JsonProperty("UNIQUEID") 
    public String uNIQUEID;
    @JsonProperty("UTRNUMBER") 
    public String uTRNUMBER;
    @JsonProperty("RESPONSE") 
    public String rESPONSE;
}
