package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 描述:zip操作对象
 * @author csy
 * @date 2020年7月2日 下午9:24:15
 */
public class ZipOperate {
	
	private String filePath;//压缩/解压  文件路径
	
	private String sourceFilePath;//压缩 源文件路径
	
	private Boolean showPackFile = true;//是否打印信息
	
	private String outDir;//解压 输出路径
	
	private File outFile;//压缩文件对象
	
	private String ompressName;
	
	private ZipOperate() {}
	
	public File getOutFile() {
		return this.outFile;
	}

	/**
	 * 
	 * 描述：获取实例
	 * @author csy
	 * @date 2020年7月2日 下午9:18:31
	 * @return ZipOperate
	 */
	public static ZipOperate getBuilder() {
		return new ZipOperate();
	}
	
	public ZipOperate filePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public ZipOperate sourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
		return this;
	}

	public ZipOperate showPackFile(Boolean showPackFile) {
		this.showPackFile = showPackFile;
		return this;
	}
	
	public ZipOperate outDir(String outDir) {
		this.outDir = outDir;
		return this;
	}
	
	public ZipOperate ompressName(String ompressName) {
		this.ompressName = ompressName;
		return this;
	}

	/**
	 * 描述：压缩文件
	 * @author csy 
	 * @date 2020年7月2日 下午5:21:26
	 * @return ZipOperate
	 */
	public ZipOperate compressZip() {
		
		if (this.sourceFilePath == null) 
			throw new RuntimeException("sourceFilePath is null");
		
		if (this.filePath == null) 
			throw new RuntimeException("filePath is null");
		
		File sourceFile = new File(this.sourceFilePath);
		if (!sourceFile.exists())
			throw new RuntimeException("要压缩文件或目录不存在 , filePath ===>>>> " + sourceFile.getAbsolutePath());
		
		File zipFile = new File(this.filePath);
		ZipOutputStream zipOutputStream = null;
		WritableByteChannel writableByteChannel = null;
		try {
			System.out.println("starting to compress...");
			if (! zipFile.exists())
				zipFile.createNewFile();
			
			zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
			writableByteChannel = Channels.newChannel(zipOutputStream);
			this.compressZip(zipOutputStream , writableByteChannel , sourceFile , this.ompressName != null ? this.ompressName : sourceFile.getName());
		} catch (Exception e) {
			
			zipFile.delete();
			
			throw new RuntimeException("压缩文件失败" , e);
			
		}finally {
			if (zipOutputStream != null){
				try {
					zipOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (writableByteChannel != null){
				try {
					writableByteChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		System.out.println("compress is end.");
		
		this.outFile = zipFile;
		
		return this;
	}
	
	/**
	 * 
	 * 描述：解压文件
	 * @author csy
	 * @date 2020年7月2日 下午10:24:06
	 * @return
	 */
	public ZipOperate uncompressZip() {
		
		if (this.filePath == null)
			throw new RuntimeException("解压文件路径为空");
		
		if (this.outDir == null)
			throw new RuntimeException("解压存放路径为空");
		File outFileDir = new File(this.outDir);
			
		if (!outFileDir.exists()) 
			outFileDir.mkdirs();
		
		Path path = Paths.get(this.filePath);
		final String targetPath = this.outDir;
		FileSystem fileSystem = null;
		try {
			System.out.println("starting to uncompress...");
			fileSystem = FileSystems.newFileSystem(path, null);
			Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<Path>() {
				
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                Path destPath = Paths.get(targetPath , file.toString());
	                Files.deleteIfExists(destPath);
	                Files.createDirectories(destPath.getParent());
	                Files.move(file, destPath);
	                return FileVisitResult.CONTINUE;
	            }
	            
			});
		} catch (IOException e) {
			
			throw new RuntimeException("解压失败" , e);
		}finally {
			if (fileSystem != null) {
				try {
					fileSystem.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("uncompress is end.");
		return this;
	}

	@SuppressWarnings("resource")
	private void compressZip(ZipOutputStream zipOutputStream , WritableByteChannel writableByteChannel, File file , String path) throws IOException{
		FileChannel fileChannel = null;
		try {
			if (file.isFile()){
				
				if (this.showPackFile)
					System.out.println("compress file ==> " + path + File.separator + file.getName());
				
				fileChannel = new FileInputStream(file).getChannel();
				zipOutputStream.putNextEntry(new ZipEntry(path));
	            fileChannel.transferTo(0, file.length(), writableByteChannel);					
			}else{
				File[] files = file.listFiles();
				for (File subfile : files){
					compressZip(zipOutputStream , writableByteChannel , subfile , path + File.separator + subfile.getName());
				}
			}

		}finally {
			
			if (fileChannel != null){
				try {
					fileChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
