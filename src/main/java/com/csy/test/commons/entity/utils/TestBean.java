package com.csy.test.commons.entity.utils;

import com.alibaba.fastjson.JSONObject;
import com.csy.test.commons.entity.build.BeanBuilder;

public class TestBean {
	
	private String name;
	
	private String say;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSay() {
		return say;
	}

	public void setSay(String say) {
		this.say = say;
	}

	public static void main(String[] args) {
		TestBean test = new BeanBuilder<>(TestBean.class)
				              .set("name", "hahha")
				              .set("say", 1)
				              .set("age", 24)
				              .build(true);
		System.out.println(JSONObject.toJSONString(test));
	}
}
