package io.asktech.payout.modal.van;

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
public class VanCreationDetails extends AbstractTimeStampAndId {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sno;
	private String merchantId;
	private String orderId;
	private String referenceId;
	private String active;
	private String beneficiaryName;
	private String ifscCode;
	private String bankName;
	private String requestId;
	private int	   sequenceNo;
	private String vanInput;
	@Column(unique = true)
	private String accoutNumber;
}
