package com.csy.test.webui.taskmanage.service;

import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.bean.PageDataBean;
import com.csy.test.webui.taskmanage.model.vo.TaskInfoPageVo;
import com.csy.test.webui.taskmanage.model.vo.TaskInfoVo;

/**
 * 
 * 描述：任务管理
 * @author csy
 * @date 2022年9月30日 下午4:34:21
 */
public interface TaskManageService {

	/**
	 * 
	 * 描述：保存任务信息
	 * @author csy
	 * @date 2022年9月30日 下午4:37:01
	 * @param apiBean
	 */
	void saveTaskInfo(ApiBean apiBean);
	
	/**
	 * 
	 * 描述：删除任务
	 * @author csy
	 * @date 2022年9月30日 下午4:47:38
	 * @param apiBean
	 */
	void deleteTaskInfo(ApiBean apiBean);
	
	/**
	 * 
	 * 描述：任务信息的分页数据
	 * @author csy
	 * @date 2022年9月30日 下午4:47:52
	 * @param apiBean
	 * @return
	 */
	PageDataBean<TaskInfoPageVo> taskPageData(ApiBean apiBean);
	
	/**
	 * 
	 * 描述：获取任务信息
	 * @author csy
	 * @date 2022年9月30日 下午4:48:57
	 * @param apiBean
	 * @return
	 */
	TaskInfoVo getTaskInfo(ApiBean apiBean);
}
