package com.csy.test.webui.taskmanage.model;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;

import lombok.Data;

/**
 * 
 * 描述：本地方法
 * @author csy
 * @date 2022年9月29日 下午3:21:49
 */
@Data
@Table(tableName = "scheduler_task_ref_local", tableRemarks ="本地方法的任务表")
public class LocalTaskBean {

	@Column(isPrimaryKey = true, columnRemarks = "主键值", columnSize = 32)
	private String uuid;
	
	@Column(columnRemarks = "任务ID", columnSize = 64)
	private String taskId;
	
	@Column(columnRemarks = "方法名", columnSize = 128)
	private String methodName;
	
	@Column(columnRemarks = "实例名字", columnSize = 255)
	private String instanceName;
	
	@Column(columnRemarks = "请求的参数，以JOSN字符串传过来", columnSize = 255)
	private String requestParam;
}
