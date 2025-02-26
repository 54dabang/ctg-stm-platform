package com.ctg.stm.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface PredicateCallBack<T> {
    List<Predicate> toPredicates(Root<T> rt, CriteriaQuery<?> query, CriteriaBuilder cb);
}
