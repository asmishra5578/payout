package io.asktech.payout.modal.merchant;

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
public class ApiRequestResponseLogger extends AbstractTimeStampAndId{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String merchantId;
	private String requestId;
	@Column(columnDefinition = "text")
	private String request;
	@Column(columnDefinition = "text")
	private String response;
	private String status;
	private String errorStatus;
	private String serviceProvider;
	private String apiType;
	
}
