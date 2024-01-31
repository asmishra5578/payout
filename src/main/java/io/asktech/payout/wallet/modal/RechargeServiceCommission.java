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
public class RechargeServiceCommission extends AbstractTimeStampAndId{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String merchantId;
	private String commissionType;
	private String commissionValue;
	
}
