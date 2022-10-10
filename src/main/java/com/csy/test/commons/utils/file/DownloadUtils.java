package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 描述：文件下载
 * @author csy
 * @date 2022年1月24日 下午2:14:25
 */
public class DownloadUtils {
	
	private DownloadUtils(){}

	/**
	 * 描述：下载文件
	 * @author csy 
	 * @date 2022年1月24日 下午2:13:28
	 * @param response
	 * @param filePath
	 * @param fileName
	 */
	public static void download(HttpServletResponse response , String filePath, String fileName){
		download(response, new File(filePath), fileName);
	}
	
	/**
	 * 描述：下载文件
	 * @author csy 
	 * @date 2022年1月24日 下午2:13:34
	 * @param response
	 * @param file
	 * @param fileName
	 */
	public static void download(HttpServletResponse response , File file, String fileName){
		try {
			download(response, new FileInputStream(file), fileName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("导出失败" , e);
		}
	}
	
	/**
	 * 描述：下载文件
	 * @author csy 
	 * @date 2022年1月24日 下午2:13:38
	 * @param response
	 * @param inputStream
	 * @param fileName
	 */
    public static void download(HttpServletResponse response , InputStream inputStream, String fileName){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        OutputStream outputStream = null;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
            outputStream = response.getOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(b)) > 0) {
				outputStream.write(b, 0, i);
			}
			outputStream.flush();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("导出失败" , e);
        } catch (IOException e) {
            throw new RuntimeException("导出失败" , e);
        }finally {
            if (outputStream != null){
                try {outputStream.close();} catch (IOException e) {}
            }
            
            if (inputStream != null){
            	try {inputStream.close();} catch (IOException e) {}
            }
        }
    }
}
