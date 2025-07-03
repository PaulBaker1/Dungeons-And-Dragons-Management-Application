package com.dmtool.dashboard.encounterbuilder.mapper;

import com.dmtool.dashboard.encounterbuilder.dto.*;
import com.dmtool.dashboard.encounterbuilder.model.Encounter;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface EncounterMapper {

    @Mapping(source = "session.id", target = "sessionId")
    EncounterDto toDto(Encounter encounter);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "session", ignore = true) // Set in the service layer
    @Mapping(target = "totalXP", ignore = true)
    @Mapping(target = "challengeRating", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Encounter toEntity(CreateEncounterRequest request);

}
