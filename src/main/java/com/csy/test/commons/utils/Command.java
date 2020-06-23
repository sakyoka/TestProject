package com.csy.test.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 描述:执行命令
 * @author csy
 * @date 2020 上午12:32:25
 */
public class Command {
	
	public static Command getBuilder() {
		return new Command();
	}
	
	private String contents;
	
	private List<String> listContents;
	
	private boolean isPrint = false;
	
	private String commandStr;
	
	/**
	 * 
	 * 描述：命令数组
	 * @author csy
	 * @date 2020 上午12:32:40
	 * @param arrayCommandArr
	 * @return
	 */
	public Command commandStr(String commandStr) {
		this.commandStr = commandStr;
		return this;
	}
	
	public Command isPrint(boolean isPrint) {
		this.isPrint = isPrint;
		return this;
	}
	
	/**
	 * 
	 * 描述：执行返回信息：字符串
	 * @author csy
	 * @date 2020 上午12:32:54
	 * @return
	 */
	public String toStringContents() {
		StringBuilder stringBuilder = new StringBuilder(); 
		new RuntimeExecutor() {
			
			@Override
			protected void doWath(String line) {
				print(line);
				stringBuilder.append(line).append("\n");
			}
			
		}.exec();
		
		this.contents = stringBuilder.toString();
		
		return this.contents;
	}
	
	/**
	 * 
	 * 描述：执行返回信息：集合存储
	 * @author csy
	 * @date 2020 上午12:33:18
	 * @return
	 */
	public List<String> toListContets(){
		List<String> contents = new ArrayList<String>();
		new RuntimeExecutor() {
		
			@Override
			protected void doWath(String line) {
				print(line);
				contents.add(line);
			}
			
		}.exec();
		
		this.listContents = contents;
		
		return this.listContents;
	}
	
	abstract class RuntimeExecutor{
		
		public void exec() {
			Runtime runtime = Runtime.getRuntime();
			BufferedReader reader = null;
			try {
				Process process = runtime.exec(commandStr);
				InputStream inputStream = process.getInputStream();
		        reader = new BufferedReader(new InputStreamReader(inputStream));
		        String line;
		        while ((line = reader.readLine()) != null) {
		        	this.doWath(line);
		        }
			} catch (IOException e) {
				throw new RuntimeException("执行命令出错 ====>>>> " , e);
			}finally {
				if (reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}			
		}

		protected abstract void doWath(String line);
	} 

	public String getContents() {
		return contents;
	}

	public List<String> getListContents() {
		return listContents;
	}
	
	/**
	 * 
	 * 描述：控制台打印
	 * @author csy
	 * @date 2020 下午5:26:02
	 * @param content
	 */
	private void print(String content) {
		if (this.isPrint)
			System.out.println(content + "\n");
	}
}
