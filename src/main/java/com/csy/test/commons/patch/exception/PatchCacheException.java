package com.csy.test.commons.patch.exception;

public class PatchCacheException extends RuntimeException{
	
	private static final long serialVersionUID = -7882141777610469218L;

	public PatchCacheException(String message){
        super(message);
    }

    public PatchCacheException(String message , Throwable throwable){
        super(message , throwable);
    }

    public PatchCacheException(Throwable throwable){
        super(throwable);
    }
}
