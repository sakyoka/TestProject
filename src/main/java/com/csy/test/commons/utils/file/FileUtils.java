package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 文件处理
 * @author csy
 * @date 2020年6月12日
 */
public class FileUtils {
	
	private FileUtils(){}
	
	/**
	 * 
	 * 描述：ZI压缩P文件
	 * @author csy
	 * @date 2020年7月2日 下午10:50:14
	 * @param filePath 压缩文件名 ，全路径
	 * @param sourceFilePath 数据源，文件或文件夹
	 * @return
	 */
	public static File compressZip(String filePath , String sourceFilePath) {
		return ZipOperate.getBuilder()
				                    .filePath(filePath)
				                    .sourceFilePath(sourceFilePath)
				                    .compressZip()
				                    .getOutFile();
	}
	
	/**
	 * 
	 * 描述：ZIP解压文件
	 * @author csy
	 * @date 2020年7月2日 下午10:52:33
	 * @param filePath
	 * @param outDir
	 */
	public static void uncompressZip(String filePath , String outDir) {
		ZipOperate.getBuilder()
		                     .filePath(filePath)
		                     .outDir(outDir)
		                     .uncompressZip();
	}

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
		
		final List<String> filePaths = new ArrayList<String>();
		
		try {
			Path path = Paths.get(dirPath);
			Files.walkFileTree(path, new SimpleFileVisitor<Path> (){
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					filePaths.add(file.toAbsolutePath().toString());
					return FileVisitResult.CONTINUE;
				}
				
			});
		} catch (IOException e) {
			throw new RuntimeException("获取文件路径失败" , e);
		}
		
		return filePaths;
	}
}
