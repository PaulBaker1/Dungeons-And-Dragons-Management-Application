package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdClassLevelClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassLevelDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class SrdClassLevelClientImpl implements SrdClassLevelClient {

    private final WebClient webClient;

    public SrdClassLevelClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<List<SrdClassLevelDto>> getClassLevels(String index, String subclass) {
        try {
            List<SrdClassLevelDto> levels = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/classes/{index}/levels");
                        if (subclass != null && !subclass.isEmpty()) {
                            uriBuilder.queryParam("subclass", subclass);
                        }
                        return uriBuilder.build(index);
                    })
                    .retrieve()
                    .bodyToFlux(SrdClassLevelDto.class)
                    .collectList()
                    .block();
            return Optional.ofNullable(levels);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SrdClassLevelDto> getClassLevel(String index, int classLevel) {
        try {
            SrdClassLevelDto level = webClient.get()
                    .uri("/classes/{index}/levels/{class_level}", index, classLevel)
                    .retrieve()
                    .bodyToMono(SrdClassLevelDto.class)
                    .block();
            return Optional.ofNullable(level);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
