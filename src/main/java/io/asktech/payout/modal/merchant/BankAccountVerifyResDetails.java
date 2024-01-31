package io.asktech.payout.modal.merchant;

import javax.persistence.Column;
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
public class BankAccountVerifyResDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String merchantId;
	@Column(unique = true)
	private String orderId;	
	private String internalOrderId;
	private String status;
	private String statusCode;
	private String statusMessage;
}
