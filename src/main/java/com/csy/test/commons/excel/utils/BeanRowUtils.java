package com.csy.test.commons.excel.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;

import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.annotion.ImportExcelField;
import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelImportInitBaseContextHolder;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.base.defaults.SynchronExcelExportWrite;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.ImportExcelTempBean;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：bean row
 * @author csy
 * @date 2021年11月25日 下午5:56:38
 */
public class BeanRowUtils {
	
	private BeanRowUtils(){}

    /**
     * 限制：一个工作簿最大行数
     */
    public static final int MAX_ROW = (65536 / 2);
	
    /**
     * 描述：beanList写入Workbook
     * @author csy
     * @date 2019年12月19日
     * @param params 参数对象
     */
	public static <T> Workbook beanListToXlsData(Params<T> params){
    	Class<T> clazz = params.getClazz();
        ExportExcelHeader exportExcelHeader = clazz.isAnnotationPresent(ExportExcelHeader.class) ?
        clazz.getAnnotation(ExportExcelHeader.class) : DefaultCommon.DEFAULTE_EXPORTXLSHEADER;
        params.exportExcelHeader(exportExcelHeader);
        
        Workbook workbook = params.getWorkbook();
        //获取序号列样式
        CellStyle cellStyle = CellStyleUtils.createOrderNumberContentCellStyle(workbook , exportExcelHeader);
        params.cellStyle(cellStyle);
        
        DataFormat dataFormat = workbook.createDataFormat();
        params.dataFormat(dataFormat);
        
        /*
         *  切割集合选择：使用subList速度快，但是多线程移除元素有异常待处理，备案方式：使用新集合存储数据
         *  移除集合选取：LinkedList:移除元素比ArrayList快
         *  如果一个sheet时候，允许使用subList分割集合移除元素
         */
        List<T> beanList = params.getBeanList();
        int oneSheetMaxRows = params.getExportConfig().getOneSheetMaxRows();
        int maxRows = (oneSheetMaxRows == 0 ? MAX_ROW : oneSheetMaxRows);
        List<List<T>> list = null;
        if (params.getExportConfig().isAutoRemoveElement() && beanList.size() > maxRows){
        	list = CollectionUtils.cutListByLengthOfNew(beanList, maxRows, LinkedList.class);
        	beanList.clear();
        }else{
        	list = CollectionUtils.cutListByLength(beanList, maxRows);
        }
        
        //设置excel阈值
        if (params.getExportConfig().isAutoInitExcelCapacity()){
        	initAboutExcelCapacity(list.size(), maxRows, 
        			params.getInitBaseContextHolder().getExportExcelTempBeans().size());
        }
        
        //如果集合为0初始化一个工作簿
        int listSize = list.size();
        ExcelExportHeaderDefinedBase headerDefinedBase = ClassUtils.newInstance(params.getHeaderDefinedBaseClazz());
        if (listSize == 0){
        	headerDefinedBase.initHeader(0, params);
        	return workbook;
        }else{
        	long startTime = System.currentTimeMillis();
            boolean autoMultiThreading = params.getExportConfig().isAutoMultiThreading();
        	if (autoMultiThreading){
        		params.setExcelExportWriteBase(new SynchronExcelExportWrite());
        		disposeEverySheetMultiThreading(list, params, headerDefinedBase);
        	}else{
                for (int i = 0; i < listSize ; i ++){
            		Sheet sheet = headerDefinedBase.initHeader(i, params);
                    List<T> subList = list.get(i);
                    foreachBeanList(params.subList(subList).sheet(sheet));
                }
        	}
        	long endTime = System.currentTimeMillis();
            System.out.println("workbook创建sheet、row、cell并且填充值耗时：" + (endTime - startTime)/ 1000 + "s");
        }
        return workbook;
    }
	
