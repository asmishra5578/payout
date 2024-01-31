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
public class WalletTransactions extends AbstractTimeStampAndId {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String transactionType;
	private String amount;
	private String purpose;
	private String credit_debit;
	@Column(unique=true)
	private String transactionId;
	private String walletid;
	private String merchantId;
	@Column(columnDefinition = "text")
	private String remarks;
	private String closingBalance;
	private String openingBalance;
	private String transactionStatus;
	@Column(columnDefinition = "text")
	private String statusRemarks;
	private String mainWalletId;
	private String referenceId;
}
