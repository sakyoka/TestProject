package com.csy.test.commons.patch.exception;

public class NotFoundCompileClassException extends RuntimeException{
	
	private static final long serialVersionUID = -965790923308337492L;

	public NotFoundCompileClassException(String message){
        super(message);
    }

    public NotFoundCompileClassException(String message , Throwable throwable){
        super(message , throwable);
    }

    public NotFoundCompileClassException(Throwable throwable){
        super(throwable);
    }
}
