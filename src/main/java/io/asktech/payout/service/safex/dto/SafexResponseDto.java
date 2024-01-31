package io.asktech.payout.service.safex.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SafexResponseDto {
    private PayOutBean payOutBean;
    private ResponseSafex response;
    private ResHeader header;
    private Transaction transaction;
}
