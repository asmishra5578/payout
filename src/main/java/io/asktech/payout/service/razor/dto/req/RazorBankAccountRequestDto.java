package io.asktech.payout.service.razor.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RazorBankAccountRequestDto {
    @JsonProperty("account_number")
    private String account_number;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("purpose")
    private String purpose;
    @JsonProperty("fund_account")
    private Fund_account Fund_account;
    @JsonProperty("queue_if_low_balance")
    private boolean queue_if_low_balance;
    @JsonProperty("reference_id")
    private String reference_id;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("notes")
    private Notes notes;

}
