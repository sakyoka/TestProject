package com.csy.test.commons.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
	
	private String charset;
	
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
	
	public ZipOperate charset(String charset){
		this.charset = charset;
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
			System.out.println();
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
		
		ZipInputStream zipInputStream = null;
        ZipEntry zipEntry = null;
		try {
			System.out.println("starting to uncompress...");
			System.out.println();

            zipInputStream = new ZipInputStream(new FileInputStream(this.filePath) , 
            		                                                this.charset == null ? Charset.forName("GBK") : Charset.forName(this.charset));
            byte[] buffer = new byte[1024];
            int readLength = 0;
            while ( (zipEntry = zipInputStream.getNextEntry()) != null){
            	if (zipEntry.isDirectory()){
            		String dir = this.outDir + File.separator + zipEntry.getName();
            		Files.createDirectories(Paths.get(dir));
            	}else{
            		File file = new File(this.outDir + File.separator + zipEntry.getName());
            		String path = file.getAbsolutePath();
            		Files.createDirectories(Paths.get(path.substring(0 , path.lastIndexOf(File.separator))));
            		if (this.showPackFile){
            			System.out.println("\t uncompress file ==> " + file.getAbsolutePath());
            			System.out.println();
            		}
            		OutputStream os = null;
            		try {
                		os = new FileOutputStream(file);
                		while( (readLength = zipInputStream.read(buffer, 0 , 1024)) != -1){
                			os.write(buffer, 0 , readLength);
                		}					
					} finally {
						if (os != null){
							os.close();
						}
					}

            	}
            }
		} catch (Exception e) {
			
			throw new RuntimeException("解压失败" , e);
		}finally {
			if (zipInputStream != null){
				try {
					zipInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("uncompress is end.");
		return this;
	}

	/**
	 * 描述：递归压缩文件
	 * @author csy 
	 * @date 2020年7月3日 上午11:26:41
	 * @param zipOutputStream
	 * @param writableByteChannel
	 * @param file
	 * @param path
	 * @throws IOException
	 */
	private void compressZip(ZipOutputStream zipOutputStream , WritableByteChannel writableByteChannel, File file , String path) throws IOException{
		FileChannel fileChannel = null;
		try {
			if (file.isFile()){
				
				if (this.showPackFile){
					System.out.println("\t compress file ==> " + path);
					System.out.println();
				}
				
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
