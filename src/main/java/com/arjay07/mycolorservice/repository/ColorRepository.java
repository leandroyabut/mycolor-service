package com.arjay07.mycolorservice.repository;

import com.arjay07.mycolorservice.model.Color;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends PagingAndSortingRepository<Color, Integer>, JpaSpecificationExecutor<Color> {

    Optional<Color> findColorByHex(String hex);

}
