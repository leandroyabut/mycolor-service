package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.exception.color.ColorNotFoundException;
import com.arjay07.mycolorservice.model.Color;
import com.arjay07.mycolorservice.repository.ColorRepository;
import com.arjay07.mycolorservice.util.ColorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public Color getColorById(int id) {
        return colorRepository.findById(id).orElseThrow(ColorNotFoundException::new);
    }

    public Page<Color> getColors(int pageNo, int pageSize, String sort, String order, String search) {
        Sort sortBy = Sort.by(sort);
        if (order.toLowerCase().startsWith("asc"))
            sortBy = sortBy.ascending();
        if (order.toLowerCase().startsWith("desc"))
            sortBy = sortBy.descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ColorSpecification specification = new ColorSpecification(search);
        return colorRepository.findAll(specification, pageable);
    }

    public Color saveColor(String name, String hex)

}
