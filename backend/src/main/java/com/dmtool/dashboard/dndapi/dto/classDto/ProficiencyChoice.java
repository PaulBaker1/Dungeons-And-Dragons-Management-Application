package com.dmtool.dashboard.dndapi.dto.classDto;

public record ProficiencyChoice(
        String desc,
        int choose,
        String type,
        FromOptions from
) {}
