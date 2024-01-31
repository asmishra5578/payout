package io.asktech.payout.service.payg.dto;

import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TransactionStatusResponseDto { 
   @JsonIgnore
   private String TotalFeeAmount;
   @JsonIgnore
   private String TransactionType;
   @JsonIgnore
   private String UniqueRequestId;
   @JsonIgnore
   private String PayoutCustomerkeyId;
   @JsonIgnore
   private String GSTAmount;
   @JsonIgnore
   private String PayOutKeyId;
   @JsonIgnore
   private String PayOutType;
   @JsonIgnore
   private String FeeCode;
   @JsonIgnore
   private String BuyGSTAmount;
   @JsonIgnore
   private String PaymentType;
   @JsonIgnore
   private String MerchantKeyId;
   @JsonIgnore
   private String FeeAmount;
   @JsonIgnore
   private String Status;
   @JsonIgnore
   private String BuyAmount;
   @JsonIgnore
   private String UTRNo;
   @JsonIgnore
   private String ResponseCode;
   @JsonIgnore
   private String Amount;
   @JsonIgnore
   private String BatchId;
   @JsonIgnore
   private String PayOutBeneficiaryKeyId;
   @JsonIgnore
   private String TransactionId;
   @JsonIgnore
   private String PayOutDate;
   @JsonIgnore
   private String AdditionalFee;
   @JsonIgnore
   private String ResponseText;
   @JsonIgnore
   private String UpdatedDateTime;
   @JsonIgnore
   private String GSTPercentage;
}
