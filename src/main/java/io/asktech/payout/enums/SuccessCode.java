package io.asktech.payout.enums;

public enum SuccessCode {
	 API_SUCCESS, 
	 SESSION_ACTIVE,
	 EMAIL_VERIFICATION_SUCCESS, 
	 RE_SEND_EMAIL_SUCCESS, 
	 RESET_PASS_EMAIL_SUCCESS, 
	 RESET_PASSWORD_SUCCESS, 
	 EMAIL_AVAILABLE, 
	 PHONE_NUMBER_AVAILABLE, 
	 PHONE_NUMBER_REGISTERED, 
	 RESEND_OTP,
	 USER_LOGGED_OUT_SUCCESSFULLY,
	 MPIN_SET_SUCCESS;
    SuccessCode() {
	}
}
