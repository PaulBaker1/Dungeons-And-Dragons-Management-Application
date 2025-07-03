package com.dmtool.dashboard.questtracker.service;

import com.dmtool.dashboard.questtracker.dto.CreateQuestRequest;
import com.dmtool.dashboard.questtracker.dto.QuestDto;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestStatusRequest;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestVisibilityRequest;
import java.util.List;

public interface QuestService {
    QuestDto createQuest(Long campaignId, CreateQuestRequest request);
    QuestDto getQuestById(Long id, Long questId);
    QuestDto updateQuest(Long id, Long questId, CreateQuestRequest request);
    QuestDto updateQuestStatus(Long id, Long questId, UpdateQuestStatusRequest request);
    QuestDto updateQuestVisibility(Long id, Long questId, UpdateQuestVisibilityRequest request);
    List<QuestDto> getQuestsByCampaign(Long campaignId);
    List<QuestDto> getQuestsBySession(Long sessionId);
    void deleteQuest(Long id, Long questId);
}

