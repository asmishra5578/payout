package io.asktech.payout.dto.reqres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusRes {

	private String status;
	private String statusCode;
	private String statusMessage;
	private TransferStatusResponce result;
	
}
