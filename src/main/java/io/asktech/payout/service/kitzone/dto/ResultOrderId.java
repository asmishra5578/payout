package io.asktech.payout.service.kitzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultOrderId {
    @JsonProperty("Wallet") 
    public Object wallet;
    @JsonProperty("TransactionID") 
    public String transactionID;
    @JsonProperty("UserID") 
    public String userID;
    @JsonProperty("ReferenceUserID") 
    public String referenceUserID;
    @JsonProperty("ServiceReference") 
    public String serviceReference;
    @JsonProperty("UserRole") 
    public String userRole;
    @JsonProperty("Type") 
    public int type;
    @JsonProperty("Time") 
    public String time;
    @JsonProperty("Status") 
    public int status;
    @JsonProperty("Balance") 
    public double balance;
    @JsonProperty("LedgerBalance") 
    public double ledgerBalance;
    @JsonProperty("Amount") 
    public double amount;
    @JsonProperty("Commission") 
    public double commission;
    @JsonProperty("ServiceCharge") 
    public double serviceCharge;
    @JsonProperty("Tds") 
    public double tds;
    @JsonProperty("Gst") 
    public double gst;
    @JsonProperty("Discount") 
    public double discount;
    @JsonProperty("FinalAmount") 
    public double finalAmount;
    @JsonProperty("Method") 
    public String method;
    @JsonProperty("Detail") 
    public String detail;
    @JsonProperty("OrderID") 
    public String orderID;
    @JsonProperty("Active") 
    public boolean active;
    @JsonProperty("OperatorReferanceID") 
    public Object operatorReferanceID;
    @JsonProperty("ApiTransationID") 
    public Object apiTransationID;
    @JsonProperty("CouponCode") 
    public Object couponCode;
    @JsonProperty("ReferanceID") 
    public Object referanceID;
    @JsonProperty("InvoiceUrl") 
    public Object invoiceUrl;
    @JsonProperty("IP") 
    public String iP;
    @JsonProperty("Location") 
    public String location;
    @JsonProperty("ChildServiceName") 
    public String childServiceName;
    @JsonProperty("ParentServiceName") 
    public String parentServiceName;
    @JsonProperty("AdditionalDetails") 
    public String additionalDetails;
    @JsonProperty("TransactionNumber") 
    public int transactionNumber;
    @JsonProperty("OTP") 
    public Object oTP;
    @JsonProperty("ServiceAccessToken") 
    public String serviceAccessToken; 
}
