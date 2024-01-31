package io.asktech.payout.service.axis.dto;
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
public class GetStatusResponse {
    @JsonProperty("SubHeader") 
    public SubHeader subHeader;
    @JsonProperty("GetStatusResponseBodyEncrypted") 
    public String getStatusResponseBodyEncrypted;
 
}
