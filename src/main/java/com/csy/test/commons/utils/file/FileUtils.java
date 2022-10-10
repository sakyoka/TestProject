package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.csy.test.commons.utils.PrintUtils;

/**
 * 文件处理
 * @author csy
 * @date 2020年6月12日
 */
public class FileUtils {
	
	private FileUtils(){}
	
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	
	public static final int DEFAULT_CAPACITY = 1024;
	
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
			throw new RuntimeException("复制文件失败,FileUtils ==> coppyTo" , e);
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
				String outFileDirPath = outFilePath.substring(0, outFilePath.lastIndexOf(File.separator));
				Files.createDirectories(Paths.get(outFileDirPath));
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
		
		Path path = Paths.get(filePath);

		try {
			File file = path.toFile();
			if (!file.exists()){
				file = Files.createFile(path).toFile();
			}
			return writeFile(file, contents);
		} catch (IOException e) {
			throw new RuntimeException("写文件失败，创建文件失败，filePath:" + filePath , e);
		}
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
	public static File writeFile(File file , String contents) {
		return writeFile(file, contents, false);
	}
	
	/**
	 * 描述：复制文件夹、文件到目标目录
	 * @author csy 
	 * @date 2021年1月29日 下午1:59:43
	 * @param inputPath 数据源
	 * @param outPath   目标文件
	 * @param showConsole   是否打印信息
	 */
	public static void copyTo(Path inputPath , Path outPath , Boolean showConsole){
		
		File dirFile = inputPath.toFile();
		
		if (!dirFile.exists()) {
			return;
		}

		try {
			Files.walkFileTree(inputPath, new SimpleFileVisitor<Path> (){
				
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetPath = Paths.get(outPath.toString() + dir.toString().substring(inputPath.toString().length()));
                    if (!Files.exists(targetPath)) {
                    	if (showConsole){
                    		System.out.println();
                    		System.out.println("dir:" + dir + "===>>>" +targetPath);
                    	}
                        Files.createDirectory(targetPath);
                    }
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                Path targetPath = Paths.get(outPath.toString() + file.toString().substring(inputPath.toString().length()));
	                if (showConsole){
		                System.out.println();
		                System.out.println("file:" + file.toFile() + " ===>>> " + targetPath.toFile());	                	
	                }
					coppyTo(file.toFile() , targetPath.toFile());
					return FileVisitResult.CONTINUE;
				}
				
			});
			
		} catch (IOException e) {
			throw new RuntimeException("复制文件夹，文件失败" , e);
		}
	}
	
	/**
	 * 描述：复制文件夹、文件到目标目录
	 * @author csy 
	 * @date 2021年1月29日 下午1:59:43
	 * @param inputPath 数据源
	 * @param outPath   目标文件
	 */
	public static void copyTo(Path inputPath , Path outPath ){
		copyTo(inputPath, outPath, true);
	}

	/**
	 * 描述：内容写进文件
	 * @author csy 
	 * @date 2020年7月4日 上午11:01:22
	 * @param file
	 * @param contents
	 * @param append 是否在原本文件中追加内容
	 * @return File
	 */
	public static File writeFile(File file , String contents , boolean append) {
		FileChannel fileChannel = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file , append);
			fileChannel = fileOutputStream.getChannel();
			ByteBuffer buffer = ByteBuffer.wrap(contents.getBytes());
			fileChannel.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException("写入文件失败 , FileUtils ==> writeFile" , e);
		} finally {
			if (fileChannel != null){
				try {
					fileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {

				}
			}
		}
		return file;
	}
	
	/**
	 * 
	 * 描述：inputStream写入文件
	 * @author csy
	 * @date 2022年3月5日 下午9:39:33
	 * @param filePath 文件绝对路径
	 * @param inputStream 流
	 * @return File
	 */
	public static File writeFile(String filePath, InputStream inputStream) {
		return writeFile(new File(filePath), inputStream);
	}
	
	/**
	 * 
	 * 描述：inputStream写入文件
	 * @author csy
	 * @date 2022年3月5日 下午9:39:54
	 * @param file   文件对象
	 * @param inputStream 流
	 * @return File
	 */
	public static File writeFile(File file, InputStream inputStream) {
		System.out.println("写入文件的绝对路径:" + file.getAbsolutePath());
		try (FileOutputStream fos = new FileOutputStream(file);){
			byte[] bytes = new byte[DEFAULT_CAPACITY];
			int len = 0;
			while ((len = inputStream.read(bytes)) != -1) {
				fos.write(bytes, 0, len);
			}
		} catch (Exception e) {
			throw new RuntimeException("写入文件失败", e);
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
			throw new RuntimeException("获取文件路径失败,FileUtils ==> getDirFilePaths" , e);
		}
		
		return filePaths;
	}
	

	/**
	 * 
	 * 描述：删除文件/文件夹
	 * @author csy
	 * @date 2020年7月13日 下午8:47:46
	 * @param dirPath 要删除文件路径或文件夹路径
	 */
	public static void deletes(String dirPath){
		deletes(dirPath, true);
	}
	
	/**
	 * 
	 * 描述：删除文件/文件夹(排除根目录dirPath)
	 * @author csy
	 * @date 2020年7月13日 下午8:47:46
	 * @param dirPath 要删除文件路径或文件夹路径
	 */
	public static void deletesExceptRoot(String dirPath){
		deletes(dirPath, false);
	}
	
	/**
	 * 
	 * 描述：删除文件/文件夹
	 * @author csy
	 * @date 2020年7月13日 下午8:47:46
	 * @param dirPath 要删除文件路径或文件夹路径
	 * @param containRoot 如果是文件夹是否包含dirPath
	 */
	public static void deletes(String dirPath, boolean containRoot){
		
		Path path = Paths.get(dirPath);
		File dirFile = path.toFile();
		
		if (!dirFile.exists()) {
			return;
		}

		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path> (){
				
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					if (dirPath.equals(dir.toFile().getAbsolutePath())){
						if (containRoot){
							Files.deleteIfExists(dir);
						}
					}else{
						Files.deleteIfExists(dir);
					}
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.deleteIfExists(file);
					return FileVisitResult.CONTINUE;
				}
				
			});
			
		} catch (IOException e) {
			throw new RuntimeException("删除文件或文件夹失败,FileUtils ==> deletes" , e);
		}
	}
	
	/**
	 * 
	 * 描述：读取文件内容
	 * @author csy
	 * @date 2021年1月23日 下午8:36:04
	 * @param filePath 文件路径
	 * @param charsetName 读取编码格式
	 * @return 文件内容
	 */
	public static String read(String filePath , String charsetName) {
		return read(new File(filePath) , charsetName);
	}
	
	/**
	 * 描述：读取文件内容
	 * @author csy 
	 * @date 2021年7月9日 上午9:31:01
	 * @param filePath 文件路径
	 * @return 文件内容
	 */
	public static String read(String filePath) {
		return read(new File(filePath) , DEFAULT_CHARSET_NAME);
	}
	
	/**
	 * 描述：读取文件内容
	 * @author csy 
	 * @date 2021年7月9日 上午9:30:58
	 * @param file 文件对象
	 * @return 文件内容
	 */
    public static String read(File file) {
    	return read(file, DEFAULT_CHARSET_NAME);
    }
	
	/**
	 * 描述：读取文件内容
	 * @author csy
	 * @date 2021年1月23日 下午8:35:40
	 * @param file 文件对象
	 * @param charsetName 读取编码格式
	 * @return 文件内容
	 */
    public static String read(File file , String charsetName) {
        try (FileInputStream fin = new FileInputStream(file);
        	 FileChannel channel = fin.getChannel();){
        	Charset charset = Charset.forName("utf-8");
        	CharsetDecoder decoder = charset.newDecoder();
            ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
            CharBuffer charBuffer = CharBuffer.allocate(DEFAULT_CAPACITY);
            int length = -1;
            StringBuilder stringBuilder = new StringBuilder();
            while ((length = channel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();
                
                charBuffer.clear();
            	byteBuffer.clear();
                byte[] bytes = byteBuffer.array();
                stringBuilder.append(new String(bytes, 0, length , charsetName));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("读取文件内容失败" , e);
 
        } 
    }

    /**
     * 描述：创建文件夹或文件
     * @author csy 
     * @date 2022年1月11日 上午9:35:50
     * @param filePath
     * @throws IOException
     */
	public static void ifNotExistsCreate(String filePath) {
		File file = new File(filePath);
		if (file.exists()){
			return ;
		}

		if (!filePath.contains(".")){
			PrintUtils.println("创建目录:%s", filePath);
			file.mkdirs();
		}else{
			File parentFile = file.getParentFile();
			if (!parentFile.exists()){
				PrintUtils.println("创建目录:%s", parentFile.getAbsolutePath());
				parentFile.mkdirs();
			}
			try {
				PrintUtils.println("创建文件:%s", filePath);
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("创建文件失败，filePath:" + filePath, e);
			}
		}
	}
}
