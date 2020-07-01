package com.csy.test.commons.excel.exception;

public class ExcelImportFormatException extends RuntimeException{

	private static final long serialVersionUID = -5247418704402200885L;

	public ExcelImportFormatException(String message, Exception e) {
		super(message , e);
	}
	
	public ExcelImportFormatException(Exception e) {
		super(e);
	}
	
	public ExcelImportFormatException(String message) {
		super(message);
	}
	
}
