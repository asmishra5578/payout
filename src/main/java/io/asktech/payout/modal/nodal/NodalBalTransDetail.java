package io.asktech.payout.modal.nodal;

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

public class NodalBalTransDetail extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sno;
	private String merchantId;
	@Column(unique = true)
	private String orderId;
	private String amount;
	private String benAccNo;
	private String benIfsc;
	private String benName;
	private String channel;
	private String remarks;
	private String beneficiaryIfsc;
	private String beneficiaryName;
	private String status;
	private String response_code;
	private String txn_id;
	private String target_account;

}
