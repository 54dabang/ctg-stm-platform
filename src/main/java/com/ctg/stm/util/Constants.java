package com.ctg.stm.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leixingbang
 * @version 19-3-7 - 下午6:18
 */
public class Constants {




    public static final int ALL_YES = 1;  //是
    public static final int ALL_NO = 0;  //否

    public static final String CODE_SUC = "0"; //成功
    public static final String CODE_FAIL = "1"; //失败
    public static final String CODE_ERROR = "2";//异常


    //更新mongo后失败返回的版本号
    public static final int VERSION_CONTROL_FAIL = -1;

    //被动更新缓存数据
    public static final int TYPE_REFRESH_CACHELOG_PASSIVE = 1;
    //版本号过时
    public static final int VERSON_TIME_OUT = -2;

    public static final String LOG_KEY_BODY = "body";
    public static final String LOG_KEY_PARAM = "param";

    public static final String LOG_KEY_RESULT = "resuslt";

    public static Map<String, String> codeMap = new HashMap<String, String>();


    public static final String NONE_ROLLOVER_SUFFIX = "nonerollover";

    public static final String ALIAS_ROLL_OVER_FIELD = "createTime";


    static {
        codeMap.put(CODE_SUC, "成功");
        codeMap.put(CODE_FAIL, "失败");
        codeMap.put(CODE_ERROR,"请求异常");
    }

    //自定义特殊符号作为分隔符
    public static final String SCROLL_NETTY_DELIMITER = "△end△";
    //定义文本的最大长度
    public static final int SCROLL_NETTY_MAX_LENGTH = 1024000;
    //服务器端抛出的信息的key值
    public static final String ES_SERVER_ERROR_MSG = "esServerErrorMsg";



    /**
     * 当服务器端抛出异常时，客户端收到的响应结果
     *
     * @param responseData
     * @param e            服务器端抛出的异常信息
     * @return
     */



    /**
     * 状态码map
     *
     * @return map
     */
    public static Map<String, String> getCodeMap() {
        return codeMap;
    }

    /**
     * 获取错误信息
     */
    public static String getErrorCodeMsg(String code) {
        return codeMap.get(code);
    }

    public static String getResponseStr(String code) {
        return getResponseStr(code, null, null, null);
    }

    public static String getResponseStr(String code, String msg) {
        return getResponseStr(code, msg, null, null);
    }

    public static String getResponseStr(String code, String msg, final Object data) {
        return getResponseStr(code, msg, data, null);
    }

    public static String getResponseStr(final String code, final String msg, final Object data, JSONObject binder) {
        Map<String, Object> resultMap = new HashMap<String, Object>() {{
            put("code", code);
            if (msg == null) {
                put("msg", getCodeMap().get(code));
            } else {
                put("msg", msg);
            }
            if (data != null) {
                put("data", data);
            }
        }};
        return JSONObject.toJSONString(resultMap);
    }

    public static ResponseEntity<Map> getErrorResponseEntity(String errorMsg) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", Constants.CODE_FAIL);
        resultMap.put("msg", errorMsg);
        return ResponseEntity.ok().body(resultMap);
    }

    public static ResponseEntity<Map> getSuccessResponseEntity(Object data) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("code", Constants.CODE_SUC);
        result.put("data", data);
        return ResponseEntity.ok().body(result);
    }
}
