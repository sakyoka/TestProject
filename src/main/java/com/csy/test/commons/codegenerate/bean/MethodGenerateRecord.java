package com.csy.test.commons.codegenerate.bean;

import java.util.Collection;

/**
 * 
 * 描述：方法记录
 * @author csy
 * @date 2022年3月15日 下午12:50:27
 */
public class MethodGenerateRecord {

	private Collection<String> impportBeans;//需要依赖的bean
	
	private String methodContent;//方法内容
	
	public MethodGenerateRecord(){}
	
	public MethodGenerateRecord(Collection<String> impportBeans, String methodContent){
		this.impportBeans = impportBeans;
		this.methodContent = methodContent;
	}

	public Collection<String> getImpportBeans() {
		return impportBeans;
	}

	public void setImpportBeans(Collection<String> impportBeans) {
		this.impportBeans = impportBeans;
	}

	public String getMethodContent() {
		return methodContent;
	}

	public void setMethodContent(String methodContent) {
		this.methodContent = methodContent;
	}
}
