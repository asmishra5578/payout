package io.asktech.payout.constants;

public interface ErrorValues {
	String SQL_ERROR = "SQL Error:";
	String SQL_DUPLICATE_ID = "SQL Exception, Duplicate Order ID";
	String SAVE_SUCCESS = "Saved Success";
	String ALL_FIELDS_MANDATORY = "All Fields Mandatory";
	String FORM_VALIDATION_FAILED = "Form Validation Failed:";
	String SIGNATURE_MISMATCH = "Signature Mismatch";
	String JSON_PARSE_ISSUE_MERCHANT_REQUEST = "Merchant Request is not in Proper Format";
	String INPUT_BLANK_VALUE = "Input should not content blank value";
	String MERCHANT_EXITS = "Provided Merchant Name already Present in System";
	String MERCHANT_NOT_FOUND = "Merchant not found / Mapped with provided AppID - ";
	String MERCHNT_NOT_EXISTIS = "Merchant not found / Mapped with provided emailid";
	String MERCHANT_PG_SERVICE_NO_MAPPED = "Merchant not mapped with provided PG Service ";
	String MERCHANT_PG_CONFIG_NOT_FOUND = "Merchant mapped with PG Service but not mapped with PG Details like (Appid and Secret Keys).";
	String MERCHANT_PG_APP_ID_NOT_FOUND = "Merchant PG APP id not found in request .";
	String MERCHANT_PG_SECRET_NOT_FOUND = "Merchant PG Secret id not found in request .";
	String MERCHANT_PG_NAME_NOT_FOUND = "Merchant Name id not found in request .";
	String PG_NOT_PRESENT = "Provided PG name not found in system.";
	String BANK_DETAILS_NOT_FOUND = "Merchnat Bank Details not found in system.";
	
	String EMAIL_VALIDATION_FAILED = "Input EMAIL is not proper as per Email Validator. Proceed with proper Email id.";
	String NAME_VALIDATION_FAILED = "Input Customer Name / Name validation failed .";
	String PHONE_VAIDATION_FILED = "Input Mobile not format is not valid";
	String EMAIL_ID_NOT_FOUND = "Input Email Id not Exists in System , please login with proper EMAIL id / Register as a merchant.";
	String USER_STATUS_BLOCKED = "User Status is Blocked in system , Contact Admistrator.";
	String USER_STATUS_REMOVED = "User is Removed from system , Contact Admistrator.";
	String PASSWORD_CANT_BE_BLANK = "User Input password can't be NULL";
	String PASSWORD_MISMATCH = "Input password is not correct as per system Information.";
	String SESSION_NOT_FOUND = "User Session not active / found .";
	String USER_NOT_EXISTS = "User does not exist";
	String INITIAL_PASSWORD_CHANGE_REQUEST = "Initial password change required , please check the password . ";
	String PASSWORD_VALIDATION = "Password not validate as per Rules .\n a. start-of-string \n b.a digit must occur at least once \n c.a lower case letter must occur at least once \n d. an upper case letter must occur at least once \n e. a special character must occur at least once \n f. no whitespace allowed in the entire string \n g. anything, at least eight places though \n h. end-of-string";
	
	String MERCHANT_BANK_DETAIL_PRESENT = "Merchant Bank details already exists , client can be update the bank details from Update option.";
	String MERCHANT_SERVICE_TYPE = "Service Type blank will not acceptable in system.";
	String MERCHANT_COMMISSION_TYPE = "CommissionType blank will not acceptable in system.";
	String MERCHANT_COMM_AMOUNT = "Commission strucure Fixed / Floating amount can't be blank .";
	String MERCHANT_COMMISSION_EXISTS = "Comission structure is avalable for the merchant with service ID and PG Details..";
	
	String ADMIN_USER_EXISTS = "The EMAIL ID alread registered with user ...";
	String SUPER_USER_ROLE = "This user does not have supur user role , only super user can perform the Acivity.";
	
	String PG_ALREADY_CREATED = "PG details already created as per Input provided , please crosscheck the sensitive information." ;
	String PG_SERVICES = "nput PG services not defined in system , contact Administrator.";
	
	String MERCHANT_PG_ASSOCIATION_EXISTS = "The Association Between Merchant and PG already present.";
	String MERCHANT_PG_ASSOCIATION_NON_EXISTS = "The Association Between Merchant and PG not Found.";
	String PG_NOT_CREATED = "The Input PG detals not found in System.";
	String PG_NOT_ACTIVE = "The Input PG detals not ACTIVE in System.";
	String PG_SEVICE_CREATED = "The Mentioned service already associated with PG.";
	String PG_SERVICE_ASSOCIATION_NOT_FOUND = "PG and Provided services association not found.";
	String PG_SERVICE_NOT_FOUND = "No PG service avaiable with inut services.";
	String MERCHANT_PG_SERVICE_NOT_ASSOCIATED = "Merchnats associated with service , but service not found.";
	String MERCHANT_PG_SERVICE_NOT_FOUND = "The service either not active with PG and Merchants / not not associated";
	
	String USER_STATUS = "The input user status is not defined in system.";
	String OTP_MISMATCH = "The provided OTP is not valid for the user.";
	String OTP_EXPIRED = "The otp is expired please login again !!";
	
