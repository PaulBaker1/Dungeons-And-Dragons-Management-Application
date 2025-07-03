package com.dmtool.dashboard.character.service;

import com.dmtool.dashboard.character.dto.CreateCharacterLogRequest;
import com.dmtool.dashboard.character.dto.PlayerCharacterLogDto;
import com.dmtool.dashboard.character.dto.PlayerUpdateCharacterLogRequest;
import com.dmtool.dashboard.character.mapper.PlayerCharacterLogMapper;
import com.dmtool.dashboard.character.model.PlayerCharacterLog;
import com.dmtool.dashboard.character.repository.PlayerCharacterLogRepository;
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
public class PlayerCharacterLogServiceImpl implements PlayerCharacterLogService {
    private final PlayerCharacterLogRepository playerCharacterLogRepository;
    private final SessionRepository sessionRepository;
    private final PlayerCharacterLogMapper mapper;

    @Override
    public PlayerCharacterLogDto createPlayerLog(CreateCharacterLogRequest request) {
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + request.sessionId()));

        PlayerCharacterLog playerCharacterLog =mapper.toEntity(request);
        playerCharacterLog.setSession(session);
        PlayerCharacterLog saved = playerCharacterLogRepository.save(playerCharacterLog);
        return mapper.toDto(saved);
    }

    @Override
    public PlayerCharacterLogDto updatePlayerLog(Long id, PlayerUpdateCharacterLogRequest request) {
        PlayerCharacterLog playerCharacterLog = playerCharacterLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlayerLog not found with id: " + id));
        mapper.playerUpdatePlayerLogFromDto(request, playerCharacterLog);
        PlayerCharacterLog saved = playerCharacterLogRepository.save(playerCharacterLog);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerCharacterLogDto> getLogsByCampaign(Long campaignId) {
        return playerCharacterLogRepository.findBySession_Campaign_Id(campaignId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerCharacterLogDto> getLogsBySession(Long sessionId) {
        return playerCharacterLogRepository.findBySession_Id(sessionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerCharacterLogDto> searchLogsByPlayerName(String playerName) {
        return playerCharacterLogRepository.findByPlayerNameIgnoreCaseContaining(playerName)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
