package com.csy.test.ui.patch.exception;

public class DeloyParamCacheException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
    public DeloyParamCacheException(String message){
        super(message);
    }

    public DeloyParamCacheException(String message , Throwable throwable){
        super(message , throwable);
    }

    public DeloyParamCacheException(Throwable throwable){
        super(throwable);
    }
}
