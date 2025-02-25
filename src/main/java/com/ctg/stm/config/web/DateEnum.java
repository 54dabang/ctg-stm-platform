package com.ctg.stm.config.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DateEnum {

    /**
     * 时间格式
     */
    COMMON_DATE_TIME(0, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())),
    COMMON_SHORT_DATE(1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault())),
    COMMON_MONTH_DAY(2, DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())),
    COMMON_MONTH(3, DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault())),
    YEAR(4, DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault())),
    COLON_DELIMITED_TIME(5, DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault())),
    COLON_DELIMITED_SHORT_TIME(6, DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())),
    CHINESE_DATE_TIME(7, DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒").withZone(ZoneId.systemDefault())),
    CHINESE_SHORT_DATE(8, DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分").withZone(ZoneId.systemDefault())),
    CHINESE_DATE(9, DateTimeFormatter.ofPattern("yyyy年MM月dd日").withZone(ZoneId.systemDefault())),
    CHINESE_MONTH(10, DateTimeFormatter.ofPattern("yyyy年MM月").withZone(ZoneId.systemDefault())),
    CHINESE_YEAR(11, DateTimeFormatter.ofPattern("yyyy年").withZone(ZoneId.systemDefault())),
    CHINESE_TIME(12, DateTimeFormatter.ofPattern("HH时mm分ss秒").withZone(ZoneId.systemDefault())),
    CHINESE_SHORT_TIME(13, DateTimeFormatter.ofPattern("HH时mm分").withZone(ZoneId.systemDefault()));

    private final Integer code;
    private final DateTimeFormatter dateTimeFormatter;


    public static DateEnum findByCode(int code){
        return Stream.of(values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

}