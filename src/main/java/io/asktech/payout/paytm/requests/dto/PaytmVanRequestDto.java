package io.asktech.payout.paytm.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaytmVanRequestDto {
	private String mobile;
	private String name;
	private String partnerId;
	private String partnerRefId;
	private String prefix;
	private String solution;
	private String strategy;
}
