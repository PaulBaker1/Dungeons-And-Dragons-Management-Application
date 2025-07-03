package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdProficiencyDto;

import java.util.Optional;

public interface SrdProficiencyClient {
    Optional<SrdProficiencyDto> getProficiencyByIndex(String index);
}
