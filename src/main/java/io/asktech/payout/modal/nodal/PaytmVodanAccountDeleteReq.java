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
public class PaytmVodanAccountDeleteReq extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sno;
	private String orderId;
	private String merchantId;
	private String requestBy;
	private String beneficiaryRefId;
}
