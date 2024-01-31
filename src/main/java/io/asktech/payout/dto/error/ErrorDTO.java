package io.asktech.payout.dto.error;

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
public class ErrorDTO {

	private String errorType;
	private String errorMessage;
	private String exceptionDetails;
	private String additionalDetails;
}
