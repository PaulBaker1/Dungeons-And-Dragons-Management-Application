package com.dmtool.dashboard.encounterbuilder.service;

import com.dmtool.dashboard.encounterbuilder.dto.CreateEncounterRequest;
import com.dmtool.dashboard.encounterbuilder.dto.EncounterDto;
import com.dmtool.dashboard.encounterbuilder.mapper.EncounterMapper;
import com.dmtool.dashboard.encounterbuilder.model.Combatant;
import com.dmtool.dashboard.encounterbuilder.model.Encounter;
import com.dmtool.dashboard.encounterbuilder.repository.EncounterRepository;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final SessionRepository sessionRepository;  // Use SessionRepository now
    private final EncounterMapper mapper;
    private final ObjectMapper objectMapper; // For JSON import

    @Override
    public EncounterDto createEncounter(CreateEncounterRequest request) {
        // Validate and load Session
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + request.sessionId()));

        Encounter encounter = mapper.toEntity(request);
        encounter.setSession(session);

        if (request.combatants() != null) {
            List<Combatant> combatants = request.combatants().stream().map(dto ->
                    Combatant.builder()
                            .name(dto.name())
                            .hp(dto.hp())
                            .xp(dto.xp())
                            .cr(dto.cr())
                            .quantity(dto.quantity())
                            .legendary(dto.specialAbilities() != null && !dto.specialAbilities().isEmpty())
                            .initiative(dto.initiative())
                            .isTemplate(dto.isTemplate())
                            .actions(dto.actions() != null ? dto.actions() : new ArrayList<>())
                            .specialAbilities(dto.specialAbilities() != null ? dto.specialAbilities() : new ArrayList<>())
                            .build()
            ).toList();
            encounter.setCombatants(combatants);
        }


        recalculateMetrics(encounter);
        Encounter saved = encounterRepository.save(encounter);
        return mapper.toDto(saved);
    }

    @Override
    public EncounterDto getEncounterById(Long id) {
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Encounter not found with id: " + id));
        return mapper.toDto(encounter);
    }

    @Override
    public List<EncounterDto> getEncountersBySession(Long sessionId) {
        return encounterRepository.findBySession_Id(sessionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public EncounterDto importEncounter(String jsonPayload) {
        try {
            CreateEncounterRequest request = objectMapper.readValue(jsonPayload, CreateEncounterRequest.class);
            return createEncounter(request);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid JSON payload", ex);
        }
    }

    @Override
    public void deleteEncounter(Long id) {
        if (!encounterRepository.existsById(id)) {
            throw new EntityNotFoundException("Encounter not found with id: " + id);
        }
        encounterRepository.deleteById(id);
    }

    // Recalculate total XP and average challenge rating from combatants
    public void recalculateMetrics(Encounter encounter) {
        int totalXP = encounter.getCombatants().stream().mapToInt(Combatant::getXp).sum();
        double averageCR = encounter.getCombatants().stream().mapToDouble(Combatant::getCr).average().orElse(0);
        encounter.setTotalXP(totalXP);
        encounter.setChallengeRating(averageCR);
    }
}
