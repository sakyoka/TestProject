package com.csy.test.commons.jarmanage.exception;

/**
 * 
 * 描述：jar管理异常
 * @author csy
 * @date 2022年1月25日 下午2:57:03
 */
public class JarManageException extends RuntimeException{

	private static final long serialVersionUID = 6981936738008937276L;

	public JarManageException(){}  
	
	public JarManageException(String message) {
        super(message);
    }  
	
	public JarManageException(Throwable t) {
        super(t);
    }
	
	public JarManageException(String message,Throwable t) {
        super(message, t);
    }
}
