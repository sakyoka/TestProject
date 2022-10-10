package com.csy.test.webui.taskmanage.mapper;

import com.csy.test.webui.taskmanage.model.LocalTaskBean;


/**
 * 描述：本地方法的任务表dao
 * @author csy
 * @date 2022-10-09 17:41:54
 */
public interface LocalTaskMapper {

    /**
     * 描述：get a record
     * @author csy
     * @date 2022-10-09 17:41:54
     * @param uuid 
     * @return localTaskBean
     */
    LocalTaskBean get(String uuid);

    /**
     * 描述：insert a record
     * @author csy
     * @date 2022-10-09 17:41:54
     * @param localTaskBean 
     * @return int
     */
    int insert(LocalTaskBean localTaskBean);

    /**
     * 描述：update a record
     * @author csy
     * @date 2022-10-09 17:41:54
     * @param localTaskBean 
     * @return int
     */
    int update(LocalTaskBean localTaskBean);

    /**
     * 描述：delete a record
     * @author csy
     * @date 2022-10-09 17:41:54
     * @param uuid 
     * @return int
     */
    int delete(String uuid);

    /**
     * 
     * 描述：根据taskid删除
     * @author csy
     * @date 2022年10月9日 下午5:46:37
     * @param taskId
     * @return int
     */
    int deleteByTaskId(String taskId);
}