package com.dmtool.dashboard.prepitem.service;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.prepitem.dto.CreatePrepItemRequest;
import com.dmtool.dashboard.prepitem.dto.PrepItemDto;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepStatusRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepVisibilityRequest;
import com.dmtool.dashboard.prepitem.mapper.PrepItemMapper;
import com.dmtool.dashboard.prepitem.model.PrepItem;
import com.dmtool.dashboard.prepitem.model.PrepStatus;
import com.dmtool.dashboard.prepitem.repository.PrepItemRepository;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrepItemServiceImpl implements PrepItemService {

    private final PrepItemRepository prepItemRepository;
    private final CampaignRepository campaignRepository;
    private final SessionRepository sessionRepository;
    private final PrepItemMapper mapper;

    @Override
    public PrepItemDto createPrepItem(CreatePrepItemRequest request) {
        // Validate and load Campaign
        Campaign campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found with id: " + request.campaignId()));

        // Optionally, load Session if provided
        Session session = null;
        if (request.sessionId() != null) {
            session = sessionRepository.findById(request.sessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + request.sessionId()));
        }

        PrepItem prepItem = mapper.toEntity(request);
        prepItem.setCampaign(campaign);
        prepItem.setSession(session);
        // Set default status
        prepItem.setStatus(PrepStatus.TODO);
        PrepItem saved = prepItemRepository.save(prepItem);
        return mapper.toDto(saved);
    }

    @Override
    public PrepItemDto updatePrepStatus(Long id, UpdatePrepStatusRequest request) {
        PrepItem prepItem = prepItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PrepItem not found with id: " + id));
        mapper.updateStatus(request, prepItem);
        PrepItem saved = prepItemRepository.save(prepItem);
        return mapper.toDto(saved);
    }

    @Override
    public PrepItemDto updatePrepVisibility(Long id, UpdatePrepVisibilityRequest request) {
        PrepItem prepItem = prepItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PrepItem not found with id: " + id));
        mapper.updateVisibility(request, prepItem);
        PrepItem saved = prepItemRepository.save(prepItem);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepItemDto> getPrepItemsBySession(Long sessionId) {
        return prepItemRepository.findBySession_Id(sessionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepItemDto> getPrepItemsBacklogByCampaign(Long campaignId) {
        return prepItemRepository.findByCampaign_IdAndSessionIsNull(campaignId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void deletePrepItem(Long id) {
        if (!prepItemRepository.existsById(id)) {
            throw new EntityNotFoundException("PrepItem not found with id: " + id);
        }
        prepItemRepository.deleteById(id);
    }
}
