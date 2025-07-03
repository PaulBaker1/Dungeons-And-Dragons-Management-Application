package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdLanguageDto;

import java.util.Optional;

public interface SrdLanguageClient {
    Optional<SrdLanguageDto> getLanguageByIndex(String index);
}
