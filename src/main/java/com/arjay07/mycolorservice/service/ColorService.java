package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.dto.PostColorDTO;
import com.arjay07.mycolorservice.exception.color.ColorNameExistsException;
import com.arjay07.mycolorservice.exception.color.ColorNotFoundException;
import com.arjay07.mycolorservice.exception.color.HexExistsException;
import com.arjay07.mycolorservice.model.Color;
import com.arjay07.mycolorservice.repository.ColorRepository;
import com.arjay07.mycolorservice.util.ColorSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;

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
        ColorSpecification specification = new ColorSpecification(search.toLowerCase());
        return colorRepository.findAll(specification, pageable);
    }

    public Color getColorByName(String name) {
        return colorRepository.findColorByNameIgnoreCase(name).orElseThrow(ColorNotFoundException::new);
    }

    public Color getColorByHex(String hex) {
        return colorRepository.findColorByHexIgnoreCase(hex).orElseThrow(ColorNotFoundException::new);
    }

    public Color postColor(PostColorDTO postColorDTO) {
        Color newColor = modelMapper.map(postColorDTO, Color.class);
        if (colorRepository.existsByHexIgnoreCase(postColorDTO.getHex()))
            throw new HexExistsException();
        if (colorRepository.existsByNameIgnoreCase(postColorDTO.getName()))
            throw new ColorNameExistsException();
        return colorRepository.save(newColor);
    }

}
