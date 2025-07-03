package com.dmtool.dashboard.prepitem.service;

import com.dmtool.dashboard.prepitem.dto.CreatePrepItemRequest;
import com.dmtool.dashboard.prepitem.dto.PrepItemDto;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepStatusRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepVisibilityRequest;
import java.util.List;

public interface PrepItemService {
    PrepItemDto createPrepItem(CreatePrepItemRequest request);
    PrepItemDto updatePrepStatus(Long id, UpdatePrepStatusRequest request);
    PrepItemDto updatePrepVisibility(Long id, UpdatePrepVisibilityRequest request);
    List<PrepItemDto> getPrepItemsBySession(Long sessionId);
    List<PrepItemDto> getPrepItemsBacklogByCampaign(Long campaignId);
    void deletePrepItem(Long id);
}