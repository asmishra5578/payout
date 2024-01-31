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

public class NodalBenefManagementRes extends AbstractTimeStampAndId{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sno;
	private String merchantId;
	@Column(unique = true)
	private String orderId;
	private String response;
	private String succes;
	private String code;
	private String errorCode;
	private String status;
	private String message;	

}
