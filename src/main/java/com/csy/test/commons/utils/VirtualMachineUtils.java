package com.csy.test.commons.utils;

import java.io.File;

import com.csy.test.commons.jarmanage.constants.JarAgentConstants;
import com.sun.tools.attach.VirtualMachine;

/**
 * 
 * 描述：虚拟机工具类
 * @author csy
 * @date 2022年6月9日 下午2:24:10
 */
public class VirtualMachineUtils {

    /**
     * 描述：执行代理包
     * @author csy 
     * @date 2022年6月9日 下午12:34:44
     * @param pid
     */
    public static void invokeAgentmain(String pid, String jarPath){
		try {
			VirtualMachine vm = VirtualMachine.attach(pid);
			vm.loadAgent(JarAgentConstants.getAgentmainRootDir() + File.separator 
					+ JarAgentConstants.AGENT_AGENTMAIN_JAR_FILE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
    }
}
