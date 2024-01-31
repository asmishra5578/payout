package io.asktech.payout.modal.nodal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.asktech.payout.modal.van.AbstractTimeStampAndId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NodalBenefManagementReq extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	@NotNull(message = "MerchantId Should not be null")
	private String merchantId;
	@Column(unique = true)
	private String orderId;
	@NotNull(message = "Account Number Should not be null")
	private String beneficiaryAccountNumber;
	@NotNull(message = "IFSC Code Should not be null")
	private String beneficiaryIfsc;
	private String beneficiaryAccountType;
	private String beneficiaryUserType;
	@NotNull(message = "Beneficiary Name Should not be null")
	private String beneficiaryName;
	@NotNull(message = "Beneficiary EmailID Should not be null")
	@Email(message = "Email should be valid")
	private String beneficiaryEmail;
	@NotNull(message = "Beneficiary MobileNo Should not be null")
	private String beneficiaryMobile;
	@NotBlank(message = "Beneficiary NickName Should not be null")
	private String beneficiaryNickName;
	

}
