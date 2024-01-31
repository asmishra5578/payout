package io.asktech.payout.dto.nodal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTmNodalBalTransRes {

	private String status;
	private String amount;
	private String response_code;
	private String txn_id;
	private String target_account;
	private String message;
	private PayTmNodalBalTransResextra_info extra_info;
	
}
