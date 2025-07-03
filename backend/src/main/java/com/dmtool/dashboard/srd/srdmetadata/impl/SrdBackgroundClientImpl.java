package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdBackgroundClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdBackgroundDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdBackgroundClientImpl implements SrdBackgroundClient {

    private final WebClient webClient;

    public SrdBackgroundClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdBackgroundDto> getBackgroundByIndex(String index) {
        try {
            SrdBackgroundDto background = webClient.get()
                    .uri("/backgrounds/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdBackgroundDto.class)
                    .block();
            return Optional.ofNullable(background);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
