package com.dmtool.dashboard.srd.srdclass.service;

import com.dmtool.dashboard.srd.srdclass.client.SrdClassClient;
import com.dmtool.dashboard.srd.srdclass.dto.SrdClassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdMulticlassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdSpellcastingDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SrdClassClientImpl implements SrdClassClient {

    private static final Logger log = LoggerFactory.getLogger(SrdClassClientImpl.class);
    private final WebClient webClient;

    @Override
    public Mono<Optional<SrdClassDto>> getClassByIndex(String index) {
        return webClient.get()
                .uri("/classes/{index}", index)
                .retrieve()
                .onStatus(status -> status.value() == 404, clientResponse -> {
                    log.warn("Class data not found for index: {}", index);
                    return Mono.empty();
                })
                .bodyToMono(SrdClassDto.class)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
    }

    @Override
    public Mono<Optional<SrdSpellcastingDto>> getSpellcasting(String classIndex) {
        return webClient.get()
                .uri("/classes/{index}/spellcasting", classIndex)
                .retrieve()
                .onStatus(status -> status.value() == 404, clientResponse -> {
                    log.warn("Spellcasting data not found for class index: {}", classIndex);
                    return Mono.empty();
                })
                .bodyToMono(SrdSpellcastingDto.class)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
    }

    @Override
    public Mono<Optional<SrdMulticlassDto>> getMulticlassing(String classIndex) {
        return webClient.get()
                .uri("/classes/{index}/multiclassing", classIndex)
                .retrieve()
                .onStatus(status -> status.value() == 404, clientResponse -> {
                    log.warn("Multiclassing data not found for class index: {}", classIndex);
                    return Mono.empty();
                })
                .bodyToMono(SrdMulticlassDto.class)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
    }
}
