package com.dmtool.dashboard.srd.srdclass.service;

import java.util.Optional;

import com.dmtool.dashboard.srd.srdclass.dto.SrdClassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdMulticlassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdSpellcastingDto;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface SrdClassClient {

    // Fetch core class data by index (e.g., "wizard")
    Mono<Optional<SrdClassDto>> getClassByIndex(String index);

    // Fetch spellcasting data for a class
    Mono<Optional<SrdSpellcastingDto>> getSpellcasting(String classIndex);

    // Fetch multiclassing requirements and options for a class
    Mono<Optional<SrdMulticlassDto>> getMulticlassing(String classIndex);
}