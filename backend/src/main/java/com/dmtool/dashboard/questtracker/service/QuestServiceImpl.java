package com.dmtool.dashboard.questtracker.service;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.questtracker.dto.CreateQuestRequest;
import com.dmtool.dashboard.questtracker.dto.QuestDto;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestStatusRequest;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestVisibilityRequest;
import com.dmtool.dashboard.questtracker.mapper.QuestMapper;
import com.dmtool.dashboard.questtracker.model.Quest;
import com.dmtool.dashboard.questtracker.model.QuestStatus;
import com.dmtool.dashboard.questtracker.repository.QuestRepository;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestServiceImpl implements QuestService {

    private final QuestRepository questRepository;
    private final CampaignRepository campaignRepository;
    private final SessionRepository sessionRepository;
    private final QuestMapper mapper;

    @Override
    public QuestDto createQuest(Long campaignId, CreateQuestRequest request) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found with id: " + campaignId));

        Session linkedSession = null;
        if (request.linkedSessionId() != null) {
            linkedSession = sessionRepository.findById(request.linkedSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + request.linkedSessionId()));
        }

        Quest quest = mapper.toEntity(request);
        quest.setCampaign(campaign);
        quest.setLinkedSession(linkedSession);

        // Don't override status with PLANNED if it's provided in the request
        if (request.status() != null && !request.status().isEmpty()) {
            quest.setStatus(QuestStatus.valueOf(request.status()));
        } else {
            quest.setStatus(QuestStatus.PLANNED);  // Default to PLANNED if no status is provided
        }

        Quest saved = questRepository.save(quest);
        return mapper.toDto(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public QuestDto getQuestById(Long id, Long questId) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + id));
        return mapper.toDto(quest);
    }

    @Override
    public QuestDto updateQuest(Long id, Long questId, CreateQuestRequest request) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + id));

        // Map fields from CreateQuestRequest to Quest entity
        mapper.updateFromDto(request, quest);

        // Update the fields if present
        quest.setQuestGiver(request.questGiver());
        quest.setReward(request.reward());
        quest.setNotes(request.notes());

        if (request.linkedSessionId() != null) {
            Session session = sessionRepository.findById(request.linkedSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + request.linkedSessionId()));
            quest.setLinkedSession(session);
        } else {
            quest.setLinkedSession(null);
        }

        Quest saved = questRepository.save(quest);
        return mapper.toDto(saved);
    }


    @Override
    public QuestDto updateQuestStatus(Long id, Long questId, UpdateQuestStatusRequest request) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + id));

        mapper.updateStatus(request, quest);

        if (request.status() == QuestStatus.COMPLETED || request.status() == QuestStatus.FAILED) {
            quest.setCompletedAt(LocalDateTime.now());
        }

        Quest saved = questRepository.save(quest);
        return mapper.toDto(saved);
    }

    @Override
    public QuestDto updateQuestVisibility(Long id, Long questId, UpdateQuestVisibilityRequest request) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quest not found with id: " + id));

        quest.setVisibleToPlayers(request.visibleToPlayers());

        Quest saved = questRepository.save(quest);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestDto> getQuestsByCampaign(Long campaignId) {
        return questRepository.findByCampaign_Id(campaignId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestDto> getQuestsBySession(Long sessionId) {
        return questRepository.findByLinkedSession_Id(sessionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void deleteQuest(Long id, Long questId) {
        if (!questRepository.existsById(id)) {
            throw new EntityNotFoundException("Quest not found with id: " + id);
        }
        questRepository.deleteById(id);
    }
}
