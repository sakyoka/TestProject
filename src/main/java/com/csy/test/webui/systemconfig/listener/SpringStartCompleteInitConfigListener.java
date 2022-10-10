package com.csy.test.webui.systemconfig.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.webui.taskmanage.utils.InitTaskUtils;

/**
 * 
 * 描述:启动完之后初始化配置
 * @author csy
 * @date 2022年1月15日 下午9:36:12
 */
@Component
public class SpringStartCompleteInitConfigListener implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		
		PrintUtils.println("====================== >>>>>>>>>> execute SpringStartCompleteInitConfigListener");
		
		InitTaskUtils.executeInitTasks(new String[0]);
	}
}
