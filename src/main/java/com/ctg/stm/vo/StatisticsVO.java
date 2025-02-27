package com.ctg.stm.vo;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.util.ProjectEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
public class StatisticsVO extends Statistics {
    private String bpmStatusStr;

    public StatisticsVO(Statistics statistics) {
        // 将父类字段拷贝到子类
        try {
            BeanUtils.copyProperties(this,statistics);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 设置 bpmStatusStr
        this.bpmStatusStr = ProjectEnum.ProBpmStatus.getByValue(statistics.getBpmStatus()).desc();
    }

}