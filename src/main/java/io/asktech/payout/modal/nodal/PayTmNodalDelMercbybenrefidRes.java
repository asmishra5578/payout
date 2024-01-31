package io.asktech.payout.modal.nodal;

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
public class PayTmNodalDelMercbybenrefidRes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sno;
	private String orderId;
	private String success;
	private String code;
	private String errorCode;
	private String status;
	private String message;
}
