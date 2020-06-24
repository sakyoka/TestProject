package com.csy.test.commons.entity.exception;

public class InitForeachException extends RuntimeException{
	
	private static final long serialVersionUID = -4689165550654120196L;

	public InitForeachException(String messgae) {
		super(messgae);
	}
	
	public InitForeachException(String messgae , Throwable cause) {
		super(messgae , cause);
	}
}
