package com.csy.test.commons.valid.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 描述:校验结果
 * @author csy
 * @date 2020 下午11:48:30
 */
public class ParamValidResult {
	
	private ParamValidResult(){}
	
	private Map<String, Object> errorMessageMap;
	
	private boolean hasError = false;
	
	public static ParamValidResult getBuilder(){
		
		return new ParamValidResult();
	}
	
	public ParamValidResult paramError(String key , Object message){
		
		if (this.errorMessageMap == null)
			this.errorMessageMap = new HashMap<String, Object>();
		
		this.errorMessageMap.put(key, message);
		
		this.hasError = true;
		
		return this;
	}
	
	public Map<String, Object> getErrorMessageMap() {
		return errorMessageMap;
	}

	public boolean getHasError() {
		return hasError;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
