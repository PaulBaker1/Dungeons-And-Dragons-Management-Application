package com.dmtool.dashboard.character.controller;

import com.dmtool.dashboard.character.dto.PlayerCreateCharacterRequest;
import com.dmtool.dashboard.character.dto.PlayerCharacterDto;
import com.dmtool.dashboard.character.mapper.PlayerCharacterMapper;
import com.dmtool.dashboard.character.model.PlayerCharacter;
import com.dmtool.dashboard.character.repository.PlayerCharacterRepository;
import com.dmtool.dashboard.character.service.PlayerCharacterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class PlayerCharacterController {

    private final PlayerCharacterService service;
    private final PlayerCharacterMapper mapper;
    private final PlayerCharacterRepository repository;

//    // POST endpoint for creating a new character
//    @PostMapping
//    public PlayerCharacterDto create(@RequestBody PlayerCreateCharacterRequest request) {
//        return service.create(request);
//    }

    // Add this POST handler
    @PostMapping
    public PlayerCharacterDto createCharacter(@RequestBody PlayerCreateCharacterRequest request) {
        return service.create(request);
    }

    // PUT endpoint for updating a character
    @PutMapping("/{id}")
    public PlayerCharacterDto update(
            @PathVariable Long id,
            @RequestBody PlayerCreateCharacterRequest request) {
        return service.update(id, request);
    }

    // GET endpoint for all characters
    @GetMapping
    public List<PlayerCharacterDto> getAllCharacters() {
        return service.findAll();
    }

    // GET endpoint for a specific character by ID
    @GetMapping("/{id}")
    public PlayerCharacterDto getCharacterById(@PathVariable Long id) {
        return service.findCharacterById(id);
    }

    // DELETE endpoint for deleting a character
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/unassigned")
    public List<PlayerCharacterDto> getUnassignedCharacters() {
        return service.findUnassignedCharacters();
    }

    @DeleteMapping("/{id}/campaign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCharacterFromCampaign(@PathVariable Long id) {
        service.removeCharacterFromCampaign(id);
    }
}
