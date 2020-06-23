package com.csy.test.commons.entity.exception;

public class FieldExecuteException extends RuntimeException{
	
	private static final long serialVersionUID = -7511519912978192973L;

	public FieldExecuteException(String messgae) {
		super(messgae);
	}
	
	public FieldExecuteException(String messgae , Throwable cause) {
		super(messgae , cause);
	}
}
