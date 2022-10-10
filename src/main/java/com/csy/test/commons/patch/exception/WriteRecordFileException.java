package com.csy.test.commons.patch.exception;

public class WriteRecordFileException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
    public WriteRecordFileException(String message){
        super(message);
    }

    public WriteRecordFileException(String message , Throwable throwable){
        super(message , throwable);
    }

    public WriteRecordFileException(Throwable throwable){
        super(throwable);
    }
}
