package com.csy.test.commons.jarmanage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarRefJmxRmiIpPortBean;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：jar ref jmx port管理
 * @author csy
 * @date 2022年1月30日 下午10:45:44
 */
public class JarJMXPortUtils {

	private JarJMXPortUtils(){}
	
	public static final String JMX_IP = "127.0.0.1";
	
	/**
	 * 最开始的端口号
	 */
	private static final Integer DEFAULT_POSRT = 12580;
	
	/**
	 * 可用的端口，收集不连贯的port
	 */
	private static final List<Integer> avaliablePorts = new ArrayList<Integer>();
	
	/**
	 * 下一个端口号
	 */
	private static Integer NEXT_PORT;
	
	public static synchronized void init() {
		avaliablePorts.clear();
		check();
	}
	
	/**
	 * 描述：获取可用的端口号
	 * @author csy 
	 * @date 2022年1月30日 下午10:54:21
	 * @return 端口号
	 */
	public static synchronized Integer getAvaliablePort(){
		//优先从集合从获取，没有的时候才计算累计
		if (!avaliablePorts.isEmpty()){
			int port = avaliablePorts.get(0);
			avaliablePorts.remove(0);
			return port;
		}
		int port = NEXT_PORT;
		NEXT_PORT += 1;
		return port;
	}
	
	/**
	 * 描述：初始化jar的jmx用到的port;
	 *       并且回收没有用到的port;
	 *       设置下一个port开始
	 * <br>如果直接删除jar配置文件,会影响下面的判断
	 * @author csy 
	 * @date 2022年1月31日 下午5:52:27
	 */
	private static void check(){
		//获取实例
		Map<String, JarManageBean> jarBeanMap = JarEntityUtils.getUnmodifiableMap();
		if (jarBeanMap.isEmpty()){
			NEXT_PORT = DEFAULT_POSRT;
			return ;
		}
		//需要更新的集合,这种数据是没有加JMX端口配置对象问题
		List<JarManageBean> replenishs = new ArrayList<JarManageBean>();
		//已经使用的端口号，因为移除jar原因，历史端口不连贯，需要收集起来，计算出中间可用的端口
		List<Integer> usedports = new ArrayList<Integer>();
		jarBeanMap.forEach((k, v) -> {
			JarRefJmxRmiIpPortBean jarRefJmxRmiIpPortBean = v.getJarRefJmxRmiIpPortBean();
			if (Objects.isNull(jarRefJmxRmiIpPortBean)){
				replenishs.add(v);
			}else{
				//占用的port
				usedports.add(jarRefJmxRmiIpPortBean.getPort());
			}
		}); 
		
		if (CollectionUtils.isNotEmpty(usedports)){
			portReSort(usedports);
			Integer endPort = usedports.get(usedports.size() -1);
			//收集中间还可以用的端口
			for (int i = DEFAULT_POSRT; i < endPort; i++){
				if (usedports.contains(i)){
					continue;
				}
				avaliablePorts.add(i);
			}
			
			//因为下面可能移除端口所以先赋值
			if (avaliablePorts.isEmpty()){
				NEXT_PORT = usedports.get(usedports.size() - 1) + 1;
			}else{
				int maxAvaliablePort = avaliablePorts.get(avaliablePorts.size() - 1);
				int maxUsePort = usedports.get(usedports.size() - 1);
				NEXT_PORT = (maxAvaliablePort > maxUsePort ? maxAvaliablePort : maxUsePort) + 1;
			}
			
			//如果没有端口号的集合大于可用端口集合，需要补充端口
			if (replenishs.size() > avaliablePorts.size()){
				int size = avaliablePorts.size() - replenishs.size();
				for (int i = 1; i <= size; i++){
					avaliablePorts.add(NEXT_PORT);
					NEXT_PORT += 1;
				}
			}
			
			//没有数据就不执行下面的了
			if (replenishs.isEmpty()){
				return ;
			}
			
			//填充没有端口的集合
			Iterator<Integer> iterator = avaliablePorts.iterator();
			int i = 0;
			JarManageBean updateJar = null;
			while(iterator.hasNext()){
				if (i > replenishs.size()){
					break;
				}
				Integer port = iterator.next();
				updateJar = replenishs.get(i);
				updateJar.setJarRefJmxRmiIpPortBean(JarRefJmxRmiIpPortBean.builder()
						.ip(JMX_IP)
						.port(port)
						.build());
				JarEntityUtils.storage(updateJar);
				iterator.remove();
				i += 1;
			}
		}else{
			int startPort = DEFAULT_POSRT;
			for (JarManageBean jarManageBean:replenishs){
				jarManageBean.setJarRefJmxRmiIpPortBean(JarRefJmxRmiIpPortBean.builder()
						.ip(JMX_IP)
						.port(startPort)
						.build());
				startPort += 1;
				JarEntityUtils.storage(jarManageBean);
			}
			NEXT_PORT = startPort;
		}
	}
	
	/**
	 * 描述：排序
	 * @author csy 
	 * @date 2022年1月31日 下午5:37:00
	 * @param ports
	 */
	private static void portReSort(List<Integer> ports){
		Collections.sort(ports, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2){
					return 1;
				}
				
				if (o1 == o2){
					return 0;
				}
				return -1;
			}
		});			
	}
}
