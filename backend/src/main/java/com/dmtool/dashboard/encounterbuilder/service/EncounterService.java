package com.dmtool.dashboard.encounterbuilder.service;

import com.dmtool.dashboard.encounterbuilder.dto.CreateEncounterRequest;
import com.dmtool.dashboard.encounterbuilder.dto.EncounterDto;

import java.util.List;

public interface EncounterService {
    EncounterDto createEncounter(CreateEncounterRequest request);
    EncounterDto getEncounterById(Long id);
    List<EncounterDto> getEncountersBySession(Long sessionId);
    EncounterDto importEncounter(String jsonPayload);
    void deleteEncounter(Long id);
}
