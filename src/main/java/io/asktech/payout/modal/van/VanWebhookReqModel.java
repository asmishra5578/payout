package io.asktech.payout.modal.van;

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
public class VanWebhookReqModel extends AbstractTimeStampAndId{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String event_tracking_id;
	private String ca_id;
	private String status;
	private String amount;
	private String vanNumber;
	private String beneficiaryAccountNumber;
	private String beneficiaryIfsc;
	private String remitterAccountNumber;
	private String remitterIfsc;
	private String remitterName;
	private String bankTxnIdentifier;
	private String transactionRequestId;
	private String transferMode;
	private String responseCode;
	private String transactionDate;
	private String transactionType;
	private String parentUtr;
	private String branch;
	private String section;
	
	
}
