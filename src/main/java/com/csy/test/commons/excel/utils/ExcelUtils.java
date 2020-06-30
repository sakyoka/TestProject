package com.csy.test.commons.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;

import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.annotion.ImportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelOperateBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.base.defaults.DefaultExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;

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
     * @Description //读取xls转换成bean List
     * @Author csy
     * @Date 2019/9/30 12:00
     * @Param [startRowIndex,filePath, clazz]
     * @return java.util.List<T>
     **/
    public static <T> List<T> xlsDataToBeans(int startRowIndex , String filePath , Class<T> clazz){
        return xlsDataToBeans(startRowIndex , new File(filePath) , clazz);
    }

    /**
     * @Description //读取xls转换成bean List
     * @Author csy
     * @Date 2019/9/30 12:00
     * @Param [startRowIndex,filePath, clazz]
     * @return java.util.List<T>
     **/
    public static <T> List<T> xlsDataToBeans(int startRowIndex , File file , Class<T> clazz){
        Workbook workbook = ExcelOperateBase.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Row row;
        List<T> list = new ArrayList<T>();
        for (int rowIndex = startRowIndex , rowLen = sheet.getLastRowNum() ; rowIndex < rowLen ; rowIndex++){
            row = sheet.getRow(rowIndex);
            list.add(rowToBean(row , clazz));
        }
        return list;
    }

    /**
     * @Description //导出excel
     * @Author csy
     * @Date 2019/9/30 15:46
     * @Param [filePath,beanList, clazz]
     * @return void
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
     * @param group void
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
     * @return void
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
     * @return void
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
     * @param headerData void
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
        return beanListToXlsData(beanList, xlsType, clazz, group , new DefaultExcelExportHeaderDefinedBase());
    }

    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param beanList
     * @param xlsType
     * @param clazz
     * @param group
     * @param headerDefinedBase
     * @return Workbook
     */
    @SuppressWarnings("unchecked")
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
                                                 String group , ExcelExportHeaderDefinedBase headerDefinedBase){

        Assert.notNull(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.getInstance()
                .initExportBean(clazz, group)//初始化ExportBean
                .initFormat(clazz, group)//初始化Format
                .initStyle(clazz, workbook, group);//初始化Style

        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
                clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;

        //获取序号列样式
        CellStyle cellStyle = getIndexStyle(workbook , exportExcelHeader);

        boolean needIndex = exportExcelHeader.needIndex();
        int useRow = exportExcelHeader.needHead() ? 2 : 1;

        //MAX_ROW
        List<List<T>> list = CollectionUtils.cutListByLength(beanList, MAX_ROW);
        //如果集合为0初始化一个工作簿
        if (list.size() == 0)
            getSheetByClass(workbook , clazz , 0 , initBaseContextHolder , headerDefinedBase);

        if (list.size() > 0){
            List<T> subList = null;
            Sheet sheet = null;
            for (int i = 0 , listSize = list.size() ; i < listSize ; i ++){
                sheet = getSheetByClass(workbook , clazz , i , initBaseContextHolder , headerDefinedBase);
                subList = list.get(i);
                foreachBeanList(Params.getBuilder()
                        .cellStyle(cellStyle)
                        .beanList(subList)
                        .exportExcelHeader(exportExcelHeader)
                        .initBaseContextHolder(initBaseContextHolder)
                        .needIndex(needIndex)
                        .sheet(sheet)
                        .useRow(useRow)
                        .workbook(workbook));
            }
        }
        return workbook;
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
        return beanListToXlsData(beanList , xlsType , clazz , new DefaultExcelExportHeaderDefinedBase() , headerData);
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
    @SuppressWarnings("unchecked")
    public static <T> Workbook beanListToXlsData(List<T> beanList , String xlsType , Class<T> clazz ,
    		ExcelExportHeaderDefinedBase headerDefinedBase , ExcelExportHeaderData headerData){

        Assert.notNull(beanList , "导出数据源不能为空");

        Workbook workbook = ExcelOperateBase.getWorkbookByXlsType(xlsType);

        ExcelExportInitBaseContextHolder initBaseContextHolder = ExcelExportInitBaseContextHolder.getInstance()
                .initExportBean(clazz, headerData.getHeaderFields())//初始化exportBean
                .initFormat(clazz, null)//初始化Format
                .defaultFormat(clazz)
                .initStyle(clazz, workbook, null)//初始化Style
                .defaultStyle(clazz , workbook);

        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
                clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;

        //获取序号列样式
        CellStyle cellStyle = getIndexStyle(workbook , exportExcelHeader);

        boolean needIndex = headerData.getNeedIndex();
        int useRow = headerData.getTotalRow();
        List<List<T>> list = CollectionUtils.cutListByLength(beanList, MAX_ROW);
        //如果集合为0初始化一个工作簿
        if (list.size() == 0)
        	getSheetByHeaderData(workbook , clazz , 0 , initBaseContextHolder , headerDefinedBase , headerData);

        if (list.size() > 0){
            List<T> subList = null;
            Sheet sheet = null;
            for (int i = 0 , listSize = list.size() ; i < listSize ; i ++){
                sheet = getSheetByHeaderData(workbook , clazz , i , initBaseContextHolder , headerDefinedBase , headerData);
                subList = list.get(i);
                foreachBeanList(Params.getBuilder()
                        .cellStyle(cellStyle)
                        .beanList(subList)
                        .exportExcelHeader(exportExcelHeader)
                        .initBaseContextHolder(initBaseContextHolder)
                        .needIndex(needIndex)
                        .sheet(sheet)
                        .useRow(useRow)
                        .workbook(workbook));
            }
        }

        return workbook;
    }

    /**
     * 描述：处理beanList
     * @author csy
     * @date 2020年1月6日
     * @param param void
     */
    private static <T> void foreachBeanList(Params<T> param){
        Workbook workbook = param.getWorkbook();
        Sheet sheet = param.getSheet();
        List<T> beanList = param.getBeanList();
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
     * @param initBaseContextHolder void
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
     * @Description对象 //行转
     * @Author csy
     * @Date 2019/9/30 13:16
     * @Param [row, clazz]
     * @return T
     **/
    private static <T> T rowToBean(Row row , Class<T> clazz){

        Field[] fields = clazz.getDeclaredFields();
        Class<ImportExcelField> importExcelFieldClazz = ImportExcelField.class;

        T entity = null;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("newInstance：初始化实体类失败" , e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("newInstance：不合法访问" , e);
        }

        for (Field field:fields){

            if (!field.isAnnotationPresent(importExcelFieldClazz))
                continue;

            ImportExcelField importXls = field.getAnnotation(importExcelFieldClazz);
            int order = importXls.order();
            try {
                field.setAccessible(true);
                field.set(entity , ExcelOperateBase.cellToData(row.getCell(order)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("设置bean值失败" , e);
            }finally {
                field.setAccessible(false);
            }
        }
        return entity;
    }

    /**
     * 描述：获取工作簿
     * @author csy
     * @date 2019年12月19日
     * @param workbook
     * @param clazz
     * @param sheetIndex
     * @param initBaseContextHolder
     * @param headerDefinedBase
     * @param headerData
     * @return Sheet
     */
    private static <T> Sheet getSheetByHeaderData(Workbook workbook , Class<T> clazz , int sheetIndex ,
                                                  ExcelExportInitBaseContextHolder initBaseContextHolder , ExcelExportHeaderDefinedBase headerDefinedBase, ExcelExportHeaderData headerData){
        return headerDefinedBase.initHeaderByHeaderData(workbook , clazz , sheetIndex , initBaseContextHolder , headerData);
    }

    /**
     * 描述：获取工作簿
     * @author csy
     * @date 2019年12月19日
     * @param workbook
     * @param clazz
     * @param sheetIndex
     * @param initBaseContextHolder
     * @param headerDefinedBase
     * @return Sheet
     */
    private static <T> Sheet getSheetByClass(Workbook workbook , Class<T> clazz , int sheetIndex ,
                                             ExcelExportInitBaseContextHolder initBaseContextHolder , ExcelExportHeaderDefinedBase headerDefinedBase) {
        return headerDefinedBase.initHeaderByClass(workbook, clazz, sheetIndex, initBaseContextHolder);
    }

    /**
     * 描述：获取序号列样式
     * @author csy
     * @date 2019年12月19日
     * @param workbook
     * @param exportXlsHeader
     * @return CellStyle
     */
    private static CellStyle getIndexStyle(Workbook workbook , ExportExcelHeader exportExcelHeader){
    	return CellStyleUtils.createOrderNumberContentCellStyle(workbook, exportExcelHeader);
    }
}
