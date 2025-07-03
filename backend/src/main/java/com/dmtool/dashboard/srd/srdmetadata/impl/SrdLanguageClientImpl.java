package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdLanguageClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdLanguageDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdLanguageClientImpl implements SrdLanguageClient {

    private final WebClient webClient;

    public SrdLanguageClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdLanguageDto> getLanguageByIndex(String index) {
        try {
            SrdLanguageDto language = webClient.get()
                    .uri("/languages/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdLanguageDto.class)
                    .block();
            return Optional.ofNullable(language);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
