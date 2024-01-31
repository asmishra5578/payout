package io.asktech.payout.dto.van;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VANResponseDto {

	private responseVANDto response;
	private String success;
	private String code;
	private String status;
	private String message;	
	private String requestId;	
	private String errorCode;
	private String errorMessage;

}
