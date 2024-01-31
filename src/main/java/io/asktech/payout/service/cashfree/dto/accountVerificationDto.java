package io.asktech.payout.service.cashfree.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class accountVerificationDto {
    
   private String accountStatus;
   private Data data;
   private Integer subCode;
   private String accountStatusCode;
   private String message;
   private String status;
}
