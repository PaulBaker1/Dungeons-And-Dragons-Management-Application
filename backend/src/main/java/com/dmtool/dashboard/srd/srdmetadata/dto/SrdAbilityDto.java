package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdAbilityDto(
        String index,
        String name,
        String full_name,
        List<String> desc,
        List<ApiReference> skills
) {
}
