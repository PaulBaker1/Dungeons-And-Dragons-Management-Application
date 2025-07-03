package com.dmtool.dashboard.session.service;

import com.dmtool.dashboard.session.dto.SessionCreateDto;
import com.dmtool.dashboard.session.dto.SessionDto;
import com.dmtool.dashboard.session.dto.SessionUpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SessionService {
    List<SessionDto> getAllSessions(Long campaignId);

    SessionDto getSessionById(Long campaignId, Long id);

    SessionDto updateSession(Long campaignId, Long id, SessionUpdateDto dto);

    void deleteSession(Long campaignId, Long id);

    List<SessionDto> getSessionsForCampaign(Long campaignId);

    ResponseEntity<SessionDto> create(Long campaignId, SessionCreateDto dto);

}