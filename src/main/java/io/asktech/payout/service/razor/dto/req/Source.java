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
public class Source {
    @JsonProperty("id")
    private String id;
    @JsonProperty("entity")
    private String entity;
    @JsonProperty("fund_account_id")
    private String fund_account_id;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("notes")
    private Notes notes;
    @JsonProperty("fees")
    private String fees;
    @JsonProperty("tax")
    private String tax;
    @JsonProperty("status")
    private String status;
    @JsonProperty("utr")
    private String utr;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("fee_type")
    private String fee_type;
}
