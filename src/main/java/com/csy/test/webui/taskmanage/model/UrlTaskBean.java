package com.csy.test.webui.taskmanage.model;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;

import lombok.Data;

/**
 * 
 * 描述：url请求的任务 内容
 * @author csy
 * @date 2022年9月29日 下午2:39:11
 */
@Data
@Table(tableName = "scheduler_task_ref_url", tableRemarks ="url任务表")
public class UrlTaskBean {
	
	@Column(isPrimaryKey = true, columnRemarks = "主键值", columnSize = 32)
	private String uuid;
	
	@Column(columnRemarks = "请求地址", columnSize = 128)
	private String requestUrl;
	
	@Column(columnRemarks = "任务ID", columnSize = 64)
	private String taskId;
	
	@Column(columnRemarks = "请求类型：0、post，1、get")
	private String methodType;
	
	@Column(columnRemarks = "请求的contentType类型", columnSize = 64)
	private String contentType;
	
	@Column(columnRemarks = "请求的参数，以JOSN字符串传过来", columnSize = 255)
	private String requestJsonParam;
}
