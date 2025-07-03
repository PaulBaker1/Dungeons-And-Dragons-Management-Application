package com.dmtool.dashboard.dndapi.dto.classDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FromOptions(
        @JsonProperty("option_set_type")
        String optionSetType,
        List<ProficiencyOption> options
) {}
