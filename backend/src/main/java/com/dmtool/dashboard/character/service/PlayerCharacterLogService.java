package com.dmtool.dashboard.character.service;

import com.dmtool.dashboard.character.dto.CreateCharacterLogRequest;
import com.dmtool.dashboard.character.dto.PlayerCharacterLogDto;
import com.dmtool.dashboard.character.dto.PlayerUpdateCharacterLogRequest;

import java.util.List;

public interface PlayerCharacterLogService {
    PlayerCharacterLogDto createPlayerLog(CreateCharacterLogRequest request);
    PlayerCharacterLogDto updatePlayerLog(Long id, PlayerUpdateCharacterLogRequest request);
    List<PlayerCharacterLogDto> getLogsByCampaign(Long campaignId);
    List<PlayerCharacterLogDto> getLogsBySession(Long sessionId);
    List<PlayerCharacterLogDto> searchLogsByPlayerName(String playerName);
}
