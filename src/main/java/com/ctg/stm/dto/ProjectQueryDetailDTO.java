package com.ctg.stm.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectQueryDetailDTO   {
   private MonthlyScientificResearchReportQueryDTO monthlyScientificResearchReportQueryDTO;
   private StatisticsSearchDTO searchDTO;
}
