package com.csy.test.commons.excel.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelOperateBase {
	
	private ExcelOperateBase(){}
	
    /**
     * @Description //获取Workbook
     * @Author csy
     * @Date 2019/9/30 12:00
     * @Param [filePath]
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    public static Workbook getWorkbook(String filePath){
        return getWorkbook(new File(filePath));
    }
    
    /**
     * 描述：获取Workbook
     * @author csy 
     * @date 2019年12月6日
     * @param xlsType
     * @return Workbook
     */
    public static Workbook getWorkbookByXlsType(String xlsType){
        Workbook workbook = null;
        if (xlsType.equalsIgnoreCase("xls")){
            workbook = new HSSFWorkbook();
        }else if(xlsType.equalsIgnoreCase("xlsx")){
            workbook = new XSSFWorkbook();
        }else{
            throw new RuntimeException("不符合excel文件类型fileType:" + xlsType);
        }
        return workbook;
    }

    /**
     * @Description //获取Workbook
     * @Author csy
     * @Date 2019/9/30 12:00
     * @Param [file]
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    public static Workbook getWorkbook(File file){
    	try {
			return getWorkbook(new FileInputStream(file), file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件不存在" , e);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
    }
    
    /**
     * 描述：对getWorkbook再封装一层
     * @author csy 
     * @date 2020年10月13日 上午9:55:34
     * @param is 文件流
     * @param filePath 文件名称路径
     * @return Workbook
     */
    public static Workbook getWorkbook(InputStream is , String filePath){
        Workbook workbook;
        try {

            String fileType = filePath.substring( filePath.lastIndexOf(".") + 1 );

            if (fileType.equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(is);
            }else if(fileType.equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(is);
            }else{
                throw new RuntimeException("不符合excel文件类型fileType:" + fileType);
            }

            return workbook;
        } catch (Exception e) {
            throw new RuntimeException("获取Workbook失败"  , e);
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * @Description //导出excel
     * @author csy 
     * @date 2019年12月5日
     * @param filePath
     * @param beanList
     * @param clazz
     * @param group
     * @return void
     */
    public static <T> void exportExcel(Workbook workbook , String filePath){

        if (StringUtils.isBlank(filePath))
            throw new RuntimeException("文件名不能为空");

        File file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建xls文件失败");
            }
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("导出失败" , e);
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 描述：把流响应到浏览器
     * @author csy
     * @date 2019年11月20日
     * @param response
     * @param workbook
     * @param fileName void
     */
    public static void export(HttpServletResponse response , Workbook workbook , String fileName){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");

        OutputStream outputStream = null;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("导出失败" , e);
        } catch (IOException e) {
            throw new RuntimeException("导出失败" , e);
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * @Description //cell数据类型转换
     * @Author csy
     * @Date 2019/9/30 13:37
     * @Param [cell]
     * @return java.lang.Object
     **/
    public static Object cellToData(Cell cell){
        int cellType = cell.getCellType();
        Object value = null;
        switch (cellType){
            case 0: value = cell.getNumericCellValue(); break;//数值型
            case 1: value = cell.getStringCellValue();break;//字符串型
            case 2: value = null;break;//公式型 , 暂时不处理
            case 3: value = null;break;//空值
            case 4: value = cell.getBooleanCellValue(); break;//布尔型
            case 5: throw new RuntimeException("xls单元格值错误获取");//错误
            default:value = null;
        }
        return value;
    }
    
    /**
     * 描述：创建sheet
     * @author csy 
     * @date 2019年12月6日
     * @param workbook
     * @param sheetIndex
     * @param sheetName
     * @return Sheet
     */
    public static Sheet createSheet(Workbook workbook , int sheetIndex , String sheetName){
        //创建工作簿，并命名
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetIndex, sheetIndex > 0 ? (sheetName + sheetIndex) : sheetName);
        return sheet;
    }
}
