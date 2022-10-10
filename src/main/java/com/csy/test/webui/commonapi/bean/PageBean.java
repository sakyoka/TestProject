package com.csy.test.webui.commonapi.bean;

import lombok.Data;

@Data
public class PageBean {

	private Integer pageIndex = 1;
	
	private Integer pageSize = 10;
}
