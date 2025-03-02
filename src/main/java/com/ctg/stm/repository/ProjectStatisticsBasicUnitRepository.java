package com.ctg.stm.repository;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatisticsBasicUnitRepository extends JpaRepository<ProjectStatisticsBasicUnit,Long>, JpaSpecificationExecutor<ProjectStatisticsBasicUnit> {
}
