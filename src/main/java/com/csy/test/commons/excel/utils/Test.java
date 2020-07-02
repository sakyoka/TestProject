package com.csy.test.commons.excel.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.annotion.ImportExcelField;
import com.csy.test.commons.excel.base.ExcelImportConvertBase;

@ExportExcelHeader(needIndex = true , needHead = true  , headerName = "Excel test")
public class Test {
	
	@ImportExcelField(order = 1 , convertClazz = ExcelImportDoubleConvertToInteger.class)
	@ExportExcelField(order = 1 , cellName = "年龄")
	private Integer age;
	
	@ImportExcelField(order = 0)
	@ExportExcelField(order = 0 , cellName = "姓名")
	private String name;
	
	public Test(){}

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
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
	public class ExcelImportDoubleConvertToInteger implements ExcelImportConvertBase{

		@Override
		public <T> void convert(T entity, Field field, Cell cell) {
			Double cellValue = cell.getNumericCellValue();
			
			if (cellValue != null){
				Integer intValue = cellValue.intValue();
				try {
					field.set(entity, intValue);
				} catch (Exception e) {
					throw new RuntimeException("设置值失败,fieldName ===>>> " + field.getName() , e);
				}			
			}

		}
		
	}

	public static void main(String[] args) {
		String filePath = "D:\\test.xls";
		List<Test> tests = new ArrayList<Test>();
		tests.add(new Test("小杰" , 17));
		tests.add(new Test("明哥" , 18));
		ExcelUtils.exportExcel(filePath, tests, Test.class);
		
		List<Test> tests2 = ExcelUtils.xlsDataToBeans(2 , 1 , filePath, Test.class);
		System.out.println(tests2);
	}
}
