package com.csy.test.commons.excel.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.annotion.ImportExcelField;
import com.csy.test.commons.excel.base.ExcelImportConvertBase;
import com.csy.test.commons.excel.base.ExcelOperateBase;

@ExportExcelHeader(needIndex = true , needHead = true  , headerName = "Excel test")
public class Test {
	
//	@ImportExcelField(order = 1 , convertClazz = ExcelImportDoubleConvertToInteger.class)
//	@ExportExcelField(order = 1 , cellName = "年龄")
//	private Integer age;
	
	@ImportExcelField(order = 0)
	@ExportExcelField(order = 0, cellName = "事项名称")
	private String approveName;
	
	@ExportExcelField(order = 2, cellName = "事项编码")
	private String approveItem;
	
	@ExportExcelField(order = 3, cellName = "申请人员集合", subClazz = SubTest.class)
	private List<SubTest> subTests;
	
	public Test(){}

//	public Test(String name , Integer age) {
//		this.name = name;
//		this.age = age;
//	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}
	
	
//	
//	public Integer getAge() {
//		return age;
//	}
//
//	public void setAge(Integer age) {
//		this.age = age;
//	}
	
	public String getApproveItem() {
		return approveItem;
	}

	public void setApproveItem(String approveItem) {
		this.approveItem = approveItem;
	}
	
	public List<SubTest> getSubTests() {
		return subTests;
	}

	public void setSubTests(List<SubTest> subTests) {
		this.subTests = subTests;
	}

	public class SubTest{
		
		@ExportExcelField(order = 4, cellName = "申请人")
		private String name;
		
		@ExportExcelField(order = 5, cellName = "申请人年龄")
		private Integer age;

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
	
	interface DataMapper{
		
		@Insert(value = "insert into slt_approve_item_record(approve_name) values(#{approveName})")
		int insert(Test test);
		
		@Select(value = "select count(*) from slt_approve_item_record")
		int getAllCount();
	}

	public static void main(String[] args) {
//		String filePath = "C:\\Users\\csy\\Desktop\\test.xlsx";
//		String driver = Properties.get("jdbc.driver");
//		String url = Properties.get("jdbc.url");
//		String username = Properties.get("jdbc.username");
//		String password = Properties.get("jdbc.password"); 
//		
//		SqlSession session = null;
//		try {
//			Configuration configuration = new Configuration();
//			configuration.addMapper(DataMapper.class);
//						
//			DruidDataSource dataSource = new DruidDataSource();
//			dataSource.setDriverClassName(driver);
//			dataSource.setUrl(url);
//			dataSource.setUsername(username);
//			dataSource.setPassword(password);
//			
//			Environment environment = new Environment("test", 
//					new JdbcTransactionFactory(), 
//					dataSource);
//			configuration.setEnvironment(environment);
//			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//			session = sessionFactory.openSession();
//			
//			DataMapper mapper = session.getMapper(DataMapper.class);
//			List<Test> tests2 = ExcelUtils.xlsDataToBeans(1 , 1 , filePath, Test.class);
//			for (Test test:tests2){
//				mapper.insert(test);
//			}
//			session.commit();
//			System.out.println("after insert data count:" + mapper.getAllCount());
//		} finally {
//			if (session != null){
//				session.close();
//			}
//		}
		
		System.out.println("starting to execute...");
		Test test;
		long startTime = System.currentTimeMillis();
		List<Test> beanList = new ArrayList<Test>(12);
		for (int i = 0; i < 12; i++){
			test = new Test();
			test.setApproveItem("no-" + i);
			test.setApproveName("测试-" + i);
			if (i == 3 || i == 7){
				SubTest subTest;
				List<SubTest> subTests = new ArrayList<SubTest>();
				for (int k = 0; k < 1000; k++){
					subTest = new Test().new SubTest();
					subTest.setName("小明-" + k);
					subTest.setAge(17);
					subTests.add(subTest);
				}
				test.setSubTests(subTests);
			}
			beanList.add(test);
		}
		
		String group = null;
		String xlsType = "xls";
		String filePath = "D:\\测试" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "." + xlsType;
		Workbook workbook = ExcelUtils.beanListToXlsData(beanList, xlsType, Test.class, group);
		ExcelOperateBase.exportExcel(workbook, filePath);
		long endTime = System.currentTimeMillis();
		System.out.println("end. cost:" + (endTime -startTime)/1000 + "s");
	}
}
