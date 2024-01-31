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

public class NodalBenefManagementDetailModal extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	private String orderId;
	private String beneficiaryAccountNumber;
	private String beneficiaryIfsc;
	private String beneficiaryAccountType;
	private String beneficiaryName;
	private String beneficiaryEmail;
	private String beneficiaryMobile;
	private String beneficiaryNickName;
	private String response;
	private String errorCode;
	private String status;
	private String message;

}
