package io.asktech.payout.service.safex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOutBean {
   @JsonProperty("accountNo")
   private String accountNo;
   @JsonProperty("accountType")
   private String accountType;
   @JsonProperty("bankName")
   private String bankName;
   @JsonProperty("txnType")
   private String txnType;
   @JsonProperty("mobileNo")
   private String mobileNo;
   @JsonProperty("ifscCode")
   private String ifscCode;
   @JsonProperty("accountHolderName")
   private String accountHolderName;
   @JsonProperty("txnAmount")
   private String txnAmount;
   @JsonProperty("remark")
   private String remark;
   @JsonProperty("spkRefNo")
   private String spkRefNo;
   @JsonProperty("payoutDate")
   private String payoutDate;
   @JsonProperty("orderRefNo")
   private String orderRefNo;
   @JsonProperty("payoutTime")
   private String payoutTime;
   @JsonProperty("txnStatus")
   private String txnStatus;
   @JsonProperty("customerId")
   private String customerId;
   @JsonProperty("txn_InitiateDate")
   private String txn_InitiateDate;
   @JsonProperty("bankTransactionRefNo")
   private String bankTransactionRefNo;
   @JsonProperty("bankRefNo")
   private String bankRefNo;
   @JsonProperty("statusDesc")
   private String statusDesc;
   @JsonProperty("count")
   private String count;
   @JsonProperty("aggregatorId")
   private String aggregatorId;
   @JsonProperty("payoutId")
   private String payoutId;
   @JsonProperty("customerName")
   private String customerName;
   @JsonProperty("bankStatus")
   private String bankStatus;
   @JsonProperty("aggregtorName")
   private String aggregtorName;
   @JsonProperty("bankResponseCode")
   private String bankResponseCode;
   @JsonProperty("txnDate")
   private String txnDate;
   @JsonProperty("statusCode")
   private String statusCode;
   @JsonProperty("id")
   private String id;

}
