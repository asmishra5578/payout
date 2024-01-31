package io.asktech.payout.wallet.modal;

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

public class BalanceProcessorQueue extends AbstractTimeStampAndId {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	@Column(unique = true)
	private String orderId;	
	@Column(unique = true)
	private String internalOrderId;
	private String phonenumber;
	private String amount;
	private String status;
	private String bankaccount;
	private String upiVpa;
	private String remarks;
	private String trType;
	private int merchantThreadFlag;
	private String pgName;
	
}
