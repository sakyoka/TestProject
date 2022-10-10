package com.csy.test.commons.excel.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelOperateBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.base.defaults.DefaultExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.defaults.ExcelExportHeaderDefinedByTemplateFile;
import com.csy.test.commons.excel.bean.ExportConfig;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import static com.csy.test.commons.utils.Objects.*;

/**
 * excel处理：
 * 1、读取转换成bean集合 @com.csy.test.commons.excel.annotion.ImportExcelField
 * 2、读取数据库导出xls @com.csy.test.commons.excel.annotion.ExportExcelHeader @com.csy.test.commons.excel.annotion.ExportExcelField
 * @author chensy
 * @Description
 * @date: 2019-09-30 11:56
 */
public class ExcelUtils {
	
    private ExcelUtils(){}

    /**
     * 限制：一个工作簿最大行数
     */
    public static final int MAX_ROW = (65536 / 2);

    /**
     * 描述：读取excel row转换成bean List
     * @author csy 
     * @date 2020年7月2日 上午9:22:34
     * @param startRowIndex 开始行
     * @param startColsIndex 开始列
     * @param filePath excel路径
     * @param clazz 目标对象
     * @return List<T>
     */
    public static <T> List<T> xlsDataToBeans(int startRowIndex , int  startColsIndex, String filePath , Class<T> clazz){
        return xlsDataToBeans(startRowIndex , startColsIndex , new File(filePath) , clazz);
    }

    /**
     * 描述：读取excel row转换成bean List
     * @author csy 
     * @date 2020年7月2日 上午9:22:03
     * @param startRowIndex 开始行
     * @param startColsIndex 开始列
     * @param file  excel文件
     * @param clazz 目标对象
     * @return List<T>
     */
    public static <T> List<T> xlsDataToBeans(int startRowIndex ,int startColsIndex , File file , Class<T> clazz){
        Workbook workbook = ExcelOperateBase.getWorkbook(file);
        return xlsDataToBeans(startRowIndex , startColsIndex , workbook , clazz , null);
    }
    
    /**
     * 描述：读取excel row转换成bean List
     * @author csy 
     * @date 2020年10月16日 下午3:23:44
     * @param startRowIndex 开始行
     * @param startColsIndex 开始列
     * @param file excel文件
     * @param clazz  目标对象
     * @param group 分组
     * @return List<T>
     */
    public static <T> List<T> xlsDataToBeans(int startRowIndex ,int startColsIndex , File file , Class<T> clazz , String group){
        Workbook workbook = ExcelOperateBase.getWorkbook(file);
        return xlsDataToBeans(startRowIndex , startColsIndex , workbook , clazz , group);
    }
    
    /**
     * 描述：读取excel row转换成bean List
     * @author csy 
     * @date 2020年10月16日 下午3:23:49
     * @param startRowIndex 开始行 
     * @param startColsIndex 开始列
     * @param workbook excel对象
     * @param clazz 目标对象
     * @return List<T>
     */
    public static <T> List<T> xlsDataToBeans(int startRowIndex ,int startColsIndex , Workbook workbook , Class<T> clazz){
    	return xlsDataToBeans(startRowIndex, startColsIndex, workbook, clazz , null);
    }
    
    /**
     * 描述：读取excel row转换成bean List
     * @author csy 
     * @date 2020年10月13日 下午5:10:44
     * @param startRowIndex 开始行 
     * @param startColsIndex 开始列
     * @param workbook  excel对象
     * @param clazz 目标对象
     * @param group 分组
     * @return List<T>
     */
    public static <T> List<T> xlsDataToBeans(int startRowIndex ,int startColsIndex , Workbook workbook , Class<T> clazz , String group){
    	return BeanRowUtils.rowsToBeans(workbook, startRowIndex, startColsIndex, clazz, group);
    }
    
    /**
     * 描述：根据模板导出数据
     * @author csy 
     * @date 2021年11月23日 上午11:45:28
     * @param templateFilePath 模板路径
     * @param response         
     * @param fileName         导出文件名
     * @param beanList         数据集合
     * @param clazz            数据对象类型
     */
    public static <T> void exportExcel(String templateFilePath, HttpServletResponse response, String fileName, List<T> beanList, Class<T> clazz){
    	exportExcel(new File(templateFilePath), response, fileName, beanList, clazz, null);

    }
    
