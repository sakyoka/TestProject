package com.csy.test.commons.codegenerate.bean;

import com.csy.test.commons.codegenerate.annotation.MapperTemplate;

/**
 * 
 * 描述：mybatis xml类填充对象
 * @author csy
 * @date 2021年1月22日 下午5:41:46
 */
public class MybatisMapperTemplateBase {

	private String daoPath;
	
	private String javaBeanPath;
	
	@MapperTemplate(idName = "findList" , tempName = "findListId" , methodType = 0 , desc = "get collections record")
	private String findListSql;
	
	@MapperTemplate(idName = "get" , tempName = "getOneId" , methodType = 1 , desc = "get a record")
	private String getOneSql;
	
	@MapperTemplate(idName = "insert" , tempName = "insertId" , methodType = 2 , desc = "insert a record")
	private String insertSql;
	
	@MapperTemplate(idName = "update" , tempName = "updateId" , methodType = 3 , desc = "update a record")
	private String updateSql;
	
	@MapperTemplate(idName = "delete" , tempName = "deleteId" , methodType = 4 , desc = "delete a record")
	private String deleteSql;
	
	public MybatisMapperTemplateBase() {}
	
	public MybatisMapperTemplateBase(String daoPath , String javaBeanPath , 
			String findListSql, String getOneSql, String insertSql, String updateSql, String deleteSql) {
		this.daoPath = daoPath;
		this.javaBeanPath = javaBeanPath;
		this.findListSql = findListSql;
		this.getOneSql = getOneSql;
		this.insertSql = insertSql;
		this.updateSql = updateSql;
		this.deleteSql = deleteSql;
	}

	public String getDaoPath() {
		return daoPath;
	}

	public void setDaoPath(String daoPath) {
		this.daoPath = daoPath;
	}

	public String getJavaBeanPath() {
		return javaBeanPath;
	}

	public void setJavaBeanPath(String javaBeanPath) {
		this.javaBeanPath = javaBeanPath;
	}

	public String getFindListSql() {
		return findListSql;
	}

	public void setFindListSql(String findListSql) {
		this.findListSql = findListSql;
	}

	public String getGetOneSql() {
		return getOneSql;
	}

	public void setGetOneSql(String getOneSql) {
		this.getOneSql = getOneSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getDeleteSql() {
		return deleteSql;
	}

	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}
}
