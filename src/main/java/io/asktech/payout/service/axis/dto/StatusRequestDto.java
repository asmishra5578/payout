package io.asktech.payout.service.axis.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequestDto {
    @JsonProperty("GetStatusRequest") 
    public GetStatusRequest getStatusRequest;

}
