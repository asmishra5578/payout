package io.asktech.payout.service.payg.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundTransferResponseDto { 

   private String Status;
   private String TotalFeeAmount;
   private String BuyAmount;
   private String TransactionType;
   private String ResponseCode;
   private String UniqueRequestId;
   private String Amount;
   private String PayoutCustomerkeyId;
   private String GSTAmount;
   private String BatchId;
   private String PayOutKeyId;
   private String PayOutBeneficiaryKeyId;
   private String PayOutType;
   private String FeeCode;
   private String TransactionId;
   private String PayOutDate;
   private String AdditionalFee;
   private String BuyGSTAmount;
   private String PaymentType;
   private String ResponseText;
   private String UpdatedDateTime;
   private String GSTPercentage;
   private String MerchantKeyId;
   private String FeeAmount;

}
