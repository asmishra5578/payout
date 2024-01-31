package io.asktech.payout.service.axis.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenficiaryRegistrationRespBody implements Serializable {
    
@JsonProperty("data")
private Data data;
// @JsonProperty("benifitDetails")
// private BenfitDetails benifitDetails;

@JsonProperty("message")
private String message;
@JsonProperty("status")
private String status;


}
