package com.dmtool.dashboard.character.service;

import com.dmtool.dashboard.character.dto.PlayerCharacterDto;
import com.dmtool.dashboard.character.dto.PlayerCreateCharacterRequest;
import com.dmtool.dashboard.character.mapper.PlayerCharacterMapper;
import com.dmtool.dashboard.character.model.PlayerCharacter;
import com.dmtool.dashboard.character.repository.PlayerCharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerCharacterServiceImpl implements PlayerCharacterService {

    private final PlayerCharacterRepository playerCharacterRepository;
    private final PlayerCharacterMapper playerCharacterMapper;

    @Autowired
    public PlayerCharacterServiceImpl(PlayerCharacterRepository playerCharacterRepository, PlayerCharacterMapper playerCharacterMapper) {
        this.playerCharacterRepository = playerCharacterRepository;
        this.playerCharacterMapper = playerCharacterMapper;
    }

    @Override
    public PlayerCharacterDto create(PlayerCreateCharacterRequest request) {
        // Convert the PlayerCreateCharacterRequest to PlayerCharacter entity
        PlayerCharacter playerCharacter = playerCharacterMapper.toEntity(request);

        // Save the new PlayerCharacter entity in the repository
        playerCharacter = playerCharacterRepository.save(playerCharacter);

        // Convert the saved PlayerCharacter entity to PlayerCharacterDto and return it
        return playerCharacterMapper.toDto(playerCharacter);
    }

    @Override
    public PlayerCharacterDto update(Long id, PlayerCreateCharacterRequest request) {
        // Fetch the existing PlayerCharacter by id from the repository
        PlayerCharacter existingCharacter = playerCharacterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player character not found with id: " + id));

        // Use the mapper to update the existing entity with the new data from the request
        playerCharacterMapper.updateEntity(existingCharacter, request);

        // Save the updated PlayerCharacter entity in the repository
        existingCharacter = playerCharacterRepository.save(existingCharacter);

        // Convert the updated PlayerCharacter entity to PlayerCharacterDto and return it
        return playerCharacterMapper.toDto(existingCharacter);
    }

    @Override
    public void delete(Long id) {
        // Delete the PlayerCharacter entity by its id
        if (playerCharacterRepository.existsById(id)) {
            playerCharacterRepository.deleteById(id);
        } else {
            throw new RuntimeException("Player character not found with id: " + id);
        }
    }

    @Override
    public PlayerCharacterDto findCharacterById(Long id) {
        // Find the PlayerCharacter by ID
        PlayerCharacter playerCharacter = playerCharacterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player character not found with id: " + id));

        // Map the PlayerCharacter entity to the DTO and return it
        return playerCharacterMapper.toDto(playerCharacter);
    }

    @Override
    public List<PlayerCharacterDto> findUnassignedCharacters() {
        // Find characters that are not assigned to any campaign (campaign is null)
        List<PlayerCharacter> unassignedCharacters = playerCharacterRepository.findByCampaignIsNull();
        return unassignedCharacters.stream()
                .map(playerCharacterMapper::toDto)
                .collect(Collectors.toList());
    }

    // Method to fetch all characters
    @Override
    public List<PlayerCharacterDto> findAll() {
        // Fetch all characters from the repository
        List<PlayerCharacter> characters = playerCharacterRepository.findAll();

        // Map the list of PlayerCharacter entities to PlayerCharacterDto and return
        return characters.stream()
                .map(playerCharacterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerCharacterDto> findByCampaign(Long campaignId) {
        // Fetch all PlayerCharacter entities associated with the given campaign id
        List<PlayerCharacter> characters = playerCharacterRepository.findByCampaignId(campaignId);

        // Convert the list of PlayerCharacter entities to a list of PlayerCharacterDto and return it
        return characters.stream()
                .map(playerCharacterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCharacterFromCampaign(Long id) {
        PlayerCharacter character = playerCharacterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Character not found with id: " + id));
        character.setCampaign(null);
        playerCharacterRepository.save(character);
    }
}
