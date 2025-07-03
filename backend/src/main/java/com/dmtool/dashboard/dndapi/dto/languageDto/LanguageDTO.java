package com.dmtool.dashboard.dndapi.dto.languageDto;

import java.util.List;

public record LanguageDTO(
        String name,
        String type,
        List<String> typical_speakers
) {
}
