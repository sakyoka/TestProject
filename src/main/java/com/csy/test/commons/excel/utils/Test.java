package com.csy.test.commons.excel.utils;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;

@ExportExcelHeader(needIndex = true , needHead = true  , headerName = "Excel test")
public class Test {
	
	@ExportExcelField(order = 1 , cellName = "年龄")
	private Integer age;
	
	@ExportExcelField(order = 0 , cellName = "姓名")
	private String name;

	public Test(String name , Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public static void main(String[] args) {
		List<Test> tests = new ArrayList<Test>();
		tests.add(new Test("小杰" , 17));
		tests.add(new Test("明哥" , 18));
		ExcelUtils.exportExcel("D:\\test.xls", tests, Test.class);
	}
}
