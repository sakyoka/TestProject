package com.csy.test.commons.excel.annotion;

import java.lang.annotation.*;

import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.defaults.DefaultExcelExportFormat;

/**
 * @Description //处理excel导出
 * @Author csy
 * @Date 2019/9/30 13:40
 * @Param
 * @return
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportExcelField {
	
    /**
     * @Description //定位对应excel列 需要从0开始递增
     * @Author csy
     * @Date 2019/9/30 13:42
     **/
    int order();

    /**
     * @Description //单元格名称
     * @Author csy
     * @Date 2019/9/30 14:13
     **/
    String cellName() default "test cell";

    /**
     * 描述：对齐方式(默认居中)
     * <br>注：这是对于值单元格的设置
     * @author csy
     * @date 2019年11月18日
     * @return short
     */
    short align() default 0x2;


    /**
     * 描述：垂直方向对齐方式
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short verticalAlignment() default 0x1;

    /**
     * 描述：垂直方向对齐方式
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short headerVerticalAlignment() default 0x1;

    /**
     * 描述：对齐方式(默认居中)
     *  <br>注：这是对于表标题单元格的设置
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short hearderAlign() default 0x2;

    /**
     * 描述：单元格宽大小
     * <br>注：这是对于值单元格的设置  , 如果这个值大于headerCellWidth，那么一整列按照这个优先
     * @author csy
     * @date 2019年11月18日
     * @return short
     */
    short cellWidth() default (short)(35.7 * 100);

    /**
     * 描述：单元格宽大小
     * <br>注：这是对于表标题单元格的设置 , 如果这个值小于cellWidth，那么一整列按照cellWidth优先
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short headerCellWidth() default (short)(35.7 * 100);

    /**
     * 描述：字体
     * <br>注：这是对于表标值单元格的设置
     * @author csy
     * @date 2019年11月18日
     * @return String
     */
    String fontName() default "宋体";

    /**
     * 描述：字体
     * <br>注：这是对于表标题单元格的设置
     * @author csy
     * @date 2019年12月3日
     * @return String
     */
    String headerFontName() default "宋体";

    /**
     * 描述：字体大小
     * <br>注：这是对于表标值单元格的设置
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short fontSize() default (short)10;

    /**
     * 描述：字体大小
     * <br>注：这是对于表标题单元格的设置
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short headerFontSize() default (short)12;

    /**
     * 描述：列标题字体粗细
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short headerFontBoldweight() default 0x2bc;

    /**
     * @Description //分组导出
     * <br> 字段根据分组导出
     * @Author csy
     * @Date 2019/11/29 13:56
     * @Param []
     * @return String[]
     **/
    String[] groups() default {};

    /**
     * @Description //字段格式化
     * @Author csy
     * @Date 2019/12/2 11:20
     * @Param []
     * @return java.lang.Class<? extends com.csy.test.commons.excel.base.ExcelExportFormatBase>
     **/
    Class<? extends ExcelExportFormatBase> formatClass() default DefaultExcelExportFormat.class;

    /**
     * 描述：内容自动换行，单元格值
     * @author csy
     * @date 2019年12月4日
     * @return boolean
     */
    boolean warpText() default false;

    /**
     * 描述：内容自动换行，列标题值
     * @author csy
     * @date 2019年12月4日
     * @return boolean
     */
    boolean headerWarpText() default false;
    
    /**
     * 描述：子类对象
     * <br>扩展：
     * <br>1、主对象可以携带仅且一个集合
     * <br>2、子集合里面实体类集合不做处理（只支持一层）
     * @author csy 
     * @date 2021年11月25日 下午5:59:18
     * @return
     */
    Class<?> subClazz() default Class.class;
}
