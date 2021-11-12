package com.udacity.jdnd.course3.critter.utility;


//Got from https://stackoverflow.com/questions/1301697/helper-in-order-to-copy-non-null-properties-from-object-to-another


import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {
    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        super.copyProperty(dest, name, value);
    }
}
