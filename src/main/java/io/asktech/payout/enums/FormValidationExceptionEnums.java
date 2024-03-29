package io.asktech.payout.enums;

public enum FormValidationExceptionEnums {
	FILE_NOT_FOUND,
	REPORT_NOT_FOUND,
	DATE_FORMAT,
	NOT_A_FIELED,
	PAYMENT_TYPE_INPUT_ERROR,
	SIZE_MAX_REACHED,
	COMPLAIN_STATUS,
	ORDER_ID_NOT_FOUND,
	WALLET_STATUS_ERROR,
	WALLET_NOT_FOUND,
	GATEWAY_INPUT_ERROR,
	TRANSACTION_TYPE_INPUT_ERROR,
	SLAB_INPUT_ERROR,
	COMMISSION_TYPE_INPUT_ERROR,
	INFORMATION_ALREADY_EXISTS,
	INFORMATION_DOES_NOT_EXISTS,
	INPUT_VALIDATION_ERROR,
	REQUIRED_INFORMATION_NOT_FOUND,
	FATAL_EXCEPTION,
	ALL_FIELDS_MANDATORY,
	FIELED_NOT_FOUND,
	UNICONTRAINT_VIOLATION,
	SQLERROR_EXCEPTION,
	SIGNATURE_VERIFICATION_ERROR,
	SIGNATURE_VERIFICATION_FAILED,
	JSON_PARSE_EXCEPTION,
	MERCHANT_ALREADY_EXISTS,
	MERCHANT_NOT_FOUND,
	PG_SERVICE_NOT_MAPPED_WITH_MERCHANT,
	EMAIL_ID_NOT_FOUND,
	USER_STATUS_BLOCKED,
	USER_STATUS_REMOVED,
	PASSWORD_VALIDATION_ERROR,
	INITIAL_PASSWORD_CHANGE_REQUIRED,
	SESSION_NOT_FOUND,
	JWT_EXPIRED,
	JWT_MISSING,
	JWT_SIGNATURE_MISSING,
	JWT_FORMATE_INVALID,
	JWT_UNSUPPORTED,
	SESSION_EXPIRED,
	IDEAL_SESSION_EXPIRED,
	JWT_NOT_VALID,
	USER_NOT_FOUND,
	REST_TEMPLATE_NOT_CALL,
	JWT_ILLEGAL_ARGUMENT,
	SESSION_DEAD,
	MERCHANT_BANK_DETAILS_EXISTS,
	MERCHANT_PG_APP_ID_NOT_FOUND,
	FORM_VALIDATION_FILED, MERCHANT_PG_SECRET_NOT_FOUND, MERCHANT_PG_NAME_NOT_FOUND, DUPLICATE_COMMISSON_FOR_MERCHANT,
	PG_VLIDATION_ERROR,
	MERCHANT_BANK_DETILS_NOT_FOUND, EMAIL_ALREADY_EXISTS, USER_ROLE_ISSUE, PG_ALREADYCREATED,
	MERCHANT_PG_ASSOCIATION_EXISTS, PG_NOT_CREATED, PG_SERVICE_PRESENT,
	PG_NOT_ACTIVE, MERCHANT_PG_ASSOCIATION_NON_EXISTS, PG_SERVICE_ASSOCIATION_NOT_FOUND, USER_STATUS,
	PG_SERVICES_DEFINED, MERCHANT_PG_SERVICE_NOT_FOUND,
	DUPLICATE_ORDERID, INVALID_COOKIE_EXCEPTION, UNKNOWN_OPTION, DATE_FORMAT_VALIDATION, PG_SERVICE_NOT_FOUND,
	MERCHANT_PG_SERVICE_NOT_ASSOCIATED, DATA_INVALID,
	BANK_ERROR, CREATE_REQUEST_FAILED, DECRYPTION_ERROR, PASSWORD_VALIDATION, OTP_MISMATCH, OTP_EXPIRED,
	FORM_VALIDATION_FAILED, COMPLAINT_TYPE_EXISTS, COMPLAINT_TYPE_NOT_EXISTS,
	COMPLAINT_TYPE_SUBTYPE_EXISTS, COMPLAINT_TYPE_SUBTYPE_NOT_EXISTS, COMPLAINT_ID_BLANK, COMPLAINT_NOT_FOUND,
	COMPLAINT_ALREADY_CLOSED, COMPLAINT_NOT_CLOSED, COMPLAINT_TYPE_SUB_TYPE_STATUS, MERCHANT_NICK_NAME_EXITS,
	ACC_NOT_FOUND_WITH_BENEFICIARYID, ACC_NOT_ACTIVE_STATE, MERCHANT_EXITS, INPUT_ID_NOT_FOUND,
	START_DATE_ERROR,END_DATE_ERROR,
	REPORT_NAME_EMPTY_NULL,
	E0100,
	E0101,
	E0102,
	E0103,
	E0104,
	E0105,
	E0106,
	E0107,
	E0108,
	E0109,

	ES1001, E0110, E0111, E0112, E114, PG_EXISTS, PG_BLANK, PG_NOT_EXISTS, NUMBER_ERROR, MERCHANT_ORDER_ID_NOT_FOUND, MERCHANT_ORDER_ID_VALIDATION, DATE_PARAMETER_IS_MANDATORY,INTERNAL_ORDER_ID_NOT_FOUND;

	FormValidationExceptionEnums() {

	}
}
