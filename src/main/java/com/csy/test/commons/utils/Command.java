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
	
	private Process process;
	
	private boolean autoReadStream = true;
	
	private String[] commandArr;
	
	private boolean useStrCommand = true;
	
	public Command commandArr(String ...commandArr) {
		this.commandArr = commandArr;
		this.useStrCommand = false;
		return this;
	}
	
	public Command commandStr(String commandStr) {
		this.commandStr = commandStr;
		this.useStrCommand = true;
		return this;
	}
	
	public Command isPrint(boolean isPrint) {
		this.isPrint = isPrint;
		return this;
	}
	
	public Command autoReadStream(boolean autoReadStream) {
		this.autoReadStream = autoReadStream;
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
		this.process = new RuntimeExecutor() {
			
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
		this.process = new RuntimeExecutor() {
		
			@Override
			protected void doWath(String line) {
				print(line);
				contents.add(line);
			}
			
		}.exec();
		
		this.listContents = contents;
		
		return this.listContents;
	}
	
	/**
	 * 描述：普通执行
	 * @author csy 
	 * @date 2022年1月12日 上午10:07:00
	 */
	public void exec(){
		
		this.process = new RuntimeExecutor() {
			
			private final String preffixName = Command.class.getPackage().getName() + ".Command.exec";
			
			@Override
			protected void doWath(String line) {
				print(preffixName + ":"+ line);
			}
			
		}.exec();		
	}
	
	abstract class RuntimeExecutor{
		
		public Process exec() {
			
			try {
				Process process = (useStrCommand ? Runtime.getRuntime().exec(commandStr) 
						: Runtime.getRuntime().exec(commandArr));
				if (autoReadStream){
			        streamPrint(process.getInputStream());
					streamPrint(process.getErrorStream());				
				}
		        return process;
			} catch (IOException e) {
				throw new RuntimeException("执行命令出错 ====>>>> " , e);
			}		
		}
		
		private void streamPrint(InputStream inputStream) {
			if (inputStream == null){
				return ;
			}
			BufferedReader reader = null;
	        String line;
	        try {
	        	reader = new BufferedReader(new InputStreamReader(inputStream, "gb2312"));
		        while ((line = reader.readLine()) != null) {
		        	this.doWath(line);
		        }	
			} catch (IOException e) {
				
			}finally {
				if (reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
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
	
	public String getCommandStr() {
		return commandStr;
	}

	public Process getProcess() {
		return process;
	}

	public boolean isPrint() {
		return isPrint;
	}

	public boolean isAutoReadStream() {
		return autoReadStream;
	}

	public String[] getCommandArr() {
		return commandArr;
	}

	public boolean isUseStrCommand() {
		return useStrCommand;
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
			System.out.println(content);
	}
}
