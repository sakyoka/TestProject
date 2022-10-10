package com.csy.test.commons.result;

import lombok.Builder;
import lombok.Data;

/**
 * 描述：结果对象
 * @author csy
 * @date 2022年1月13日 上午11:22:53
 */
@Data
@Builder
public class ResultBean {
	
	private String code;
	
	private String msg;
	
	private Object data;
	
	public static final String OK = "200";
	
	public static final String BAD_REQUEST = "400";
	
	public static final String INTERNAL_ERROR = "500";
	
	public static final String UNAUTHORIZED = "401";
	
	public static ResultBean ok(String msg, Object data){
		return ResultBean.builder()
				           .code(OK)
				           .msg(msg)
				           .data(data)
				           .build();
	}
	
	public static ResultBean ok(Object data){
		return ok("ok", data);
	}
	
	public static ResultBean badRequest(String msg, Object data){
		return ResultBean.builder()
				           .code(BAD_REQUEST)
				           .msg(msg)
				           .data(data)
				           .build();
	}
	
	public static ResultBean badRequest(Object data){
		return badRequest("bad request", data);
	}
}
