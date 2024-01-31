package io.asktech.payout.wallet.modal;

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
public class WalletDetails extends AbstractTimeStampAndId{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String walletid;
	private String merchantid;
	private String mainWalletid;
	private String status;
	private String amount;
	private String name;
	private String walletCallBackAPI;
	private String walletHoldAmount;
	private String instantReversal;
	private int merchantThreadFlag;
}