package com.arjay07.mycolorservice.util;

import com.arjay07.mycolorservice.model.Color;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ColorSpecification implements Specification<Color> {

    private final String keyword;

    @Override
    public Predicate toPredicate(Root<Color> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (!keyword.isEmpty()) {
            Predicate namePredicate = builder.like(builder.lower(root.get("name")), "%" + keyword + "%");
            Predicate hexPredicate = builder.like(builder.lower(root.get("hex")), "%" + keyword + "%");
            return builder.or(namePredicate, hexPredicate);
        }
        return null;
    }
}
