package com.dmtool.dashboard.character.service;

import com.dmtool.dashboard.character.dto.PlayerCharacterDto;
import com.dmtool.dashboard.character.dto.PlayerCreateCharacterRequest;

import java.util.List;

public interface PlayerCharacterService {
    PlayerCharacterDto create(PlayerCreateCharacterRequest request);

    // Method to fetch all characters
    List<PlayerCharacterDto> findAll();

    List<PlayerCharacterDto> findByCampaign(Long campaignId);
    PlayerCharacterDto update(Long id, PlayerCreateCharacterRequest request);
    void delete(Long id);
    PlayerCharacterDto findCharacterById(Long id);

    List<PlayerCharacterDto> findUnassignedCharacters();

    void removeCharacterFromCampaign(Long id);
}
