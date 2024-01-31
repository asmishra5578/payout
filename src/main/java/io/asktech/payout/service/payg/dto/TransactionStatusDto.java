package io.asktech.payout.service.payg.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionStatusDto { 

   private String PayOutKeyId;
   private String MerchantKeyId;
   private String TransactionId;

}
