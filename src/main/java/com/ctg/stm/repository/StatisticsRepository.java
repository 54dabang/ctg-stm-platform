package com.ctg.stm.repository;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import com.ctg.stm.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Long>, JpaSpecificationExecutor<Statistics> {
}
