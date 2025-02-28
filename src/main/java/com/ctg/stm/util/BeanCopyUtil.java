package com.ctg.stm.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

public class BeanCopyUtil {


        public static void copyPropertiesIfNotNull(Object dest, Object orig) {
            try {
                // 获取源对象的所有属性描述符
                java.beans.PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(orig);

                for (java.beans.PropertyDescriptor descriptor : descriptors) {
                    String propertyName = descriptor.getName();
                    // 跳过 "class" 属性（因为它是 Java 内置属性）
                    if ("class".equals(propertyName)) {
                        continue;
                    }

                    // 判断源对象的属性是否为非空
                    Object propertyValue = PropertyUtils.getProperty(orig, propertyName);
                    if (propertyValue != null) {
                        // 如果目标对象存在该属性并且非空，则复制
                        PropertyUtils.setProperty(dest, propertyName, propertyValue);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Error copying properties", e);
            }
        }
}
