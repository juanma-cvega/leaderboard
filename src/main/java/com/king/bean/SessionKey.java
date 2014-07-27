package com.king.bean;

import java.util.Date;

public class SessionKey {

	private final String key;
	private final Date expirationDay;
	
	public SessionKey(String key, long expirationTime) {
		this.key = key;
		this.expirationDay = new Date(System.currentTimeMillis() + expirationTime);
	}

	public String getKey() {
		return key;
	}

	public Date getExpirationDay() {
		return expirationDay;
	}
	
}
