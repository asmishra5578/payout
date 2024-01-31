package io.asktech.payout.service.safex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResHeader {
    @JsonProperty("date")
    private String date;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("ipAddress")
    private String ipAddress;
    @JsonProperty("userAgent")
    private String userAgent;
    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("operatingSystem")
    private String operatingSystem;
    @JsonProperty("version")
    private String version;
    @JsonProperty("key")
    private String key;
}
