package io.asktech.payout.exceptions;

import io.asktech.payout.enums.FormValidationExceptionEnums;

public class FormValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4236137461216744410L;
	
	public FormValidationException(String msg, FormValidationExceptionEnums exception) {
		super(msg);
	}

}