    /**
     * 描述：根据模板导出数据
     * @author csy 
     * @date 2021年11月23日 上午11:45:32
     * @param templateFile 模板文件对象
     * @param response
     * @param fileName     导出文件名
     * @param beanList     数据集合
     * @param clazz        数据对象类型
     */
    public static <T> void exportExcel(File templateFile, HttpServletResponse response, String fileName, List<T> beanList, Class<T> clazz){
    	exportExcel(templateFile, response, fileName, beanList, clazz, null);
    }  
    
    /**
     * 描述：根据模板导出数据
     * @author csy 
     * @date 2021年11月23日 上午11:45:38
     * @param templateFile  模板文件对象
     * @param response
     * @param fileName      导出文件名
     * @param beanList      数据集合
     * @param clazz         数据对象类型
     * @param group         分组
     */
    public static <T> void exportExcel(File templateFile, HttpServletResponse response, String fileName, List<T> beanList, Class<T> clazz, String group){

        Workbook workbook = beanListToXlsData(templateFile, beanList, clazz , group);

        ExcelOperateBase.export(response, workbook, fileName);
    }
    
    /**
     * 描述：数据填充到模板excel文件
     * @author csy 
     * @date 2021年11月23日 上午11:45:44
     * @param templateFile 模板文件对象
     * @param beanList     数据集合
     * @param clazz        数据对象类型
     * @param group        分组
     * @return Workbook
     */
	public static <T> Workbook beanListToXlsData(File templateFile, List<T> beanList, Class<T> clazz, String group) {
		return beanListToXlsData(templateFile, beanList, clazz, group, new ExportConfig());
	}

    /**
     * 描述：数据填充到模板excel文件
     * @author csy 
     * @date 2021年11月23日 上午11:45:44
     * @param templateFile 模板文件对象
     * @param beanList     数据集合
     * @param clazz        数据对象类型
     * @param group        分组
     * @param exportConfig 导出配置对象
     * @return Workbook
     */
	@SuppressWarnings("unchecked")
	public static <T> Workbook beanListToXlsData(File templateFile, List<T> beanList, Class<T> clazz, String group, ExportConfig exportConfig) {
		
		notNullAssert(beanList , "导出数据源不能为空");
        Workbook workbook = ExcelOperateBase.getWorkbook(templateFile);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.newInstance()
                .initExportBean(clazz, group)//初始化ExportBean
                .initFormat(clazz, group)//初始化Format
                .initStyle(clazz, workbook, group)//初始化Style
                .initSubClazz(clazz, workbook, group);//子集合ExcelExportInitBaseContextHolder

        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
                clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;
        Sheet sheet = workbook.getSheetAt(0);
        int useRow = sheet.getPhysicalNumberOfRows();
        boolean needIndex = exportExcelHeader.needIndex();
        return beanListToXlsData(Params.getBuilder()
								        .beanList(beanList)
								        .initBaseContextHolder(initBaseContextHolder)
								        .needIndex(needIndex)
								        .useRow(useRow)
								        .workbook(workbook)
								        .clazz(clazz)
								        .headerDefinedBaseClazz(ExcelExportHeaderDefinedByTemplateFile.class)
								        .exportConfig(exportConfig));
	}

	/**
     * @Description //导出excel
     * @Author csy
     * @Date 2019/9/30 15:46
     * @Param [filePath,beanList, clazz]
     **/
    public static <T> void exportExcel(String filePath , List<T> beanList , Class<T> clazz){
        exportExcel(filePath, beanList, clazz , null);
    }


    /**
     * 描述：导出excel
     * @author csy
     * @date 2019年12月6日
     * @param filePath
     * @param beanList
     * @param clazz
     * @param group 
     */
    public static <T> void exportExcel(String filePath , List<T> beanList , Class<T> clazz , String group){

        String xlsType = filePath.substring(filePath.lastIndexOf(".") + 1);

        Workbook workbook = beanListToXlsData(beanList , xlsType , clazz , group);

        ExcelOperateBase.exportExcel(workbook , filePath);
    }


