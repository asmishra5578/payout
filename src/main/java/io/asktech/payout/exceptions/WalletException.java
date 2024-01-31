package io.asktech.payout.exceptions;

import io.asktech.payout.enums.WalletExceptionEnums;

public class WalletException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4236137461216744410L;
	
	public WalletException(String msg, WalletExceptionEnums exception) {
		super(msg);
	}

}
