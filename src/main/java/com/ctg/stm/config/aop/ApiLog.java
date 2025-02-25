package com.ctg.stm.config.aop;

import java.lang.annotation.*;

/**
 * Created by zhanghongjun on 2019/3/25.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {
}
