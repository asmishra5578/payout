package io.asktech.payout.modal.nodal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaytmNodalUpdateBeneficiaryReq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sno;
	private String merchantId;
	private String orderId;
	private String beneficiaryRefId;
	private String beneficiaryNickName;
	private String beneficiaryUserType;
	private String email;
	private String ifsc;
	private String mobile;
	private String name;
	private String updatedBy;
}