	String COMPLAINT_TYPE_EXISTS = "The mentioned complaint type already present in system.";
	String COMPLAINT_TYPE_NOT_EXISTS = "The mentioned complaint type not present in system.";
	String COMPLAINT_TYPE_SUBTYPE_EXISTS = "The mentioned complaint type and Subtype already present in system.";
	String COMPLAINT_TYPE_SUBTYPE_NOT_EXISTS = "The mentioned complaint type and Subtype not present in system.";
	String COMPLAINT_ID_BLANK = "Input complaint ID should provided during complaint update operation.";
	String COMPLAINT_NOT_FOUND = "Provided Complaint id not found .";
	String COMPLAINT_ALREADY_CLOSED = "The provided Ticket already in closed state , please reopen the ticket/ create another new Ticket.";
	String COMPLAINT_NOT_CLOSED = "The provided Ticket not in closed status.";
	String COMPLAINT_TYPE_SUB_TYPE_STATUS = "The input Complaint type and Sub Type is not found as Active in system.";
	
	String DUPLICATE_ORDERID = "Order Id Already used";
	String INVALID_COOKIE = "Invalid Cookie Exception";
	String UNKNOWN_OPTION = "Invalid Option";
	String IFSC_VAIDATION_FAILED = "Input IFSC format is not valid";
	String ACCOUNT_NUMBER_VAIDATION_FAILED = "Input ACCOUNT NUMBER format is not valid";
	String TRANSACTION_STATUS = "The input transaction status is not defined in system.";
	String TRANSACTION_TYPE = "The input transaction type is not defined in system.";
	String WALLET_STATUS = "The input wallet status is not defined in system.";
	String WALLET_TYPE = "The input wallet type is not defined in system.";
	
	
	String DATE_FORMAT_VALIDATION = "Date field validaton failed , Date Format should be [DD-MM-YYYY] like [23-09-2021]";
	String DATA_NOT_FOUND = "Data with entered input not found!";

	String DATA_INVALID = "Invalid data in input field";
	String BANK_ERROR = "BANK Not Found";
	String CREATE_REQUEST_FAILED = "Create request failed";
	
	String DECRYPTION_ERROR = "Decription issue found , please contact admistrator.";
	
	String VALIDATION_ERROR_ORDER_AMOUNT = "Input Order Amount can't be negetive or 0";
	String VALIDATION_ERROR_PAYMENT_OPTION = "Input payment Option is not valid.";
	String VALIDATION_ERROR_CARD_NUMBER = "Input card number is not valid.";
	String VALIDATION_ERROR_CARD_HOLDER_NAME = "Input Card Holder name is not valid";
	
	String MERCHANT_NICK_NAME_EXITS = "Merchant with Nick Name Combination already exits in system.";
	String ACC_NOT_FOUND_WITH_BENEFICIARYID = "No Account information found as per provided Beneficiary ID , Contact administrator.";
	String ACC_NOT_ACTIVE_STATE = "The provided Beneficiary account already in deleted state.";
	
	String INPUT_ID_NOT_FOUND = "Input ID field value is not found in system.";
	
	String INSUFICIENT_BALANCE_E0100 = "Insufficient balance in wallet.";
	String PAYTM_CHECKSUM_ERROR_E0101 = "Paytm CheckSum generation error . contact Administrator.";
	String ACCOUNT_NOT_FOUND_E0102 = "Account not found";
	String WALLET_BALANCE_NOT_FOUND_E0103="Wallet Balance not retrive from Paytm , contact Administrator.";
	String WALLET_ORDERID_EXITS_E0104 = "Duplicate OrderId Exits in system.";
	String PAYTM_SYSTEM_EXCEPTION_ES1001 = "PAYTM SYSTEM EXCEPTION .";
	String DATE_VALIDATION_ERROR_E0105 = "Input date format is not valid, Please provide correct data format[yyyy-MM-dd]";
	String ACCOUNT_ORDERID_EXITS_E0106 = "Duplicate OrderId Exits in system.";
	String PHONE_VAIDATION_FILED_E0107 = "Input Mobile no format is not valid";
	String BANK_ACCOUNT_VAIDATION_FILED_E0108 = "Input Bank Account no format is not valid";
	String BANK_IFSC_VALIDATION_FAILED_E0109= "Input Bank IFSC code is not stanrdard. Contact administrator.";
	String BANK_UPI_VALIDATION_FAILED_E0110= "Input UPI Id is not stanrdard. Contact administrator.";
	String FROM_DATE_IS_INVALID_111 = "Invalid from date"; 
	String INVALID_WALLET_112 = "Wallet could not be found"; 
	String WALLET_TRANSFER_FAILED_E0113 = "Wallet Transfer failed";
	
	String WALLET_REPORT_FROM_DATE_E114 = "Please enter the mandatory fields to proceed ahead!";

	String REPORT_NOT_FOUND = "Report Not Found";
	String FILE_NOT_FOUND = "File Not Found";
	String DATE_FORMAT = "Date Format Not Correct";
	String END_DATE_ERROR = "END date is mandatory";
	String REPORT_NAME_EMPTY_NULL = "ReportName is mandatory";
	String START_DATE_ERROR = "START date is mandatory";
	String INTERNAL_ORDER_ID = "Internal oredr id is not found";
	

}
