package io.asktech.payout.dto.van;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VANRequestDto {

	private String mobile;
	private String name;
	private String partnerId;
	private String partnerRefId;
	private String prefix;	
	private String solution;	
	private String strategy;	
}
