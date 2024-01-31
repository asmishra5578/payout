package io.asktech.payout.service.payg.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundTransferDto { 

   private String BankName;
   private String ProductData;
   private String BeneficiaryVerification;
   private String UniqueRequestId;
   private String BankCountry;
   private String Amount;
   private String PayoutCustomerkeyId;
   private String BeneficiaryName;
   private String BankCode;
   private String PayOutType;
   private String AccountNumber;
   private String PayOutDate;
   private String PaymentType;
   private String BranchName;
   private String MerchantKeyId;
   private String KycData;

}
