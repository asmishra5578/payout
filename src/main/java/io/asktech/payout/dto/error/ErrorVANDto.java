package io.asktech.payout.dto.error;

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
public class ErrorVANDto {
	
	private String success;
	private String requestId;
	@JsonProperty("errorCode")	
	private String errorCode;
	@JsonProperty("errorMessage")	
	private String errorMessage;
	private String responseCode;
}
