package io.asktech.payout.dto.nodal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTmNodalFetchAccBalRes {
	private String accountNumber;
	private String effectiveBalance;
	private String slfdBalance;
	private String status;
	private String txn_id;
	private String message;
	@JsonProperty("response_code")
	private String responseCode;
}
