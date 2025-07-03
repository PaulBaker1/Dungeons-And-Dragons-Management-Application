package com.dmtool.dashboard.srd.srdmetadata.impl;



import com.dmtool.dashboard.srd.srdmetadata.client.SrdAlignmentClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdAlignmentDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdAlignmentClientImpl implements SrdAlignmentClient {

    private final WebClient webClient;

    public SrdAlignmentClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdAlignmentDto> getAlignmentByIndex(String index) {
        try {
            SrdAlignmentDto alignment = webClient.get()
                    .uri("/alignments/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdAlignmentDto.class)
                    .block();
            return Optional.ofNullable(alignment);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}