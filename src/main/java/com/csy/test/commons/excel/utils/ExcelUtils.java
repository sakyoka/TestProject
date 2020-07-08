package com.csy.test.commons.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;

import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelImportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelOperateBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.base.defaults.DefaultExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.ImportExcelTempBean;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import com.csy.test.commons.utils.ClassUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

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
        Sheet sheet;
        Row row;
        List<T> list = new ArrayList<T>();
        Workbook workbook = ExcelOperateBase.getWorkbook(file);
        ExcelImportInitBaseContextHolder excelImportInitBase = ExcelImportInitBaseContextHolder.getInstance()
        		.initConvert(clazz)
        		.initTempBeans(clazz);
        for (int i = 0 , sheetsNum = workbook.getNumberOfSheets(); i < sheetsNum ; i++){
        	sheet = workbook.getSheetAt(i);
        	for (int rowIndex = startRowIndex , rowLen = sheet.getLastRowNum() ; rowIndex <= rowLen ; rowIndex++){
                row = sheet.getRow(rowIndex);
                list.add(rowToBean(startColsIndex , row , clazz , excelImportInitBase ));
            }       	
        }
        return list;
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
    	
        Assert.notNull(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.getInstance()
                .initExportBean(clazz, group)//初始化ExportBean
                .initFormat(clazz, group)//初始化Format
                .initStyle(clazz, workbook, group);//初始化Style


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
									        .headerDefinedBaseClazz(DefaultExcelExportHeaderDefinedBase.class);
									        
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
    	
        Assert.notNull(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.getInstance()
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
									        .headerDefinedBaseClazz(DefaultExcelExportHeaderDefinedBase.class);
        return beanListToXlsData(params);
    }

    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param headerDefinedBase
     * @param headerData
     * @return Workbook
     */
	public static <T> Workbook beanListToXlsData(Params<T> params){
		
    	Workbook workbook = params.getWorkbook();
    	List<T> beanList = params.getBeanList();
    	Class<T> clazz = params.getClazz();
    	ExcelExportHeaderDefinedBase headerDefinedBase = ClassUtils.newInstance(params.getHeaderDefinedBaseClazz());
    	ExcelExportInitBaseContextHolder initBaseContextHolder = params.getInitBaseContextHolder();
    	ExcelExportHeaderData headerData = params.getHeaderData();

        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
        clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;
        //获取序号列样式
        CellStyle cellStyle = CellStyleUtils.createOrderNumberContentCellStyle(workbook , exportExcelHeader);
        List<List<T>> list = CollectionUtils.cutListByLength(beanList, MAX_ROW);
        //如果集合为0初始化一个工作簿
        if (list.size() == 0)
        	headerDefinedBase.initHeader(workbook , clazz , 0 , initBaseContextHolder , headerData);

        if (list.size() > 0){
            List<T> subList = null;
            Sheet sheet = null;
            for (int i = 0 , listSize = list.size() ; i < listSize ; i ++){
                sheet = headerDefinedBase.initHeader(workbook , clazz , i , initBaseContextHolder , headerData);
                subList = list.get(i);
                foreachBeanList(params
			                        .cellStyle(cellStyle)
			                        .subList(subList)
			                        .exportExcelHeader(exportExcelHeader)
			                        .sheet(sheet));
            }
        }

        return workbook;
    }

    /**
     * 描述：处理beanList
     * @author csy
     * @date 2020年1月6日
     * @param param 
     */
    private static <T> void foreachBeanList(Params<T> param){
        Workbook workbook = param.getWorkbook();
        Sheet sheet = param.getSheet();
        List<T> beanList = param.getSubList();
        int useRow = param.getUseRow();
        ExportExcelHeader exportExcelHeader = param.getExportExcelHeader();
        ExcelExportInitBaseContextHolder initBaseContextHolder = param.getInitBaseContextHolder();
        boolean needIndex = param.getNeedIndex();
        CellStyle cellStyle = param.getCellStyle();
        int recordNum = 0;
        for (T entity:beanList){

            Row row = sheet.createRow( recordNum + useRow);
            row.setHeightInPoints(exportExcelHeader.cellHeight());

            if (needIndex){
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(recordNum + 1);
                cell0.setCellStyle(cellStyle);
            }
            beanToRow(entity , row , needIndex , workbook , initBaseContextHolder);

            recordNum++;//累加
        }
    }

    /**
     * 描述：实体类填充到row
     * @author csy
     * @date 2019年12月19日
     * @param entity
     * @param row
     * @param needIndex
     * @param workbook
     * @param initBaseContextHolder
     */
    private static <T> void beanToRow(T entity, Row row, boolean needIndex , Workbook workbook ,
                                      ExcelExportInitBaseContextHolder initBaseContextHolder) {

        Field field = null;
        String fieldName = null;
        Cell cell = null;
        CellStyle cellStyle = null;
        ExcelExportFormatBase excelExportFormatBase = null;
        Class<?> fields = entity.getClass();

        DataFormat dataFormat = workbook.createDataFormat();
        Map<String, ExcelExportFormatBase> temImplMap = initBaseContextHolder.getFormatMap();
        Map<String, CellStyle> temStyleMap = initBaseContextHolder.getStyleMap();
        List<ExportExcelTempBean> exportXlsTempBeans = initBaseContextHolder.getExportExcelTempBeans();
        final int start = needIndex ? 1 : 0;
        try {
            for (int i = 0 , len = exportXlsTempBeans.size() ; i < len ; i++){
                fieldName = exportXlsTempBeans.get(i).getFieldName();
                //创建单元格
                cell = row.createCell(i + start);
                //根据fieldName获取style实例
                cellStyle = temStyleMap.get(fieldName);
                cell.setCellStyle(cellStyle);

                //根据fieldName获取format实例
                excelExportFormatBase = temImplMap.get(fieldName);//5785 5887
                //执行format方法
                field = fields.getDeclaredField(fieldName);
                excelExportFormatBase.format(workbook , cell , dataFormat , entity , field );
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("ExcelUtil - > beanAddtoRow IllegalAccess" , e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("ExcelUtil - > beanAddtoRow NoSuchField" , e);
        } catch (SecurityException e) {
            throw new RuntimeException("ExcelUtil - > beanAddtoRow Security" , e);
        }
    }

    /**
     * 描述：行转对象
     * @author csy 
     * @date 2019/9/30 13:16
     * @param startColsIndex
     * @param row
     * @param clazz
     * @param excelImportInitBase
     * @return T
     */
    private static <T> T rowToBean(int startColsIndex,Row row , Class<T> clazz , ExcelImportInitBaseContextHolder excelImportInitBase){
    	
        T entity = null;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("newInstance：初始化实体类失败" , e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("newInstance：不合法访问" , e);
        }
        
        List<ImportExcelTempBean> importExcelTempBeans = excelImportInitBase.getTempBeans();
        Field field = null;
        for (int i = 0 , len = importExcelTempBeans.size() ; i < len ; i++) {
            try {
            	field = clazz.getDeclaredField(importExcelTempBeans.get(i).getFieldName());
                field.setAccessible(true);
                excelImportInitBase.getConvertMap()
                                                   .get(field.getName()).convert(entity, field, row.getCell(i + startColsIndex));
            } catch (NoSuchFieldException | SecurityException e) {
            	throw new RuntimeException("row to entity error " , e);
			}finally {
                field.setAccessible(false);
            }        	
        }
        return entity;
    }
}
