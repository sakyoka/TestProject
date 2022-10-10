package com.csy.test.commons.patch.exception;

public class SourcePathExecutorException extends RuntimeException{
	
	private static final long serialVersionUID = 6802548640435525824L;

	public SourcePathExecutorException(String message){
        super(message);
    }

    public SourcePathExecutorException(String message , Throwable throwable){
        super(message , throwable);
    }

    public SourcePathExecutorException(Throwable throwable){
        super(throwable);
    }
}
