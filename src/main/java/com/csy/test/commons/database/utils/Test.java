package com.csy.test.commons.database.utils;

import java.sql.SQLException;
import java.util.Date;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;
import com.csy.test.commons.database.enums.DatabaseTypeEnum;
import com.csy.test.commons.utils.DataBase;

import lombok.Data;

@Table(tableName = "appr_test_auto_create", tableRemarks = "自动生成表测试")
@Data
public class Test {

	@Column(isPrimaryKey = true, columnSize = 64, columnRemarks = "主键")
	private String seq;
	
	@Column(columnSize = 3, columnRemarks = "年龄")
	private Integer age;
	
	@Column(columnRemarks = "生日")
	private Date brirthDay;
	
	@Column(columnRemarks = "姓名")
	private String name;
	
	public static void main(String[] args) {
		//TableAboutUtils.generateAndIfExistsUpdate(Test.class);
		
//		TableGenerateUtils.getCreateColumnSqlsOfCls(Test.class, 
//				DatabaseTypeEnum.ORACLE.getType(), "appr_test_auto_create")
//		.forEach(System.out::println);
		
		System.out.println("===================================");
		
		try {
			TableGenerateUtils.getCreateTableSqlOfDataBase(DataBase.getConnection(), 
					"scheduler_task_ref_local")
			.forEach(System.out::println);
		} catch (SQLException e) {e.printStackTrace();}
	}
}
