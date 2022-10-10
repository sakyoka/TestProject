package com.csy.test.commons.codegenerate.bean;

import com.csy.test.commons.codegenerate.database.defaults.EntityTranferToHump;
import com.csy.test.commons.codegenerate.database.defaults.EntityTranferToJavaType;
import com.csy.test.commons.entity.base.annotion.EntityTranfer;

/**
 * 
 * 描述:字段信息
 * @author csy
 * @date 2021年1月23日 下午12:35:04
 */
public class BeanFieldMessage {
	
	@EntityTranfer(sourceFieldName = "columnName" , entityTranferClazz = EntityTranferToHump.class)
	private String fieldName;//字段名
	
	@EntityTranfer(sourceFieldName = "columnType" , entityTranferClazz = EntityTranferToJavaType.class)
	private String fieldType;//字段类型
	
	@EntityTranfer(sourceFieldName = "columnTypeName")
	private String remarks;//注释
	
	@EntityTranfer
	private Boolean primaryKey;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
}
