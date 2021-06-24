package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.exception.color.ColorNotFoundException;
import com.arjay07.mycolorservice.model.Color;
import com.arjay07.mycolorservice.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public Color getColorById(int id) {
        return colorRepository.findById(id).orElseThrow(ColorNotFoundException::new);
    }

    public Page<Color> getColors(int pageNo, int pageSize, String sort, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        return colorRepository.findColorByNameContains(search, pageable);
    }

}
