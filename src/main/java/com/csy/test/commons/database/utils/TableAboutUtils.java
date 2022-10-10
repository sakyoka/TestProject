package com.csy.test.commons.database.utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.csy.test.commons.utils.DataBase;
import com.csy.test.commons.utils.PrintUtils;

/**
 * 
 * 描述：表操作
 * @author csy
 * @date 2022年9月22日 下午6:08:33
 */
public class TableAboutUtils {

	private TableAboutUtils(){}
	
	/**
	 * 
	 * 描述：生成表
	 * @author csy
	 * @date 2022年9月23日 上午8:49:08
	 * @param cls
	 */
	public static void generate(Class<?> cls){
		TableGenerateUtils.generate(DataBase.getConnection(), cls);
	}
	
	/**
	 * 
	 * 描述：生成表
	 * @author csy
	 * @date 2022年9月23日 上午9:56:05
	 * @param entityClses
	 */
	public static void generate(List<Class<?>> entityClses){
		Connection conn = DataBase.getConnection();
		try {
			entityClses.forEach((cls) -> {
				TableGenerateUtils.generate(conn, cls, false);
			});
		} finally{
			DataBase.getCloseObject().closeConnection(conn);
		}
	}
	
	/**
	 * 
	 * 描述：创建表，如果存在更新
	 * @author csy
	 * @date 2022年9月23日 下午3:04:52
	 * @param cls
	 */
	@SuppressWarnings("serial")
	public static void generateAndIfExistsUpdate(Class<?> cls){
		generateAndIfExistsUpdate(new ArrayList<Class<?>>(1){{this.add(cls);}});
	}
	
	/**
	 * 
	 * 描述：创建表，如果存在更新
	 * @author csy
	 * @date 2022年9月23日 下午3:05:08
	 * @param entityClses
	 */
	public static void generateAndIfExistsUpdate(List<Class<?>> entityClses){
		Connection conn = DataBase.getConnection();
		try {
			entityClses.stream().filter((cls) -> {
				return !TableGenerateUtils.generate(conn, cls, false);
			}).forEach((cls) -> {
				PrintUtils.println("检测修改:%s", cls.getName());
				TableUpdateUtils.updateTable(conn, cls);
			});
		} finally{
			DataBase.getCloseObject().closeConnection(conn);
		}		
	}
	
	/**
	 * 
	 * 描述：更新表
	 * @author csy
	 * @date 2022年9月23日 下午2:55:56
	 */
	public static void updateTable(Class<?> cls){
		TableUpdateUtils.updateTable(DataBase.getConnection(), cls);
	}
	
	/**
	 * 
	 * 描述：更新表
	 * @author csy
	 * @date 2022年9月23日 下午3:16:50
	 * @param entityClses
	 */
	public static void updateTable(List<Class<?>> entityClses){
		Connection conn = DataBase.getConnection();
		try {
			entityClses.forEach((cls) -> {
				TableUpdateUtils.updateTable(conn, cls, false);
			});
		} finally{
			DataBase.getCloseObject().closeConnection(conn);
		}		
	}
}
