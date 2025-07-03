package com.dmtool.dashboard.dndapi.dto.classDto;

import com.dmtool.dashboard.dndapi.dto.ApiReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProficiencyOption(
        @JsonProperty("option_type")
        String optionType,
        ApiReference item
) {}