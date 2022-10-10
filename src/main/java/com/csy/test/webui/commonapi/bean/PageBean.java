package com.csy.test.webui.commonapi.bean;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.Data;

@Data
public class PageBean {

	private Integer pageIndex = 1;
	
	private Integer pageSize = 10;
	
	private Page<?> page;
	
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(){
		this.page = PageHelper.startPage(pageIndex, pageSize);
		return (Page<T>) this.page;
	}
}
