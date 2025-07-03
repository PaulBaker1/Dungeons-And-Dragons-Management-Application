package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdClassSpellsClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassSpellsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdClassSpellsClientImpl implements SrdClassSpellsClient {

    private final WebClient webClient;

    public SrdClassSpellsClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdClassSpellsDto> getClassSpells(String index, int spellLevel) {
        try {
            SrdClassSpellsDto spells = webClient.get()
                    .uri("/classes/{index}/spells/{spell_level}", index, spellLevel)
                    .retrieve()
                    .bodyToMono(SrdClassSpellsDto.class)
                    .block();
            return Optional.ofNullable(spells);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
