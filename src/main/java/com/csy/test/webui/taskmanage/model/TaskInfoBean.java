package com.csy.test.webui.taskmanage.model;

import java.util.Date;

import com.csy.test.commons.database.annotation.Column;
import com.csy.test.commons.database.annotation.Table;
import com.csy.test.commons.utils.UUID;
import com.csy.test.webui.taskmanage.enums.ExecuteStatusEnum;
import com.csy.test.webui.taskmanage.enums.TaskNormalEnum;

import lombok.Data;

/**
 * 
 * 描述：任务定义对象
 * <br> 1、任务管理模块作为@Table、@Column实践模块，直接用数据库保存、操作数据，不使用本地文件存储
 * <br> 2、jar管理模块。可以忽略这个任务管理模块的使用。
 * @author csy
 * @date 2022年9月29日 上午11:45:43
 */
@Data
@Table(tableName = "scheduler_task_info", tableRemarks ="任务信息表")
public class TaskInfoBean {

	@Column(isPrimaryKey = true, columnRemarks = "主键值", columnSize = 32)
	private String uuid;

	@Column(columnRemarks = "任务名称", columnSize = 128)
	private String taskName;
	
	@Column(columnRemarks = "任务描述", columnSize = 255)
	private String taskDesc;

	@Column(columnRemarks = "任务id唯一标识", columnSize = 64)
	private String taskId;
	
	@Column(columnRemarks = "任务Key标识", columnSize = 32)
	private String taskKey;

	@Column(columnRemarks = "调度表达式", columnSize = 64)
	private String cron;

	@Column(columnRemarks = "执行时间")
	private Date startTime;

	@Column(columnRemarks = "调度类型：0使用表达式，1使用到点执行")
	private Integer dispatchType;
	
	@Column(columnRemarks = "任务类型：0 执行简单的url请求，1添加jar包形式，2指定执行类")
	private Integer taskType;
	
	@Column(columnRemarks = "延迟执行时长 0时候不延长", columnSize = 12)
	private Integer delayTime;

	@Column(columnRemarks = "执行状态：0停止，1执行中，2执行失败")
	private Integer executeStatus;
	
	@Column(columnRemarks = "创建时间")
	private Date createTime;
	
	@Column(columnRemarks = "修改时间")
	private Date updateTime;
	
	@Column(columnRemarks = "是否有效0否，1是")
	private Integer enable;
	
	public void preInsert(){
		this.createTime = new Date();
		this.updateTime = this.createTime;
		this.uuid = UUID.getString();
		this.taskId = UUID.getString();
		this.enable = TaskNormalEnum.ENABLE.getType();
		this.executeStatus = ExecuteStatusEnum.STOP.getType();
	}
	
	public void preUpdate(){
		this.updateTime = new Date();
	}
}
