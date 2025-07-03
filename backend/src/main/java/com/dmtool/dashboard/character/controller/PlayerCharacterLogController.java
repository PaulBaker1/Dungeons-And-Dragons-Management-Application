package com.dmtool.dashboard.character.controller;

import com.dmtool.dashboard.character.dto.CreateCharacterLogRequest;
import com.dmtool.dashboard.character.dto.PlayerCharacterLogDto;
import com.dmtool.dashboard.character.dto.PlayerUpdateCharacterLogRequest;
import com.dmtool.dashboard.character.service.PlayerCharacterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlayerCharacterLogController {

    private final PlayerCharacterLogService playerCharacterLogService;

    @PostMapping("/characterlogs")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerCharacterLogDto createPlayerLog(@RequestBody CreateCharacterLogRequest request) {
        return playerCharacterLogService.createPlayerLog(request);
    }

    @GetMapping("/campaigns/{campaignId}/logs")
    public List<PlayerCharacterLogDto> getLogsByCampaign(@PathVariable Long campaignId) {
        return playerCharacterLogService.getLogsByCampaign(campaignId);
    }

    @GetMapping("/sessions/{sessionId}/logs")
    public List<PlayerCharacterLogDto> getLogsBySession(@PathVariable Long sessionId) {
        return playerCharacterLogService.getLogsBySession(sessionId);
    }

    @GetMapping("/characterlogs/search")
    public List<PlayerCharacterLogDto> searchLogsByPlayerName(@RequestParam String playerName) {
        return playerCharacterLogService.searchLogsByPlayerName(playerName);
    }

    @PatchMapping("/characterlogs/{id}")
    public PlayerCharacterLogDto updatePlayerLog(@PathVariable Long id, @RequestBody PlayerUpdateCharacterLogRequest request) {
        return playerCharacterLogService.updatePlayerLog(id, request);
    }}
