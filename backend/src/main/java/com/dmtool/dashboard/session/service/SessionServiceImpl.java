package com.dmtool.dashboard.session.service;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.session.dto.SessionCreateDto;
import com.dmtool.dashboard.session.dto.SessionDto;
import com.dmtool.dashboard.session.dto.SessionUpdateDto;
import com.dmtool.dashboard.session.mapper.SessionMapper;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final CampaignRepository campaignRepository;
    private final SessionMapper sessionMapper;

    @Override
    public List<SessionDto> getAllSessions(Long campaignId) {
        return sessionRepository.findByCampaignId(campaignId).stream()
                .map(sessionMapper::toDto)
                .toList();
    }

    @Override
    public SessionDto getSessionById(Long campaignId, Long id) {
        return sessionRepository.findByIdAndCampaignId(id, campaignId)
                .map(sessionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
    }

    @Override
    public SessionDto updateSession(Long campaignId, Long id, SessionUpdateDto dto) {
        var session = sessionRepository.findByIdAndCampaignId(id, campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found: " + id));

        sessionMapper.update(session, dto);
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    @Override
    public void deleteSession(Long campaignId, Long id) {
        if (!sessionRepository.existsByIdAndCampaignId(id, campaignId)) {
            throw new EntityNotFoundException("Session not found with id: " + id);
        }
        sessionRepository.deleteByIdAndCampaignId(id, campaignId);
    }

    // SessionServiceImpl.java
    @Override
    public List<SessionDto> getSessionsForCampaign(Long campaignId) {
        List<Session> sessions = sessionRepository.findByCampaignId(campaignId);
        return sessions.stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<SessionDto> create(Long campaignId, SessionCreateDto dto) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));

        Session session = sessionMapper.fromCreateDto(dto);
        session.setCampaign(campaign);

        session = sessionRepository.save(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionMapper.toDto(session));
    }
}