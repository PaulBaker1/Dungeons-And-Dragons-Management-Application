package com.dmtool.dashboard.questtracker.controller;

import com.dmtool.dashboard.questtracker.dto.*;
import com.dmtool.dashboard.questtracker.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    // Create quest within a specific campaign
    @PostMapping("/campaigns/{id}/quests")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDto createQuest(@PathVariable Long id, @RequestBody CreateQuestRequest request) {
        return questService.createQuest(id, request);
    }

    // Get all quests for a campaign
    @GetMapping("/campaigns/{campaignId}/quests")
    public List<QuestDto> getQuestsByCampaign(@PathVariable Long campaignId) {
        return questService.getQuestsByCampaign(campaignId);
    }

    // Get all quests for a session
    @GetMapping("/sessions/{sessionId}/quests")
    public List<QuestDto> getQuestsBySession(@PathVariable Long sessionId) {
        return questService.getQuestsBySession(sessionId);
    }

    // Get a quest by ID for a specific campaign
    @GetMapping("/campaigns/{campaignId}/quests/{questId}")
    public QuestDto getQuestById(@PathVariable Long campaignId, @PathVariable Long questId) {
        return questService.getQuestById(campaignId, questId);
    }

    // Full quest update within a specific campaign
    @PutMapping("/campaigns/{campaignId}/quests/{questId}")
    public QuestDto updateQuest(@PathVariable Long campaignId, @PathVariable Long questId,
                                @RequestBody CreateQuestRequest request) {
        return questService.updateQuest(campaignId, questId, request);
    }

    // Delete a quest within a specific campaign
    @DeleteMapping("/campaigns/{campaignId}/quests/{questId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuest(@PathVariable Long campaignId, @PathVariable Long questId) {
        questService.deleteQuest(campaignId, questId);
    }

    // Partial: update quest status within a specific campaign
    @PatchMapping("/campaigns/{campaignId}/quests/{questId}/status")
    public QuestDto updateQuestStatus(@PathVariable Long campaignId, @PathVariable Long questId,
                                      @RequestBody UpdateQuestStatusRequest request) {
        return questService.updateQuestStatus(campaignId, questId, request);
    }

    // Partial: update quest visibility within a specific campaign
    @PatchMapping("/campaigns/{campaignId}/quests/{questId}/visibility")
    public QuestDto updateQuestVisibility(@PathVariable Long campaignId, @PathVariable Long questId,
                                          @RequestBody UpdateQuestVisibilityRequest request) {
        return questService.updateQuestVisibility(campaignId, questId, request);
    }
}
