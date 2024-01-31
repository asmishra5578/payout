package io.asktech.payout.service.Pinwallet.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Paymentreq {
    public String benificiaryAccount;
    public String benificiaryIfsc;
    public String benificiaryName;
    public String latitude;
    public String longitude;
    public String transactionId; 
    public String amount;
}
