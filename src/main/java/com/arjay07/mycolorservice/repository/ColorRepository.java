package com.arjay07.mycolorservice.repository;

import com.arjay07.mycolorservice.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends PagingAndSortingRepository<Color, Integer> {

    Optional<Color> findColorByHex(String hex);

    Page<Color> findColorByNameContains(String substr, Pageable pageable);

}
