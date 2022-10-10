package com.csy.test.webui.taskmanage.model.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 
 * 描述：任务信息分页展示vo对象
 * @author csy
 * @date 2022年9月30日 下午4:43:05
 */
@Data
public class TaskInfoPageVo{

	/**主键值*/
	private String uuid;

	/**任务名称*/
	private String taskName;
	
	/**任务描述*/
	private String taskDesc;

	/**任务id唯一标识*/
	private String taskId;
	
	/**任务Key标识*/
	private String taskKey;

	/**调度表达式*/
	private String cron;

	/**执行时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**调度类型：0使用表达式，1使用到点执行*/
	private Integer dispatchType;
	
	/**任务类型：0 执行简单的url请求，1添加jar包形式*/
	private Integer taskType;

	/**执行状态：0停止，1执行中，2执行失败*/
	private Integer executeStatus;
	
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**修改时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	/**是否有效0否，1*/
	private Integer enable;
}
