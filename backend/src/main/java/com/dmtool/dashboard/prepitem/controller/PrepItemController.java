package com.dmtool.dashboard.prepitem.controller;

import com.dmtool.dashboard.prepitem.dto.CreatePrepItemRequest;
import com.dmtool.dashboard.prepitem.dto.PrepItemDto;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepStatusRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepVisibilityRequest;
import com.dmtool.dashboard.prepitem.service.PrepItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrepItemController {

    private final PrepItemService prepItemService;

    @PostMapping("/prepitems")
    @ResponseStatus(HttpStatus.CREATED)
    public PrepItemDto createPrepItem(@RequestBody CreatePrepItemRequest request) {
        return prepItemService.createPrepItem(request);
    }

    @GetMapping("/sessions/{id}/prepitems")
    public List<PrepItemDto> getPrepItemsBySession(@PathVariable("id") Long sessionId) {
        return prepItemService.getPrepItemsBySession(sessionId);
    }

    @GetMapping("/campaigns/{id}/prepitems/backlog")
    public List<PrepItemDto> getPrepItemsBacklogByCampaign(@PathVariable("id") Long campaignId) {
        return prepItemService.getPrepItemsBacklogByCampaign(campaignId);
    }

    @PatchMapping("/prepitems/{id}/status")
    public PrepItemDto updatePrepStatus(@PathVariable("id") Long id,
                                        @RequestBody UpdatePrepStatusRequest request) {
        return prepItemService.updatePrepStatus(id, request);
    }

    @PatchMapping("/prepitems/{id}/visibility")
    public PrepItemDto updatePrepVisibility(@PathVariable("id") Long id,
                                            @RequestBody UpdatePrepVisibilityRequest request) {
        return prepItemService.updatePrepVisibility(id, request);
    }

    @DeleteMapping("/prepitems/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrepItem(@PathVariable("id") Long id) {
        prepItemService.deletePrepItem(id);
    }
}
