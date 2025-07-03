package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdAbilityScoreClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdAbilityDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdAbilityScoreClientImpl implements SrdAbilityScoreClient {

    private final WebClient webClient;

    public SrdAbilityScoreClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdAbilityDto> getAbilityByIndex(String index) {
        try {
            SrdAbilityDto ability = webClient.get()
                    .uri("/ability-scores/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdAbilityDto.class)
                    .block();
            return Optional.ofNullable(ability);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
