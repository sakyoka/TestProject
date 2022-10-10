package com.csy.test.commons.patch.exception;

public class PatchGenerateFailException extends RuntimeException{
	
	private static final long serialVersionUID = 1182837296837358358L;

	public PatchGenerateFailException(String message){
        super(message);
    }

    public PatchGenerateFailException(String message , Throwable throwable){
        super(message , throwable);
    }

    public PatchGenerateFailException(Throwable throwable){
        super(throwable);
    }
}
