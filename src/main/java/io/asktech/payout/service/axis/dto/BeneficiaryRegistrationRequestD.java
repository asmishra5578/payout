package io.asktech.payout.service.axis.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRegistrationRequestD {
    private SubHeader SubHeader;
    private BeneficiaryRegistrationRequestBody beneficiaryRegistrationRequestBody;
}