	/**
	 * 描述：初始化阈值，对于xls有效
	 * @author csy 
	 * @param sheetSize  sheet个数
	 * @param rowSize    每一个sheet的row行数
	 * @param cellSize   每一个row的列数
	 * @date 2022年4月22日 上午9:22:27
	 */
	private static void initAboutExcelCapacity(int sheetSize, int rowSize, int cellSize){
		//行数，一般需要加上表头1~2行
		System.setProperty("HSSFSheet.RowInitialCapacity", String.valueOf(rowSize + 2));
		//列数，一般需要加上序号（可选）
		System.setProperty("HSSFRow.ColInitialCapacity", String.valueOf(cellSize + 1));
		//sheet个数预留多一个复制
		System.setProperty("HSSFWorkbook.SheetInitialCapacity", String.valueOf(sheetSize + 1));
	}
	
	/**
	 * 描述：多线程处理每一个sheet
	 * @author csy 
	 * @date 2022年4月15日 下午2:38:59
	 * @param list
	 * @param params
	 * @param headerDefinedBase
	 */
	private static <T> void disposeEverySheetMultiThreading(List<List<T>> list, Params<T> params, 
			ExcelExportHeaderDefinedBase headerDefinedBase){
		int listSize = list.size();
		//一个线程负责sheet个数
		int oneThreadDisposeSheetNumber = params.getExportConfig().getOneThreadDisposeSheetNumber();
		//线程池个数，向上取整
		BigDecimal aBigDecimal = new BigDecimal(listSize);
		BigDecimal bBigDecimal = new BigDecimal(oneThreadDisposeSheetNumber);
		int poolSize = (int)Math.ceil(aBigDecimal.divide(bBigDecimal, 2).doubleValue());
		ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
		Map<Integer, Params<T>> sheetParamMap = new HashMap<Integer, Params<T>>(listSize);
		try {
			//初始化sheet
            for (int i = 0; i < listSize ; i ++){
        	    Params<T> newParams = new Params<T>();
        	    Sheet sheet = headerDefinedBase.initHeader(i, params);
        	    List<T> subList = list.get(i);
        	    //这个拷贝对于对象、集合只是值引用
            	BeanUtils.copyProperties(params, newParams);
            	newParams.subList(subList).sheet(sheet).workbook(params.getWorkbook());
            	sheetParamMap.put(i, newParams);
            }
            
            //填充sheet数据
    		List<Future<?>> futures = new ArrayList<Future<?>>(listSize);
            for (int i = 0; i < poolSize ; i++){
            	final int sheetIndex = i;
            	Future<?> future = executorService.submit(() -> {
            		long startTime = System.currentTimeMillis();
            		int sheetIndexNew = sheetIndex * oneThreadDisposeSheetNumber;
            		for (int k = 0; k < oneThreadDisposeSheetNumber; k++){
            			if (sheetIndexNew >= listSize){
            				break;
            			}
            			foreachBeanList(sheetParamMap.get(sheetIndexNew));
            			sheetIndexNew++;
            		}
                    long endTime = System.currentTimeMillis();
                    System.out.println("第" + sheetIndex + "个线程处理时间:" + (endTime - startTime)/ 1000 + "s");
            	});
            	futures.add(future);
            }
            
            //等待所有线程结束，最快速度取决于最慢那个任务
            for (Future<?> future:futures){
            	try {
					future.get();
				} catch (Exception e) {
					throw new RuntimeException("等待处理excel数据失败", e);
				} 
            }
		} finally {
			sheetParamMap.clear();
			executorService.shutdownNow();
		}	
	}

    /**
     * 描述：处理beanList(一个sheet也可以多线程分段处理，待优化)
     * @author csy
     * @date 2020年1月6日
     * @param params 
     */
    private static <T> void foreachBeanList(Params<T> params){
        Sheet sheet = params.getSheet();
        List<T> beanList = params.getSubList();
        boolean needIndex = params.getNeedIndex();
        int startCols = needIndex ? 1 : 0;
        int recordNum = 0;
        if (params.getExportConfig().isAutoRemoveElement()){
            Iterator<T> iterable = beanList.iterator();
            while (iterable.hasNext()){
            	beanToRow(iterable.next(), sheet, params, recordNum, startCols);
                iterable.remove();
                recordNum++;//累加
            }
        }else{
            for (T entity:beanList){
            	beanToRow(entity, sheet, params, recordNum, startCols);
                recordNum++;//累加
            }	
        }
    }
    
