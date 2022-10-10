package com.csy.test.commons.excel.annotion;

import java.lang.annotation.*;

/**
 * @Description //xls导出。头部处理
 * @Author csy
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportExcelHeader {

    /**
     * @Description //标题名称
     * @Author csy
     * @Date 2019/9/30 13:46
     **/
    String headerName() default "hello title";//需要needHead() 为true

    /**
     * @Description //标题名称字体大小
     * @Author csy
     **/
    short headerNameFontSize() default 18;

    /**
     * @Description //标题行高
     * @Author csy
     **/
    float headerHeight() default (float)40;

    /**
     * 描述：标题字体粗细
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    short headerFontBoldweight() default 0x190;

    /**
     * @Description //工作簿名
     * @Author csy
     * @Date 2019/9/30 13:46
     **/
    String sheetName() default "sheet";

    /**
     * @Description //是否需要排序号
     * @Author csy
     * @Date 2019/9/30 13:48
     **/
    boolean needIndex() default false;

    /**
     * 描述：
     * @author csy
     * @date 2019年11月19日
     * @return String
     */
    String indexName() default "序号";//需要needIndex() 为true才生效

    /**
     * 描述：是否需要headerName
     * @author csy
     * @date 2019年11月18日
     * @return boolean
     */
    boolean needHead() default false;

    /**
     * 描述：对齐方式(默认居中)
     * @author csy
     * @date 2019年11月18日
     * @return short
     */
    short align() default 0x2;
    
    /**
     * 描述：上下对齐方式(默认垂直居中)
     * @author csy 
     * @date 2019年12月5日
     * @return short
     */
    short verticalAlignment() default 0x1;

    /**
     * 描述：单元格大小 , 初始化默认每个单元格大小
     * @author csy
     * @date 2019年11月18日
     * @return short
     */
    short cellWidth() default (short)(35.7 * 100);

    /**
     * 描述：值单元格行高
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    float cellHeight() default (float)15;

    /**
     * 描述：列标题行高
     * @author csy
     * @date 2019年12月3日
     * @return short
     */
    float headerCellHeight() default (float)30;

    /**
     * 描述：字体
     * @author csy
     * @date 2019年11月18日
     * @return String
     */
    String fontName() default "宋体";

    /**
     * @Description //序号 字体格式
     * @Author csy
     * @Date 2019/12/4 17:15
     * @Param []
     * @return java.lang.String
     **/
    String indexFontName() default "宋体";

    /**
     * @Description //序号 字体加粗大小
     * @Author csy
     * @Date 2019/12/4 17:15
     * @Param []
     * @return short
     **/
    short indexFontBoldweight() default 0x2bc;

    /**
     * @Description //序号 size
     * @Author csy
     * @Date 2019/12/4 17:15
     * @Param []
     * @return short
     **/
    short indexFontSize() default (short)12;

    /**
     * @Description //序号 左右对齐方式
     * @Author csy
     * @Date 2019/12/4 17:15
     * @Param []
     * @return short
     **/
    short indexAlign() default (short)0x2;

    /**
     * @Description //序号 上下对齐方式
     * @Author csy
     * @Date 2019/12/4 17:16
     * @Param []
     * @return short
     **/
    short indexVerticalAlignment() default 0x1;
}
