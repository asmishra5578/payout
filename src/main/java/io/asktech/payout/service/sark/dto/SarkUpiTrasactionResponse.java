package io.asktech.payout.service.sark.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.asktech.payout.service.sark.dto.SarkTrasactionResponse.Transaction_details;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SarkUpiTrasactionResponse {
    private String message;
    private Transaction_details transaction_details;
    private String status;
}
