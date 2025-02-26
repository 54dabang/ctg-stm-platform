package com.ctg.stm.repository;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import com.ctg.stm.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatisticsBasicUnitRepository extends JpaRepository<ProjectStatisticsBasicUnit,Long> {
}
