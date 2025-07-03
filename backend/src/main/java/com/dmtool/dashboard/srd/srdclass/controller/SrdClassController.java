package com.dmtool.dashboard.srd.srdclass.controller;

import com.dmtool.dashboard.srd.srdclass.dto.SrdClassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdMulticlassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdSpellcastingDto;
import com.dmtool.dashboard.srd.srdclass.service.ClassDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Optional;

@RestController
@RequestMapping("/api/external/srd/classes")
@RequiredArgsConstructor
public class SrdClassController {

    private final ClassDataService classDataService;

    @GetMapping("/{index}")
    public Mono<Optional<SrdClassDto>> getClassData(@PathVariable String index) {
        return classDataService.getClassData(index);
    }

    @GetMapping("/{index}/spellcasting")
    public Mono<Optional<SrdSpellcastingDto>> getSpellcasting(@PathVariable String index) {
        return classDataService.getSpellcastingData(index);
    }

    @GetMapping("/{index}/multiclassing")
    public Mono<Optional<SrdMulticlassDto>> getMulticlassing(@PathVariable String index) {
        return classDataService.getMulticlassingData(index);
    }
}