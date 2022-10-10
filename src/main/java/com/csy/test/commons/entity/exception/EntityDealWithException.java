package com.csy.test.commons.entity.exception;

public class EntityDealWithException extends RuntimeException{
	
	private static final long serialVersionUID = 7762569686467908405L;

	public EntityDealWithException(String messgae) {
		super(messgae);
	}
	
	public EntityDealWithException(String messgae , Throwable cause) {
		super(messgae , cause);
	}
}
