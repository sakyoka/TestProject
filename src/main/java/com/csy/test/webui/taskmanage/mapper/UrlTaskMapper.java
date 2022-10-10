package com.csy.test.webui.taskmanage.mapper;

import com.csy.test.webui.taskmanage.model.UrlTaskBean;


/**
 * 描述：url任务表dao
 * @author csy
 * @date 2022-10-09 17:34:18
 */
public interface UrlTaskMapper {

    /**
     * 描述：get a record
     * @author csy
     * @date 2022-10-09 17:34:18
     * @param uuid 
     * @return urlTaskBean
     */
    UrlTaskBean get(String uuid);

    /**
     * 描述：insert a record
     * @author csy
     * @date 2022-10-09 17:34:18
     * @param urlTaskBean 
     * @return int
     */
    int insert(UrlTaskBean urlTaskBean);

    /**
     * 描述：update a record
     * @author csy
     * @date 2022-10-09 17:34:18
     * @param urlTaskBean 
     * @return int
     */
    int update(UrlTaskBean urlTaskBean);

    /**
     * 描述：delete a record
     * @author csy
     * @date 2022-10-09 17:34:18
     * @param uuid 
     * @return int
     */
    int delete(String uuid);

    /**
     * 
     * 描述：根据任务id删除
     * @author csy
     * @date 2022年10月9日 下午5:39:30
     * @param taskId
     * @return int
     */
    int deleteByTaskId(String taskId);
}