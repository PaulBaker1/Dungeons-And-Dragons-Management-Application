package com.dmtool.dashboard.questtracker.mapper;

import com.dmtool.dashboard.questtracker.dto.CreateQuestRequest;
import com.dmtool.dashboard.questtracker.dto.QuestDto;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestStatusRequest;
import com.dmtool.dashboard.questtracker.model.Quest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QuestMapper {

    @Mapping(source = "linkedSession.id", target = "linkedSessionId")
    @Mapping(source = "campaign.id", target = "campaignId")
    QuestDto toDto(Quest quest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "linkedSession", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Quest toEntity(CreateQuestRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStatus(UpdateQuestStatusRequest request, @MappingTarget Quest quest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "linkedSession", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    void updateFromDto(CreateQuestRequest request, @MappingTarget Quest quest);
}
