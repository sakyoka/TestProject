package com.csy.test.commons.valid.base;

/**
 * 校验抽象类
 * @author csy
 * @date 2020年6月12日
 */
public abstract class AbstractValidBase {

    /**
     * 描述：校验方法由继承类完成
     * @author csy 
     * @date 2020年6月12日
     * @param value
     * @return boolean
     */
    public abstract boolean valid(Object value);
}
