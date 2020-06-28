package com.csy.test.commons.excel.utils;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;

@ExportExcelHeader
public class Test {
	
	@ExportExcelField(order = 0)
	private String name;

	public Test(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		List<Test> tests = new ArrayList<Test>();
		tests.add(new Test("测试"));
		ExcelUtils.exportExcel("D:\\test.xls", tests, Test.class);
	}
}