    /**
     * 描述：实体类填充到row
     * @author csy 
     * @date 2022年4月15日 下午6:03:40
     * @param entity     数据对象
     * @param sheet      sheet对象
     * @param params      参数对象
     * @param recordNum  记录数
     * @param startCols  开始列
     */
    private static <T> void beanToRow(T entity, Sheet sheet, Params<T> params, int recordNum, int startCols){
    	int startRow = sheet.getPhysicalNumberOfRows();
        Row row = sheet.createRow(startRow);
        ExportExcelHeader exportExcelHeader = params.getExportExcelHeader();
        ExcelExportInitBaseContextHolder initBaseContextHolder = params.getInitBaseContextHolder();
        row.setHeightInPoints(exportExcelHeader.cellHeight());
        boolean needIndex = params.getNeedIndex();
        CellStyle cellStyle = params.getCellStyle();
        if (needIndex){
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(recordNum);
            cell0.setCellStyle(cellStyle);
        }
        beanToRow(entity, row , params, initBaseContextHolder, startCols);   	
    }

    /**
     * 描述：实体类填充到row
     * @author csy
     * @date 2019年12月19日
     * @param entity
     * @param row
     * @param needIndex`
     * @param params
     * @param initBaseContextHolder
     * @param startCols  开始列数
     */
    private static <T> void beanToRow(Object entity, Row row, Params<T> params,
                                      ExcelExportInitBaseContextHolder initBaseContextHolder, int startCols) {
        Field field = null;
        String fieldName = null;
        Class<?> clazz = entity.getClass();
        List<ExportExcelTempBean> exportXlsTempBeans = initBaseContextHolder.getExportExcelTempBeans();
        Sheet sheet = params.getSheet();
        int startRowIndex = sheet.getPhysicalNumberOfRows();
        List<Integer> recordIndexList = new ArrayList<Integer>();
        try {
        	int recordCols = startCols;
            for (ExportExcelTempBean exportExcelTempBean:exportXlsTempBeans){
            	fieldName = exportExcelTempBean.getFieldName();
            	field = ClassUtils.getFieldByFieldName(clazz, fieldName);
            	if (exportExcelTempBean.getExportExcelField().subClazz() == Class.class){
            		params.getExcelExportWriteBase().write(params, initBaseContextHolder, fieldName, row, recordCols, entity, field);
                    //保存列下标
                    recordIndexList.add(recordCols);
                    recordCols++;
            	}else{
            		Object value = ClassUtils.getFieldValue(field, entity);
            		//附加subClazz类型解析
            		analysisSubClassTypeValue(value, row, params, recordCols);
            		//因为子集合用到了多个列，所以加回来
            		int useCols = params.getInitBaseContextHolder()
            				.getSubExcelExportInitBaseContextHolder().getExportExcelTempBeans().size();
            		recordCols += useCols;
            	}
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("BeanRowUtils - > beanToRow IllegalAccess" , e);
        } catch (SecurityException e) {
            throw new RuntimeException("BeanRowUtils - > beanToRow Security" , e);
        }
        
        ifContainSubDataExecuteMerge(sheet, params.getNeedIndex(), recordIndexList, startRowIndex);
    }
    
    /**
     * 描述：因为子集合造成需要列合并
     * @author csy 
     * @date 2021年11月30日 下午6:38:01
     * @param sheet                当前工作簿
     * @param needIndex            是否需要序号列
     * @param recordIndexList      需要合并列的下标
     * @param recordStartRowIndex  开始行
     */
    private static void ifContainSubDataExecuteMerge(Sheet sheet, boolean needIndex, List<Integer> recordIndexList, int recordStartRowIndex){
    	int finallyRowNumber = sheet.getPhysicalNumberOfRows();
        if (finallyRowNumber != recordStartRowIndex){
        	if (needIndex){
        		recordIndexList.add(0);
        	}
        	for (Integer colIndex:recordIndexList){
        		sheet.addMergedRegion(new CellRangeAddress(recordStartRowIndex - 1, finallyRowNumber - 1, colIndex, colIndex));
        	}
        	recordIndexList.clear();
        }
    }
    
    /**
     * 描述：处理subClass类型的对象
     * <br>1、value type collection (大于1之后会继续创建行对象)
     * <br>2、value type entity（其它类型对象暂无解析）（这个类型暂时进不了）
     * 在InitBeanImpl -> done -> listCount.oneAccessAssert(field)时候拒绝了其它类型，只有集合
     * @author csy 
     * @date 2021年11月30日 下午6:39:20
     * @param value      对象值
     * @param row        当前行对象
     * @param params     参数对象
     * @param startCols  开始列数
     */
    private static <T> void analysisSubClassTypeValue(Object value, Row row, Params<T> params, int startCols){

    	if (Objects.isNull(value)){
    		return ;
    	}

    	ExcelExportInitBaseContextHolder initBaseContextHolder = params.getInitBaseContextHolder()
    			.getSubExcelExportInitBaseContextHolder();
    	if (value instanceof Collection){
    		//如果是集合类型
    		Collection<?> collection = (Collection<?>) value;
    		if (collection.isEmpty()){
    			return;
    		}

    		Sheet sheet = params.getSheet();
    		List<Object> beanList = new ArrayList<Object>(collection.size());
    		beanList.addAll(collection);
			for (int i = 0, beanListSize = beanList.size(); i < beanListSize; i++){
                row = (i == 0 ? row : sheet.createRow(sheet.getPhysicalNumberOfRows()));
                row.setHeightInPoints(params.getExportExcelHeader().cellHeight());
                beanToRow(beanList.get(i), row, params, initBaseContextHolder, startCols);
			}
    	}else{
    		//这个暂时不会出现
    		//如果是对象类型	
    		beanToRow(value, row, params, initBaseContextHolder, startCols);
    	}
    }
    
    /**
     * 描述：Workbook转list java集合数据
     * @author csy 
     * @date 2022年4月19日 下午2:49:57
     * @param workbook       工作簿
     * @param startRowIndex  开始行
     * @param startColsIndex 开始列
     * @param clazz          目标java对象类型
     * @param group          分组值
     * @return List<T>
     */
    public static <T> List<T> rowsToBeans(Workbook workbook, int startRowIndex, int startColsIndex, Class<T> clazz, String group){
        Sheet sheet;
        Row row;
        List<T> list = new ArrayList<T>();
        ExcelImportInitBaseContextHolder excelImportInitBase = ExcelImportInitBaseContextHolder.newInstance()
        		.initConvert(clazz)
        		.initTempBeans(clazz);
        for (int i = 0 , sheetsNum = workbook.getNumberOfSheets(); i < sheetsNum ; i++){
        	sheet = workbook.getSheetAt(i);
        	for (int rowIndex = startRowIndex , rowLen = sheet.getLastRowNum() ; rowIndex <= rowLen ; rowIndex++){
                row = sheet.getRow(rowIndex);
                list.add(BeanRowUtils.rowToBean(startColsIndex , row , clazz , excelImportInitBase , group));
            }       	
        }
        return list;
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
    public static <T> T rowToBean(int startColsIndex,Row row , Class<T> clazz , ExcelImportInitBaseContextHolder excelImportInitBase , String group){
    	
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
        try {
            for (int i = 0 , len = importExcelTempBeans.size() ; i < len ; i++) {
                try {
                	field = clazz.getDeclaredField(importExcelTempBeans.get(i).getFieldName());
                	
                	if (group != null && !Arrays.asList(field.getAnnotation(ImportExcelField.class)).contains(group)){
                		continue;
                	}
                	
                    field.setAccessible(true);
                    excelImportInitBase.getConvertMap()
                                                       .get(field.getName()).convert(entity, field, row.getCell(i + startColsIndex));
    			}finally {
                    ClassUtils.accessibleFalse(field);
                }        	
            }			
		} catch (Exception e) {
			throw new RuntimeException("row to entity error " , e);
		}
        return entity;
    }
}
