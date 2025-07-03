package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdProficiencyClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdProficiencyDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdProficiencyClientImpl implements SrdProficiencyClient {

    private final WebClient webClient;

    public SrdProficiencyClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdProficiencyDto> getProficiencyByIndex(String index) {
        try {
            SrdProficiencyDto proficiency = webClient.get()
                    .uri("/proficiencies/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdProficiencyDto.class)
                    .block();
            return Optional.ofNullable(proficiency);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
