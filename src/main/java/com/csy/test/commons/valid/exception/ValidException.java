package com.csy.test.commons.valid.exception;

/**
 * 校验异常类
 * @author csy
 * @date 2020年6月12日
 */
public class ValidException extends RuntimeException{

    private static final long serialVersionUID = -1714005257490028257L;

	public ValidException(){
        super();
    }

    public ValidException(String message){
        super(message);
    }

    public ValidException(String message , Throwable throwable){
        super(message , throwable);
    }

    public ValidException(Throwable throwable){
        super(throwable);
    }
}
