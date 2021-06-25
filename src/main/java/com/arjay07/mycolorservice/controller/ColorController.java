package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.dto.PostColorDTO;
import com.arjay07.mycolorservice.model.Color;
import com.arjay07.mycolorservice.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @Value("${server.port}")
    private int port;

    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable int id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(colorService.getColorById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Color> getColorByName(@PathVariable String name) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(colorService.getColorByName(name));
    }

    @GetMapping("/hex/{hex}")
    public ResponseEntity<Color> getColorByHex(@PathVariable String hex) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(colorService.getColorByHex(hex));
    }

    @GetMapping("")
    public ResponseEntity<Page<Color>> getColors(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String order,
            @RequestParam(defaultValue = "") String search
    ) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(colorService.getColors(pageNo, pageSize, sortBy, order, search));
    }

    @PostMapping("")
    public ResponseEntity<Color> postColor(@Valid @RequestBody PostColorDTO postColor) {

        Color posted = colorService.postColor(postColor);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(port)
                .path("/colors/{id}")
                .buildAndExpand(posted.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(posted);
    }

}
