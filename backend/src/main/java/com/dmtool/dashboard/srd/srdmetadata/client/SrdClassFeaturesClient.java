package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassFeaturesDto;

import java.util.Optional;

public interface SrdClassFeaturesClient {
    Optional<SrdClassFeaturesDto> getClassFeatures(String index, int classLevel);
}
