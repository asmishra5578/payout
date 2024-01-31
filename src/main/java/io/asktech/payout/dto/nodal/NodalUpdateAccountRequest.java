package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodalUpdateAccountRequest {

	private String beneficiaryRefId;
    private NodalUpdateAccountRequestupdate update;
    private String updatedBy;
}
