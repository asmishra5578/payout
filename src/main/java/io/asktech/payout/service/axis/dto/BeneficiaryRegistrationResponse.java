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
public class BeneficiaryRegistrationResponse implements Serializable{
    @JsonProperty("SubHeader")
    public SubHeader SubHeader;
    @JsonProperty("BeneficiaryRegistrationResponseBodyEncrypted")
    public String BeneficiaryRegistrationResponseBodyEncrypted;
}
