package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantUpdateAccountRequest {

	private String beneficiaryRefId;
    private NodalUpdateAccountRequestupdate update;
    private String updatedBy;
    private String orderId;
    private String merchantId;
}
