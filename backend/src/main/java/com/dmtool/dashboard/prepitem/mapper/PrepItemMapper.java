package com.dmtool.dashboard.prepitem.mapper;

import com.dmtool.dashboard.prepitem.dto.CreatePrepItemRequest;
import com.dmtool.dashboard.prepitem.dto.PrepItemDto;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepStatusRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepVisibilityRequest;
import com.dmtool.dashboard.prepitem.model.PrepItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrepItemMapper {

    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "campaign.id", target = "campaignId")
    PrepItemDto toDto(PrepItem prepItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true) // default set in service/prePersist
    @Mapping(target = "session", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PrepItem toEntity(CreatePrepItemRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStatus(UpdatePrepStatusRequest request, @MappingTarget PrepItem prepItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVisibility(UpdatePrepVisibilityRequest request, @MappingTarget PrepItem prepItem);
}
