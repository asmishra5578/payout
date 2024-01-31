package io.asktech.payout.service.neocred.dto;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDto {
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("data")
    private String data;
    public String convertDTO2JsonString() {
        return null;
    }
}
