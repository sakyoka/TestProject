package com.csy.test.commons.valid.base.defaults;

import com.csy.test.commons.valid.base.AbstractValidBase;

/**
 * 非null
 * @author csy
 * @date 2020年6月12日
 */
public class NullValid extends AbstractValidBase {

    @Override
    public boolean valid(Object value) {
        return null != value;
    }
}
