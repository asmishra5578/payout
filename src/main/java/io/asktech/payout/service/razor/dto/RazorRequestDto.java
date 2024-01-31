package io.asktech.payout.service.razor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.asktech.payout.service.razor.dto.req.Fund_account;
import io.asktech.payout.service.razor.dto.req.Notes;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazorRequestDto {
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("account_number")
    private String account_number;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("notes")
    private Notes notes;
    @JsonProperty("reference_id")
    private String reference_id;
    @JsonProperty("purpose")
    private String purpose;
    @JsonProperty("queue_if_low_balance")
    private String queue_if_low_balance;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("fund_account")
    private Fund_account fund_account;
    @JsonProperty("currency")
    private String currency;
    
}
