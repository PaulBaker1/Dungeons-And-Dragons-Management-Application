package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdAbilityDto;

import java.util.Optional;

public interface SrdAbilityScoreClient {
    Optional<SrdAbilityDto> getAbilityByIndex(String index);
}
