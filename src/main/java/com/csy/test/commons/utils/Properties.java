package com.csy.test.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

/**
 * 配置文件属性
 * 描述：
 * @author csy
 * @date 2020年7月17日 上午11:46:03
 */
public class Properties {
	
	private Properties(){}
	
	//private static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private static final ConcurrentMap<String , String> concurrentMap = new ConcurrentHashMap<String , String>();

    static {
        /**
         * 初始化值 concurrentMap
         **/
        load();
    }

    /**
     * 描述：获取配置文件值
     * @author csy 
     * @date 2020年7月17日 上午11:45:26
     * @param key
     * @param defaultValue
     * @return String
     */
    public static String get(String key , String defaultValue){
        if (concurrentMap.containsKey(key)){
            String value = concurrentMap.get(key);
            return StringUtils.isBlank(value) ? defaultValue : value;
        }else{
            return defaultValue;
        }
    }

    /**
     * 描述：获取配置文件值
     * @author csy 
     * @date 2020年7月17日 上午11:45:12
     * @param key 
     * @return String
     */
    public static String get(String key){
        return get(key , null);
    }

    /**
     * 描述：加载
     * @author csy 
     * @date 2020年7月17日 上午11:45:05
     */
    private static void load(){
        InputStream inputStream = null;
        try {
            concurrentMap.clear();
            String path = System.getProperty("user.dir") + File.separator + "application.properties";
            File file = new File(path);
            java.util.Properties properties = new java.util.Properties();
            //先读取外部文件
            if (file.exists()){
                inputStream = new FileInputStream(file);
                properties.load(inputStream);
                Set<Map.Entry<Object , Object>> entrySet =  properties.entrySet();
                for (Map.Entry<Object , Object> entry:entrySet){
                    concurrentMap.put(entry.getKey().toString() , ( entry.getValue() == null ? null : entry.getValue().toString() ));
                }
            }
            //读取程序里面的文件， 如果已存在不进行覆盖，跳过
            inputStream = Properties.class.getClassLoader().getResourceAsStream("application.properties");
            if (inputStream != null){
                properties.clear();
                properties.load(inputStream);
                Set<Map.Entry<Object , Object>> entrySet =  properties.entrySet();
                String key;
                for (Map.Entry<Object , Object> entry:entrySet){
                    key = entry.getKey().toString();
                    if (! concurrentMap.containsKey(key)){
                        concurrentMap.put(key , ( entry.getValue() == null ? null : entry.getValue().toString() ));
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 描述：获取所有值
     * @author csy 
     * @date 2020年7月17日 上午11:43:58
     * @return Map<String , String>
     */
    public static Map<String , String> allProperties(){
        return Collections.unmodifiableMap(concurrentMap);
    }
}
