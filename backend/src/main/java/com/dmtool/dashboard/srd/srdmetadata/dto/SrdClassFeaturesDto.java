package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdClassFeaturesDto(
        Integer count,
        List<SrdFeatureDto> results
) {
}
