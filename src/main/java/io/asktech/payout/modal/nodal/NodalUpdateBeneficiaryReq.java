package io.asktech.payout.modal.nodal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.asktech.payout.modal.van.AbstractTimeStampAndId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodalUpdateBeneficiaryReq extends AbstractTimeStampAndId{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	private String orderId;
	private String updateBy;
	private String beneficiaryRefId;
	private String beneficiaryNickName;
	private String beneficiaryUserType;
	private String email;
	private String ifsc;
	private String mobile;
	private String name;
	
}
