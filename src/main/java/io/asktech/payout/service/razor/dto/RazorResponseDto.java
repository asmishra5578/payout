package io.asktech.payout.service.razor.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.asktech.payout.service.razor.dto.req.Notes;
import io.asktech.payout.service.razor.dto.response.Fund_accountRes;
import io.asktech.payout.service.razor.dto.response.RazError;
import io.asktech.payout.service.razor.dto.response.Status_details;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazorResponseDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("entity")
    private String entity;
    @JsonProperty("fund_account_id")
    private String fund_account_id;
    @JsonProperty("fund_account")
    private Fund_accountRes fund_account;    
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("notes")
    private Notes notes;
    @JsonProperty("fees")
    private String fees;
    @JsonProperty("tax")
    private String tax;
    @JsonProperty("status")
    private String status;
    @JsonProperty("purpose")
    private String purpose;
    @JsonProperty("utr")
    private String utr;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("reference_id")
    private String reference_id;
    @JsonProperty("narration")
    private String narration;    
    @JsonProperty("batch_id")
    private String batch_id;
    @JsonProperty("failure_reason")
    private String failure_reason;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("fee_type")
    private String fee_type;
    @JsonProperty("status_details")
    private Status_details status_details;
    @JsonProperty("merchant_id")
    private String merchant_id;
    @JsonProperty("status_details_id")
    private String status_details_id;
    @JsonProperty("error")
    private RazError error;
    
}
