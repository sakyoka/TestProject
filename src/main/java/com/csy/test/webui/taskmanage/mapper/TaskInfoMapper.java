package com.csy.test.webui.taskmanage.mapper;

import java.util.List;

import com.csy.test.webui.taskmanage.model.TaskInfoBean;
import com.csy.test.webui.taskmanage.model.query.TaskInfoPageQuery;

/**
 * 描述：任务信息表dao
 * @author csy
 * @date 2022-10-09 13:56:55
 */
public interface TaskInfoMapper {

    /**
     * 描述：get collections record
     * @author csy
     * @date 2022-10-09 13:56:55
     * @param taskInfoPageQuery 
     * @return List<TaskInfoBean>
     */
    List<TaskInfoBean> findList(TaskInfoPageQuery taskInfoPageQuery);

    /**
     * 描述：get a record
     * @author csy
     * @date 2022-10-09 13:56:55
     * @param uuid 
     * @return TaskInfoBean
     */
    TaskInfoBean get(String uuid);

    /**
     * 描述：insert a record
     * @author csy
     * @date 2022-10-09 13:56:55
     * @param schedulerTaskInfoModel 
     * @return int
     */
    int insert(TaskInfoBean taskInfoBean);

    /**
     * 描述：update a record
     * @author csy
     * @date 2022-10-09 13:56:55
     * @param schedulerTaskInfoModel 
     * @return int
     */
    int update(TaskInfoBean taskInfoBean);

    /**
     * 描述：delete a record
     * @author csy
     * @date 2022-10-09 13:56:55
     * @param uuid 
     * @return int
     */
    int delete(String uuid);
    
    /**
     * 
     * 描述：修改数据
     * @author csy
     * @date 2022年10月9日 下午3:35:15
     * @param taskInfoBean
     * @return int
     */
    int updateIfNotEmpty(TaskInfoBean taskInfoBean);
}
