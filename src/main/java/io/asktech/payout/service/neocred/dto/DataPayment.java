package io.asktech.payout.service.neocred.dto;
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
public class DataPayment {
    public String transferId;
    public String bankAccount;
    public String ifsc;
    public String amount;
    public String referenceId;
    public String utr;
    public String addedOn;
    public String processedOn;
    public String transferMode;
    public int acknowledged; 
    public String phone;
}
