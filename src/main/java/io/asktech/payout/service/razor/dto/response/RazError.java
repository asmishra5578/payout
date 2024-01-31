package io.asktech.payout.service.razor.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazError {
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("code")
    private String code;
    @JsonProperty("field")
    private String field;
    @JsonProperty("description")
    private String description;
    @JsonProperty("step")
    private String step;
    @JsonProperty("source")
    private String source;
     
}