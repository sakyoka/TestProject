package com.csy.test.webui.taskmanage.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csy.test.commons.utils.UUID;
import com.csy.test.webui.commonapi.annotation.ServiceApi;
import com.csy.test.webui.commonapi.annotation.ServiceApiMethod;
import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.bean.PageDataBean;
import com.csy.test.webui.commonapi.utils.ApiBeanUtils;
import com.csy.test.webui.taskmanage.enums.TaskTypeEnum;
import com.csy.test.webui.taskmanage.mapper.LocalTaskMapper;
import com.csy.test.webui.taskmanage.mapper.TaskInfoMapper;
import com.csy.test.webui.taskmanage.mapper.UrlTaskMapper;
import com.csy.test.webui.taskmanage.model.JarTaskBean;
import com.csy.test.webui.taskmanage.model.LocalTaskBean;
import com.csy.test.webui.taskmanage.model.TaskInfoBean;
import com.csy.test.webui.taskmanage.model.UrlTaskBean;
import com.csy.test.webui.taskmanage.model.vo.TaskInfoPageVo;
import com.csy.test.webui.taskmanage.model.vo.TaskInfoVo;
import com.csy.test.webui.taskmanage.service.TaskManageService;

/**
 * 
 * 描述：任务管理
 * @author csy
 * @date 2022年9月30日 下午3:50:36
 */
@Service
@ServiceApi(serviceClass = "taskManage")
public class TaskManageServiceImpl implements TaskManageService {

	@Autowired
	TaskInfoMapper taskInfoMapper;
	
	@Autowired
	UrlTaskMapper urlTaskMapper;
	
	@Autowired
	LocalTaskMapper localTaskMapper;
	
	@Override
	@ServiceApiMethod
	@Transactional(rollbackFor = Exception.class)
	public void saveTaskInfo(ApiBean apiBean) {
		Map<String, Object> paramObject = ApiBeanUtils.paramlistToMap(apiBean);
		TaskInfoBean taskInfoBean = ApiBeanUtils.toBean(paramObject, TaskInfoBean.class);
		boolean update = StringUtils.isNotBlank(taskInfoBean.getTaskId());
		if (update){
			taskInfoBean.preUpdate();
			taskInfoMapper.updateIfNotEmpty(taskInfoBean);
		}else{
			taskInfoBean.preInsert();
			taskInfoMapper.insert(taskInfoBean);
		}
		
		this.disposeSaveRefTask(taskInfoBean, paramObject);
	}
	
	/**
	 * 
	 * 描述：保存任务关联信息
	 * @author csy
	 * @date 2022年10月9日 下午4:55:41
	 * @param taskInfoBean
	 * @param paramObject
	 */
	private void disposeSaveRefTask(TaskInfoBean taskInfoBean, Map<String, Object> paramObject){
		if (TaskTypeEnum.URL.getType().equals(taskInfoBean.getTaskType())){
			UrlTaskBean urlTaskBean = ApiBeanUtils.toBean(paramObject, UrlTaskBean.class);
			urlTaskMapper.deleteByTaskId(taskInfoBean.getTaskId());
			urlTaskBean.setUuid(UUID.getString());
			urlTaskMapper.insert(urlTaskBean);
		}
		
		if (TaskTypeEnum.JAR.getType().equals(taskInfoBean.getTaskType())){
			JarTaskBean jarTaskBean = ApiBeanUtils.toBean(paramObject, JarTaskBean.class);
		}
		
		if (TaskTypeEnum.CLASS.getType().equals(taskInfoBean.getTaskType())){
			LocalTaskBean localTaskBean = ApiBeanUtils.toBean(paramObject, LocalTaskBean.class);
			localTaskMapper.deleteByTaskId(taskInfoBean.getTaskId());
			localTaskBean.setUuid(UUID.getString());
			localTaskMapper.insert(localTaskBean);
		}	
	}

	@Override
	@ServiceApiMethod
	public void deleteTaskInfo(ApiBean apiBean) {
		
	}

	@Override
	@ServiceApiMethod
	public PageDataBean<TaskInfoPageVo> taskPageData(ApiBean apiBean) {
		
		return null;
	}

	@Override
	@ServiceApiMethod
	public TaskInfoVo getTaskInfo(ApiBean apiBean) {
		return null;
	}
}
