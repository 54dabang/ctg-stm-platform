package com.ctg.stm.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "STATISTICS_REWARDS")
@NoArgsConstructor
@Getter
@Setter
public class Rewards {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "奖励级别")
    @Column(name = "REWARDS_CLASS")
    private String rewardsClass;

}
