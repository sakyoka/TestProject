package com.csy.test.webui.taskmanage.base;

/**
 * 
 * 描述：初始化任务接口
 * @author csy
 * @date 2022年9月10日 下午11:44:01
 */
public interface InitTaskBase {
	
	/**
	 * 
	 * 描述：任务名称
	 * @author csy
	 * @date 2022年9月29日 下午5:08:58
	 * @return 任务名称
	 */
	default String taskName(){
		return null;
	};
	
	/**
	 * 
	 * 描述：执行顺序（越小越靠前）
	 * @author csy
	 * @date 2022年9月11日 上午12:13:13
	 * @return 排序号
	 */
	default int order(){return 9999;};

	/**
	 * 
	 * 描述：执行方法
	 * @author csy
	 * @date 2022年9月10日 下午11:44:13
	 */
	void execute();
}
