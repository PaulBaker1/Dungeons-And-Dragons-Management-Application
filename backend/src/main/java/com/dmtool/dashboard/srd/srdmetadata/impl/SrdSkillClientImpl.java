package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.SrdSkillClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdSkillDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
public class SrdSkillClientImpl implements SrdSkillClient {

    private final WebClient webClient;

    public SrdSkillClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Optional<SrdSkillDto> getSkillByIndex(String index) {
        try {
            SrdSkillDto skill = webClient.get()
                    .uri("/skills/{index}", index)
                    .retrieve()
                    .bodyToMono(SrdSkillDto.class)
                    .block();
            return Optional.ofNullable(skill);
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }
}
