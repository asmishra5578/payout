package io.asktech.payout.paytm.requests.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PayTmAccountRequestsDto {
	private String orderId;
	private String subwalletGuid;
	private String amount;
	private String beneficiaryAccount;
	private String beneficiaryIFSC;
	private String purpose;
	private String date;
	private String beneficiaryName;
	private String transferMode;
	private String beneficiaryVPA;
}
