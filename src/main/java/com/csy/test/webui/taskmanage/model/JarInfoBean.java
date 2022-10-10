package com.csy.test.webui.taskmanage.model;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;

import lombok.Data;

/**
 * 
 * 描述：jar包信息
 * @author csy
 * @date 2022年9月29日 下午2:52:47
 */
@Data
@Table(tableName = "scheduler_jar_info", tableRemarks ="jar包信息表")
public class JarInfoBean {

	@Column(isPrimaryKey = true, columnRemarks = "主键值", columnSize = 32)
	private String uuid;

	@Column(columnRemarks = "jar包存储相对地址",columnSize = 255)
	private String jarPath;
	
	@Column(columnRemarks = "jar包名",columnSize = 128)
	private String jarChName;
	
	@Column(columnRemarks = "jar包英文名",columnSize = 64)
	private String jarEnName;
	
	@Column(columnRemarks = "jar key标识",columnSize = 64)
	private String jarKey;
}