    /**
     * 描述：响应到浏览器
     * @author csy
     * @date 2019年11月18日
     * @param response
     * @param fileName
     * @param beanList
     * @param clazz
     */
    public static <T> void exportExcel(HttpServletResponse response , String fileName , List<T> beanList ,
                                       Class<T> clazz){
        exportExcel(response, fileName, beanList, clazz , null);
    }

    /**
     * 描述：响应到浏览器
     * @author csy
     * @date 2019年12月5日
     * @param response
     * @param fileName 文件名称带后缀
     * @param beanList 数据集合
     * @param clazz    目标对象的class
     * @param group    分组值
     */
    public static <T> void exportExcel(HttpServletResponse response , String fileName , List<T> beanList ,
                                       Class<T> clazz , String group){

        String xlsType = fileName.substring(fileName.lastIndexOf(".") + 1);

        Workbook workbook = beanListToXlsData(beanList , xlsType , clazz , group);

        ExcelOperateBase.export(response , workbook , fileName);

    }

    /**
     * 描述：响应到浏览器
     * @author csy
     * @date 2019年12月19日
     * @param response
     * @param fileName
     * @param beanList
     * @param clazz
     * @param headerData 
     */
    public static <T> void exportExcel(HttpServletResponse response , String fileName , List<T> beanList ,
    		ExcelExportHeaderData headerData , Class<T> clazz ){

        String xlsType = fileName.substring(fileName.lastIndexOf(".") + 1);

        Workbook workbook = beanListToXlsData(beanList , xlsType , clazz , headerData);

        ExcelOperateBase.export(response , workbook , fileName);

    }

    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param group
     * @return Workbook
     */
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
                                                 String group){
        return beanListToXlsData(beanList, xlsType, clazz, group, new ExportConfig());
    }
    
    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param group
     * @param exportConfig  导出配置对象
     * @return Workbook
     */
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
                                                 String group, ExportConfig exportConfig){
    	
    	notNullAssert(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.newInstance()
                .initExportBean(clazz, group)//初始化ExportBean
                .initFormat(clazz, group)//初始化Format
                .initStyle(clazz, workbook, group)//初始化Style
                .initSubClazz(clazz, workbook, group);//子集合ExcelExportInitBaseContextHolder

        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
                clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;
                
        boolean needIndex = exportExcelHeader.needIndex();
        int useRow = exportExcelHeader.needHead() ? 2 : 1;
        @SuppressWarnings("unchecked")
		Params<T> params = Params.getBuilder()
									        .beanList(beanList)
									        .initBaseContextHolder(initBaseContextHolder)
									        .needIndex(needIndex)
									        .useRow(useRow)
									        .workbook(workbook)
									        .clazz(clazz)
									        .headerDefinedBaseClazz(DefaultExcelExportHeaderDefinedBase.class)
									        .exportConfig(exportConfig);
									        
        return beanListToXlsData(params);
    }
    
    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param headerData
     * @return Workbook
     */
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
                                                 ExcelExportHeaderData headerData){
        return beanListToXlsData(beanList, xlsType, clazz, headerData, new ExportConfig());
    }

    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param headerData
     * @return Workbook
     */
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
                                                 ExcelExportHeaderData headerData, ExportConfig exportConfig){
    	
    	notNullAssert(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.newInstance()
                .initExportBean(clazz, headerData.getHeaderFields())//初始化exportBean
                .initFormat(clazz, null)//初始化Format
                .defaultFormat(clazz)
                .initStyle(clazz, workbook, null)//初始化Style
                .defaultStyle(clazz , workbook);

        boolean needIndex = headerData.getNeedIndex();
        int useRow = headerData.getTotalRow();
    	
        
        @SuppressWarnings("unchecked")
		Params<T> params = Params.getBuilder()
									        .beanList(beanList)
									        .initBaseContextHolder(initBaseContextHolder)
									        .needIndex(needIndex)
									        .useRow(useRow)
									        .workbook(workbook)
									        .clazz(clazz)
									        .headerData(headerData)
									        .headerDefinedBaseClazz(DefaultExcelExportHeaderDefinedBase.class)
									        .exportConfig(exportConfig);
        return beanListToXlsData(params);
    }

    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param params
     */
	public static <T> Workbook beanListToXlsData(Params<T> params){
		try {
			return BeanRowUtils.beanListToXlsData(params);
		} finally {
			params.getInitBaseContextHolder().release();
		}
    }
}
