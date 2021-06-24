package com.arjay07.mycolorservice.util;

import com.arjay07.mycolorservice.model.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ColorSpecification implements Specification<Color> {

    private final String keyword;

    @Override
    public Predicate toPredicate(Root<Color> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate namePredicate = builder.like(root.get("name"), keyword);
        Predicate hexPredicate = builder.like(root.get("hex"), keyword);
        return builder.or(namePredicate, hexPredicate);
    }
}
