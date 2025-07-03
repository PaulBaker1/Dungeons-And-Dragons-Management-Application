package com.dmtool.dashboard.character.mapper;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.character.dto.PlayerCharacterDto;
import com.dmtool.dashboard.character.dto.PlayerCreateCharacterRequest;
import com.dmtool.dashboard.character.model.PlayerCharacter;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        imports = {LocalDateTime.class})
public interface PlayerCharacterMapper {

    @Mapping(target = "classAndLevel", expression = "java(entity.getCharacterClass() + \" \" + entity.getLevel())")
    @Mapping(target = "controlPlayer", source = "playerControlled")
    @Mapping(target = "sessionCount", expression = "java(entity.getSessions() != null ? entity.getSessions().size() : 0)")
    PlayerCharacterDto toDto(PlayerCharacter entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterClass", expression = "java(extractClass(request.classAndLevel()))")
    @Mapping(target = "level", expression = "java(extractLevel(request.classAndLevel()))")
    @Mapping(target = "playerControlled", source = "controlPlayer")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "sessions", ignore = true)
    PlayerCharacter toEntity(PlayerCreateCharacterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "characterClass", expression = "java(extractClass(request.classAndLevel()))")
    @Mapping(target = "level", expression = "java(extractLevel(request.classAndLevel()))")
    @Mapping(target = "playerControlled", source = "controlPlayer")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "sessions", ignore = true)
    void updateEntity(@MappingTarget PlayerCharacter entity, PlayerCreateCharacterRequest request);

    default String extractClass(String classAndLevel) {
        if (classAndLevel == null) return "";
        String[] parts = classAndLevel.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    default int extractLevel(String classAndLevel) {
        if (classAndLevel == null) return 1;
        String[] parts = classAndLevel.split(" ");
        return parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
    }

    @AfterMapping
    default void setCampaign(@MappingTarget PlayerCharacter entity, PlayerCreateCharacterRequest request) {
        if (request.campaignId() != null) {
            entity.setCampaign(Campaign.builder().id(request.campaignId()).build());
        }
    }
}