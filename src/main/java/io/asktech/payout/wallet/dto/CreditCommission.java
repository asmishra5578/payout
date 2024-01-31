package io.asktech.payout.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCommission {
	private String merchantId;
	private String gateway;
	private String type;
	private String amount;
	private String commissionValue;
	private String unitType;
}
