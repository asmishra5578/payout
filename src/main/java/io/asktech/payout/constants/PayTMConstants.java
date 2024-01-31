package io.asktech.payout.constants;

public interface PayTMConstants {
	
	//UAT DETAILS
	String PREFIXVAL = "89999";
	String VANSOLUTION = "VAN";
	String STRATEGY = "PREFIX_CUSTOM";
	String PARTNERID = "000243";
	String VANbaseURL="https://pass-api-staging.paytmbank.com/api/pms/admin/v1/product/mapping";
	String CONTENTTYPE = "Content-Type";
	String APPTYPE = "application/json";
	String AUTHORIZATION = "Authorization";
	String merchantKEYPROD="tMrLdfhOq5vkkIKZ";
	//String merchantKEYUAT="xN4#G3@goxsX7DGl";
	//String x_midPROD = "Analyt67347605126132";
	//String x_midUAT = "Analyt01170777231026";
	String ACCEPT = "Accept";
	String CACHE_CONTROL = "Cache-Control";
	String NO_CACHE = "no-cache";
	//String GUUID="fc1674ba-ba23-11eb-994b-fa163e429e83";
	//String WalletBaseURL="https://staging-dashboard.paytm.com/bpay/api/v1/disburse/order/wallet/gratification";
	//String BankBaseUrl = "https://staging-dashboard.paytm.com/bpay/api/v1/disburse/order/bank";
	//PayTM 
	String secretKey = "4zTmmKYw6a2jxKLbrNcQBqpNkiDWTv-RxuI5FUVs6vQ=";
	
	
	/*
	//PROD DETAILS
	String PREFIXVAL = "89999";
	String VANSOLUTION = "VAN";
	String STRATEGY = "PREFIX_CUSTOM";
	String PARTNERID = "000268";
	//Prod URL of VAN Creation
	String VANbaseURL="https://pass-api.paytmbank.com /api/pms/admin/v1/product/mapping";
	String CONTENTTYPE = "Content-Type";
	String APPTYPE = "application/json";
	//Wallet Details
	String merchantKEYPROD="tMrLdfhOq5vkkIKZ";
	String x_midPROD = "Analyt67347605126132";
	//Wallet Tranfer API URL
	String WalletBaseURL="https://dashboard.paytm.com/bpay/api/v1/disburse/order/wallet/gratification";
	//Wallet Bank Trasfer URL
	String BankBaseUrl = "https://dashboard.paytm.com/bpay/api/v1/disburse/order/bank";
	//VAN SECRET KEY
	String secretKey = "GIj7GvEySFRhSfWkNvwY6abWkqRXz5GICBHkJenVSGI=";
	*/
}
