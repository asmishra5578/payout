package io.asktech.payout.modal.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

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

public class TransactionDetails extends AbstractTimeStampAndId {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	@Column(unique = true)
	private String orderId;
	private String internalOrderId;
	private String amount;
	private String phonenumber;
	private String ifsc;
	private String bankaccount;
	private String beneficiaryName;
	private String beneficiaryVPA;
	private String purpose;
	private String transactionType; //WALLET, IMPS, NEFT, RTGS
	private String paytmTransactionId;
	private String transactionStatus;
	@Column(columnDefinition = "text")
	private String transactionMessage;
	private String reconStatus;
	private String utrid;
	private String referenceId;
	private String pgname;
	private String pgId;
	private String callBackURL;
	private String callBackFlag;
	@Column(columnDefinition = "text")
	private String errorMessage;
}
