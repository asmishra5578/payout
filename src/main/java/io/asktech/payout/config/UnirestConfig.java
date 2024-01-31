package io.asktech.payout.config;

import kong.unirest.Unirest;

public  class UnirestConfig {
	private static final Unirest Unirest = new Unirest();

	public static Unirest unirestConf() {
		
		return Unirest;
	

	}
}
