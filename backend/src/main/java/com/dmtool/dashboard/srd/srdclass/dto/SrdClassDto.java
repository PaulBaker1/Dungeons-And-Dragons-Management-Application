package com.dmtool.dashboard.srd.srdclass.dto;

import java.util.List;

public record SrdClassDto(
        String index,
        String name,
        String hit_die,
        List<String> proficiencies,
        List<String> saving_throws,
        String subclass_title // e.g., "School of Evocation"
) {
}
