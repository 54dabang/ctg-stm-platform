package com.ctg.stm.service;

import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;


@Service
public class StatisticsScheduler {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> selectDepart() {
        String querySql = "SELECT ID, DEPART_NAME FROM SYS_DEPART";
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(querySql)
                .getResultList();
        return resultList;
    }

}
