package com.dmtool.dashboard.session.mapper;

import com.dmtool.dashboard.session.dto.*;
import com.dmtool.dashboard.session.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionMapper {

    public Session fromCreateDto(SessionCreateDto dto) {
        Session session = Session.builder()
                .title(dto.title())
                .objective(dto.objective())
                .summary(dto.summary())
                .sessionDate(dto.sessionDate())
                .status(dto.status())
                .build();

        // Safely map all collections (handles null cases)
        session.setAgenda(mapAgendaItems(dto.agendaItems(), session));
        session.setEncounters(mapEncounters(dto.encounters(), session));
        session.setNotes(mapNotes(dto.notes(), session));
        session.setDecisions(mapDecisions(dto.decisions(), session));
        session.setKeyItems(mapKeyItems(dto.keyItems(), session));

        return session;
    }

    public void update(Session session, SessionUpdateDto dto) {
        if (dto.title() != null) session.setTitle(dto.title());
        if (dto.objective() != null) session.setObjective(dto.objective());
        if (dto.summary() != null) session.setSummary(dto.summary());
        if (dto.sessionDate() != null) session.setSessionDate(dto.sessionDate());
        if (dto.status() != null) session.setStatus(dto.status());

        // Replace child lists entirely if provided (or skip if null)
        if (dto.agendaItems() != null) {
            session.getAgenda().clear();
            session.getAgenda().addAll(mapAgendaItems(dto.agendaItems(), session));
        }

        if (dto.encounters() != null) {
            session.getEncounters().clear();
            session.getEncounters().addAll(mapEncounters(dto.encounters(), session));
        }

        if (dto.notes() != null) {
            session.getNotes().clear();
            session.getNotes().addAll(mapNotes(dto.notes(), session));
        }

        if (dto.decisions() != null) {
            session.getDecisions().clear();
            session.getDecisions().addAll(mapDecisions(dto.decisions(), session));
        }

        if (dto.keyItems() != null) {
            session.getKeyItems().clear();
            session.getKeyItems().addAll(mapKeyItems(dto.keyItems(), session));
        }
    }


    // --- Agenda Items ---
    private List<SessionAgendaItem> mapAgendaItems(List<AgendaItemDto> dtos, Session session) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(dto -> SessionAgendaItem.builder()
                        .content(dto.content())
                        .dmOnly(dto.dmOnly())
                        .order(dto.order())
                        .session(session)
                        .build())
                .collect(Collectors.toList());
    }

    private List<AgendaItemDto> mapAgendaItemsToDto(List<SessionAgendaItem> items) {
        return items.stream()
                .map(i -> new AgendaItemDto(i.getId(), i.getContent(), i.isDmOnly(), i.getOrder()))
                .toList();
    }

    // --- Encounters ---
    private List<SessionEncounter> mapEncounters(List<EncounterDto> dtos, Session session) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(dto -> SessionEncounter.builder()
                        .name(dto.name())
                        .location(dto.location())
                        .estimatedDifficulty(dto.estimatedDifficulty())
                        .outcome(dto.outcome())
                        .session(session)
                        .build())
                .collect(Collectors.toList());
    }

    private List<EncounterDto> mapEncountersToDto(List<SessionEncounter> encounters) {
        return encounters.stream()
                .map(e -> new EncounterDto(
                        e.getId(),
                        e.getName(),
                        e.getLocation(),
                        e.getEstimatedDifficulty(),
                        e.getOutcome()
                ))
                .toList();
    }

    // --- Notes ---
    private List<SessionNote> mapNotes(List<SessionNoteDto> dtos, Session session) {
        return dtos.stream()
                .map(dto -> SessionNote.builder()
                        .type(dto.type())
                        .content(dto.content())
                        .session(session)
                        .build())
                .collect(Collectors.toList()); // Explicit collector
    }

    private List<SessionNoteDto> mapNotesToDto(List<SessionNote> notes) {
        return notes.stream()
                .map(n -> new SessionNoteDto(n.getId(), n.getType(), n.getContent()))
                .toList();
    }

    // --- Decisions ---
    private List<SessionDecision> mapDecisions(List<SessionDecisionDto> dtos, Session session) {
        return dtos.stream()
                .map(dto -> SessionDecision.builder()
                        .decisionPoint(dto.decisionPoint())
                        .choiceMade(dto.choiceMade())
                        .consequences(dto.consequences())
                        .session(session)
                        .build())
                .collect(Collectors.toList()); // Explicit collector
    }

    private List<SessionDecisionDto> mapDecisionsToDto(List<SessionDecision> decisions) {
        return decisions.stream()
                .map(d -> new SessionDecisionDto(
                        d.getId(),
                        d.getDecisionPoint(),
                        d.getChoiceMade(),
                        d.getConsequences()
                ))
                .toList();
    }

    // --- Key Items ---
    private List<KeyItemTracker> mapKeyItems(List<KeyItemDto> dtos, Session session) {
        return dtos.stream()
                .map(dto -> KeyItemTracker.builder()
                        .itemName(dto.itemName())
                        .description(dto.description())
                        .acquired(dto.acquired())
                        .locationFound(dto.locationFound())
                        .usedIn(dto.usedIn())
                        .session(session)
                        .build())
                .collect(Collectors.toList()); // Explicit collector
    }

    private List<KeyItemDto> mapKeyItemsToDto(List<KeyItemTracker> items) {
        return items.stream()
                .map(i -> new KeyItemDto(
                        i.getId(),
                        i.getItemName(),
                        i.getDescription(),
                        i.isAcquired(),
                        i.getLocationFound(),
                        i.getUsedIn()
                ))
                .toList();
    }

    // --- DTO Mapping ---
    public SessionDto toDto(Session session) {
        return new SessionDto(
                session.getId(),
                session.getTitle(),
                session.getObjective(),
                session.getSummary(),
                session.getStatus(),
                session.getSessionDate(),

                session.getAgenda() != null
                        ? mapAgendaItemsToDto(session.getAgenda())
                        : List.of(),

                session.getEncounters() != null
                        ? mapEncountersToDto(session.getEncounters())
                        : List.of(),

                session.getNotes() != null
                        ? mapNotesToDto(session.getNotes())
                        : List.of(),

                session.getDecisions() != null
                        ? mapDecisionsToDto(session.getDecisions())
                        : List.of(),

                session.getKeyItems() != null
                        ? mapKeyItemsToDto(session.getKeyItems())
                        : List.of(),

                // new text fields
                session.getUpcomingNotes(),
                session.getPastNotes(),
                session.getAttendanceNotes()
        );
    }
}