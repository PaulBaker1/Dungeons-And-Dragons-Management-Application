package com.dmtool.dashboard.session.controller;

import com.dmtool.dashboard.session.dto.SessionCreateDto;
import com.dmtool.dashboard.session.dto.SessionDto;
import com.dmtool.dashboard.session.dto.SessionUpdateDto;
import com.dmtool.dashboard.session.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionDto> list(@PathVariable Long campaignId) {
        return sessionService.getAllSessions(campaignId);
    }

    @GetMapping("/{id}")
    public SessionDto get(@PathVariable Long campaignId, @PathVariable Long id) {
        return sessionService.getSessionById(campaignId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SessionDto> create(@PathVariable Long campaignId,
                                             @Valid @RequestBody SessionCreateDto dto) {
        return sessionService.create(campaignId, dto);
    }

    @PutMapping("/{id}")
    public SessionDto update(@PathVariable Long campaignId,
                             @PathVariable Long id,
                             @RequestBody SessionUpdateDto dto) {
        return sessionService.updateSession(campaignId, id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long campaignId, @PathVariable Long id) {
        sessionService.deleteSession(campaignId, id);
    }
}
