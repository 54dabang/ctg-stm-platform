package com.ctg.stm.config.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author: leixingbang
 * @create: 2022/05/08 17:30
 * @description:
 */
@Component
public class CommonConfig {
    public static String BUSINESS_SIDE;


    @Value("${application.business.side}")
    public void setBusinessSide(String businessSide) {
        CommonConfig.BUSINESS_SIDE = businessSide;
    }





}
