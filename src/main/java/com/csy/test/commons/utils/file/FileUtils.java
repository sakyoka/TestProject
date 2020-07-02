package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理
 * @author csy
 * @date 2020年6月12日
 */
public class FileUtils {
	
	private FileUtils(){}

	/**
	 * 描述：复制文件
	 * @author csy 
	 * @date 2020年6月12日
	 * @param inputFile
	 * @param outFile
	 * @return File
	 */
	@SuppressWarnings("resource")
	public static File coppyTo(File inputFile , File outFile){
		FileChannel inputFileChannel = null;
		FileChannel outFileChannel = null;

		try {
			inputFileChannel = new FileInputStream(inputFile).getChannel();
			outFileChannel = new FileOutputStream(outFile).getChannel();
			outFileChannel.transferFrom(inputFileChannel , 0 , inputFileChannel.size());
		} catch (Exception e) {
			throw new RuntimeException("复制文件失败!!" , e);
		}finally {
			if (inputFileChannel != null){
				try {
					inputFileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outFileChannel != null){
				try {
					outFileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return outFile;
	}
	
	/**
	 * 描述：复制文件
	 * @author csy 
	 * @date 2020年6月12日
	 * @param inputFile
	 * @param outFilePath
	 * @return
	 * @throws IOException File
	 */
	public static File coppyTo(File inputFile , String outFilePath) throws IOException{
		File outFile = new File(outFilePath);
		if (outFile.isDirectory()){
			String inputFileName = inputFile.getName();
			outFile = new File(outFilePath + File.separator + inputFileName);
		}else{
			if (outFilePath.contains(".")) {
				File outFileDir = new File( outFilePath.substring(0, outFilePath.lastIndexOf('/') + 1) );
				if (! outFileDir.exists()) {
					outFileDir.mkdirs();
				}
			}
			outFile.createNewFile();
		}
		return coppyTo(inputFile, outFile);
	}
	
	/**
	 * 
	 * 描述：内容写进文件
	 * @author csy
	 * @date 2020 下午3:22:07
	 * @param filePath 目标文件路径
	 * @param contents 内容
	 * @return File
	 */
	public static File writeFile(String filePath , String contents) {
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("创建文件失败!" , e);
		}
		
		return writeFile(file, contents);
	}
	
	/**
	 * 
	 * 描述：内容写进文件
	 * @author csy
	 * @date 2020 下午3:18:06
	 * @param file 目标文件
	 * @param contents 内容
	 * @return  File
	 */
	@SuppressWarnings("resource")
	public static File writeFile(File file , String contents) {
		FileChannel fileChannel = null;
		try {
			fileChannel = new FileOutputStream(file).getChannel();
			ByteBuffer buffer = ByteBuffer.wrap(contents.getBytes());
			fileChannel.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException("写入文件失败!" , e);
		} finally {
			if (fileChannel != null){
				try {
					fileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	/**
	 * 
	 * 描述：获取文件夹下面的所有文件路径
	 * @author csy
	 * @date 2020年6月21日 下午6:45:33
	 * @param dirPath 文件夹路径
	 * @return 文件路径集合
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getDirFilePaths(String dirPath){
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			return Collections.EMPTY_LIST;
		}
		
		List<String> filePaths = new ArrayList<String>();
		getDirFilePaths(dirFile , filePaths);
		
		return filePaths;
	}
	
	/**
	 * 
	 * 描述：获取文件夹下面的所有文件路径
	 * @author csy
	 * @date 2020年6月21日 下午6:46:32
	 * @param dirFile 文件夹路径
	 * @param filePaths 存储文件夹路径
	 */
	private static void getDirFilePaths(File dirFile , List<String> filePaths) {
		File[] files = dirFile.listFiles();
		for (File file:files) {
			if (file.isDirectory()) {
				getDirFilePaths(file , filePaths);
			}else {
				filePaths.add(file.getAbsolutePath());
			}
		}		
	}
	
//	/**
//	 * 描述：压缩文件
//	 * @author csy 
//	 * @date 2020年7月2日 下午5:21:26
//	 * @param outFilePath 全路径 压缩文件名，需要带后缀
//	 * @param filePath 文件/目录
//	 * @return 压缩文件
//	 */
//	public static File zipFile(String outFilePath, String filePath){
//		return zipFile( outFilePath , new File(filePath));
//	}
//
//	/**
//	 * 描述：压缩文件
//	 * @author csy 
//	 * @date 2020年7月2日 下午5:21:26
//	 * @param outFilePath 全路径 压缩文件名，需要带后缀
//	 * @param file 文件/目录
//	 * @return 压缩文件
//	 */
//	private static File zipFile(String outFilePath , File file) {
//		
//		if (!file.exists())
//			throw new RuntimeException("要压缩文件或目录不存在 , filePath ===>>>> " + file.getAbsolutePath());
//		
//		File zipFile = new File(outFilePath);
//		ZipOutputStream zipOutputStream = null;
//		WritableByteChannel writableByteChannel = null;
//		try {
//			
//			if (! zipFile.exists()){
//				zipFile.createNewFile();
//			}
//			zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
//			writableByteChannel = Channels.newChannel(zipOutputStream);
//			compressZip(zipOutputStream , writableByteChannel , file , file.getName());
//		} catch (Exception e) {
//			
//			zipFile.delete();
//			
//			throw new RuntimeException("压缩文件失败" , e);
//			
//		}finally {
//			if (zipOutputStream != null){
//				try {
//					zipOutputStream.close();
//					zipOutputStream.closeEntry();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if (writableByteChannel != null){
//				try {
//					writableByteChannel.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//		return zipFile;
//	}
//	
//	private static void compressZip(ZipOutputStream zipOutputStream , WritableByteChannel writableByteChannel, File file , String base){
//		FileChannel fileChannel = null;
//		try {
//			if (file.isFile()){
//				fileChannel = new FileInputStream(file).getChannel();
//				zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
//	            fileChannel.transferTo(0, file.length(), writableByteChannel);					
//			}else{
//				File[] files = file.listFiles();
//				for (File subfile : files){
//					compressZip(zipOutputStream , writableByteChannel , subfile , base + File.separator + subfile.getName());
//				}
//			}
//		} catch (Exception e) {
//	
//			throw new RuntimeException("压缩文件失败" , e);
//		}finally {
//
//			if (writableByteChannel != null){
//				try {
//					writableByteChannel.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if (fileChannel != null){
//				try {
//					fileChannel.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}	
//	}
	
//	public static void main(String[] args) {
//		zipFile("D:\\compile_cache\\test.zip", "D:\\compile_cache\\补丁\\");
//	}
}
