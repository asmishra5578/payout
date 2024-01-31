package io.asktech.payout.dto.nodal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodalUpdateAccountRequestupdate {

	private String beneficiaryNickName;
	private String beneficiaryUserType;
	private String email;
	private String ifsc;
	private String mobile;
	private String name;
}
