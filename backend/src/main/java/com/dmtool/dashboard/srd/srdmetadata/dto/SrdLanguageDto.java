package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdLanguageDto(
        String index,
        String name,
        List<String> desc
) {
}
