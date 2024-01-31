package io.asktech.payout.modal.merchant;

import javax.persistence.Column;
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
public class AccountTransactionUPIReq extends AbstractTimeStampAndId {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	@Column(unique = true)
	private String orderId;	
	private String internalOrderId;
	private String phonenumber;
	private String amount;
	private String purpose;
	private String beneficiaryVPA;
	private String beneficiaryName;
	private String requestType;
	private String requestStatus;
	private String pgName;
	private String trRemarks;
	private int threadFlag;
	private int merchantThreadFlag;
}
