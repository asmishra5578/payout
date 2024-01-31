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
public class BeneficiaryResponseDto implements Serializable{
    @JsonProperty("BeneficiaryRegistrationResponse")
    public BeneficiaryRegistrationResponse beneficiaryRegistrationResponse;

}


// {"BeneficiaryRegistrationResponse":
// {"SubHeader":{"requestUUID":"ABC123","serviceRequestId":"OpenAPI","serviceRequestVersion":"1.0","channelId":"ANALYTIQ"},
// "BeneficiaryRegistrationResponseBodyEncrypted":"GRZiDbSQQXoy+PnePWQZnUTEkhQVBthGmIFRLptIdOENfUm7088Suinb7XkbtFm7nSvX47iRnLxOaUdR3XKkFPFz9Mho0FGU4dGFZkIG37Y7yzhPh+kti/9JTJCYJfbfQxRZFO8zrPd4Kwj20DCeVBUFav7TQ0oee3bHKbJa2HS6zItygvmylJpAU505Q7z8AfmWs+3TrNN1r4X9B80gD0wq4e2bFr08jzGLfnzDveD841blshQylPk3p/94DmcAdKnBWAADgh6W5LCo+GIE1GWIMrIQPgKb4STzaTfCEpNywXHtbTc9ETltWcqidJ8+c+AL5fa0tyckzPn+CiRwcLgUGelUOKgRXt6xjTfpyJS4BTKJNl4K99do3byCT6mf8HeAVu8/2zTmacVIw4FE7n7HbmGq+b0GyZPmzsD/vNnd7Vvq0XcYFJY9tfRek7VoTXnBPWjxlPUG3T0FA/IfDnqmEPaG3b0od+ERVBLaMpNFSOfkxPx6QRxAnxwTqPgx"}}
