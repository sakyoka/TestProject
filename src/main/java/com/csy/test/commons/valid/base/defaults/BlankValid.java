package com.csy.test.commons.valid.base.defaults;

import com.csy.test.commons.valid.base.AbstractValidBase;
import org.apache.commons.lang3.StringUtils;

/**
 * 非空
 * @author csy
 * @date 2020年6月12日
 */
public class BlankValid extends AbstractValidBase {

    @Override
    public boolean valid(Object value) {
        
    	if (value == null){
            return false;
        }
        
        return StringUtils.isNotBlank(value.toString());
    }
}
