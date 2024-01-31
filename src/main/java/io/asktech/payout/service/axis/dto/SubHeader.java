package io.asktech.payout.service.axis.dto;
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import java.io.Serializable;

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
public class SubHeader implements Serializable{
    public String requestUUID;
    public String serviceRequestId;
    public String serviceRequestVersion;
    public String channelId;
}
