package com.dmtool.dashboard.dndapi.dto.classDto;

import com.dmtool.dashboard.dndapi.dto.ApiReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClassDTO(
        String index,
        String name,
        @JsonProperty("hit_die")
        int hitDie,
        @JsonProperty("proficiency_choices")
        List<ProficiencyChoice> proficiencyChoices,
        List<ApiReference> proficiencies,
        @JsonProperty("saving_throws")
        List<ApiReference> savingThrows,
        @JsonProperty("starting_equipment")
        List<StartingEquipment> startingEquipment,
        List<ApiReference> subclasses
) {}

