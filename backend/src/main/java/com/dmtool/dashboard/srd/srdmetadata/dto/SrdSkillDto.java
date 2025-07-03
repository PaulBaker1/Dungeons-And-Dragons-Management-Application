package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdSkillDto(
        String index,
        String name,
        List<String> desc,
        ApiReference ablitiy_score
) {
}
