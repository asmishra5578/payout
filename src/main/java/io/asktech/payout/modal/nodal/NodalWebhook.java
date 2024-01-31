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

public class NodalWebhook extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String event_tracking_id;
	private String ca_id;
	private String transactionType;
	private String amount;
	private String response_code;
	private String clientRequestId;
	private String transactionRequestId;
	private String transactionDate;
	private String status;
	private String transfer_mode;
	private String externalTransactionId;
	

}
