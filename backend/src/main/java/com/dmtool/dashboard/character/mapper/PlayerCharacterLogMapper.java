package com.dmtool.dashboard.character.mapper;

import com.dmtool.dashboard.character.dto.CreateCharacterLogRequest;
import com.dmtool.dashboard.character.dto.PlayerCharacterLogDto;
import com.dmtool.dashboard.character.dto.PlayerUpdateCharacterLogRequest;
import com.dmtool.dashboard.character.model.PlayerCharacterLog;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PlayerCharacterLogMapper {

    @Mapping(source = "session.id", target = "sessionId")
    PlayerCharacterLogDto toDto(PlayerCharacterLog playerCharacterLog);

    // MapStruct will use the record's canonical constructor for records
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "session", ignore = true)
    PlayerCharacterLog toEntity(CreateCharacterLogRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void playerUpdatePlayerLogFromDto(PlayerUpdateCharacterLogRequest dto, @MappingTarget PlayerCharacterLog entity);
}