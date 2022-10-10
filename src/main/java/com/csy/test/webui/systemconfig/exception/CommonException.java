package com.csy.test.webui.systemconfig.exception;

/**
 * 
 * 描述：公共异常类，没有细分
 * @author csy
 * @date 2022年1月25日 下午2:57:03
 */
public class CommonException extends RuntimeException{

	private static final long serialVersionUID = 6981936738008937276L;

	public CommonException(){}  
	
	public CommonException(String message) {
        super(message);
    }  
	
	public CommonException(Throwable t) {
        super(t);
    }
	
	public CommonException(String message,Throwable t) {
        super(message, t);
    }
}
