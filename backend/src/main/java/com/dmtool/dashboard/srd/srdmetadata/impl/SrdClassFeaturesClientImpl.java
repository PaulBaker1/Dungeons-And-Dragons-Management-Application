package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdClassFeaturesClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassFeaturesDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdClassFeaturesClientImpl implements SrdClassFeaturesClient {

    private final WebClient webClient;

    public SrdClassFeaturesClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdClassFeaturesDto> getClassFeatures(String index, int classLevel) {
        try {
            SrdClassFeaturesDto features = webClient.get()
                    .uri("/classes/{index}/levels/{class_level}/features", index, classLevel)
                    .retrieve()
                    .bodyToMono(SrdClassFeaturesDto.class)
                    .block();
            return Optional.ofNullable(features);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
