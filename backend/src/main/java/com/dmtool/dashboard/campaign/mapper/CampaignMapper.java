package com.dmtool.dashboard.campaign.mapper;

import com.dmtool.dashboard.campaign.dto.CampaignCreateDTO;
import com.dmtool.dashboard.campaign.dto.CampaignDetailsDTO;
import com.dmtool.dashboard.campaign.dto.CampaignSummaryDTO;
import com.dmtool.dashboard.campaign.dto.CampaignUpdateDTO;
import com.dmtool.dashboard.campaign.model.*;
import com.dmtool.dashboard.campaign.repository.FactionRepository;
import com.dmtool.dashboard.campaign.repository.TagRepository;
import com.dmtool.dashboard.session.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CampaignMapper {

    private final FactionRepository factionRepo;
    private final TagRepository tagRepo;

    public Campaign fromCreateDTO(CampaignCreateDTO dto) {
        Campaign entity = new Campaign();

        // ✅ Basic fields
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setStatus(dto.status());
        entity.setAdventureCode(dto.adventureCode());
        entity.setVisibleToPlayers(dto.visibleToPlayers());
        entity.setPinned(false);// Default false unless updated

        // ✅ Recommended level range
        LevelRange levelRange = new LevelRange();
        levelRange.setMinLevel(dto.minLevel());
        levelRange.setMaxLevel(dto.maxLevel());
        entity.setRecommendedLevelRange(levelRange);


        // ✅ Sections
        if (dto.sections() != null && !dto.sections().isEmpty()) {
            entity.setSections(dto.sections().stream()
                    .map(CampaignSection::new)
                    .collect(Collectors.toList()));
        } else {
            entity.setSections(new ArrayList<>());
        }

        // ✅ Locations
        if (dto.keyLocations() != null && !dto.keyLocations().isEmpty()) {
            entity.setKeyLocations(dto.keyLocations().stream()
                    .map(name -> new CampaignLocation(name, entity))
                    .collect(Collectors.toList()));
        } else {
            entity.setKeyLocations(new ArrayList<>());
        }

        // ✅ Factions
        Set<Long> factionIds = dto.involvedFactions();
        if (factionIds != null && !factionIds.isEmpty()) {
            entity.setInvolvedFactions(new HashSet<>(factionRepo.findAllById(factionIds)));
        } else {
            entity.setInvolvedFactions(new HashSet<>());
        }

        // ✅ Tags
        entity.setTags(resolveTags(dto.tags()));

        return entity;
    }

    public CampaignDetailsDTO toDetailsDTO(Campaign entity) {
        return new CampaignDetailsDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.isArchived(),
                entity.getLastSessionDate(),
                entity.isPinned(),
                entity.getDmNotesPrivate(),
                entity.getDmNotesPublic(),
                entity.getRecommendedLevelRange(),  // ✅ Pass LevelRange directly
                entity.getSections().stream()
                        .map(CampaignSection::getTitle)
                        .collect(Collectors.toSet()),
                entity.getKeyLocations().stream()
                        .map(CampaignLocation::getName)
                        .collect(Collectors.toSet()),
                entity.getAdventureCode(),
                entity.getInvolvedFactions().stream()
                        .map(Faction::getName)
                        .collect(Collectors.toSet()),
                entity.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toSet()),
                entity.getStatus(),
                entity.getVisibleToPlayers(),
                entity.getCharacters()
        );

    }

    public CampaignSummaryDTO toSummaryDTO(Campaign entity) {
        // Default to "—" if no session objective is found.
        String lastSessionObjective = "—";

        if (entity.getLastSessionDate() != null && entity.getSessions() != null) {
            // Find the session that matches the lastSessionDate
            lastSessionObjective = entity.getSessions().stream()
                    .filter(s -> s.getSessionDate() != null
                            && s.getSessionDate().equals(entity.getLastSessionDate()))
                    .map(Session::getObjective)  // Assuming getObjective() returns a String
                    .findFirst()
                    .orElse("—");
        }

        return new CampaignSummaryDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                lastSessionObjective,
                entity.isPinned()
        );
    }

    public void update(Campaign entity, CampaignUpdateDTO dto) {
        if (dto.title() != null)
            entity.setTitle(dto.title());

        if (dto.description() != null)
            entity.setDescription(dto.description());

        if (dto.recommendedLevelRange() != null)
            entity.setRecommendedLevelRange(dto.recommendedLevelRange());

        if (dto.sections() != null) {
            entity.getSections().clear();
            dto.sections().forEach(title -> entity.getSections().add(new CampaignSection(title)));
        }

        if (dto.keyLocations() != null) {
            entity.getKeyLocations().clear();
            dto.keyLocations().forEach(name -> entity.getKeyLocations().add(new CampaignLocation(name, entity)));
        }

        if (dto.adventureCode() != null)
            entity.setAdventureCode(dto.adventureCode());

        if (dto.involvedFactions() != null) {
            entity.setInvolvedFactions(new HashSet<>(factionRepo.findAllById(dto.involvedFactions())));
        }

        if (dto.tags() != null) {
            entity.setTags(resolveTags(dto.tags()));
        }

        if (dto.status() != null)
            entity.setStatus(dto.status());

        if (dto.visibleToPlayers() != null)
            entity.setVisibleToPlayers(dto.visibleToPlayers());

        if (dto.pinned() != null)
            entity.setPinned(dto.pinned());

        if (dto.dmNotesPrivate() != null)
            entity.setDmNotesPrivate(dto.dmNotesPrivate());

        if (dto.dmNotesPublic() != null)
            entity.setDmNotesPublic(dto.dmNotesPublic());

        if (dto.bannerImageUrl() != null) {
            entity.setBannerImageUrl(dto.bannerImageUrl());
        }

    }

    private Set<Tag> resolveTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) return Set.of();
        return tagNames.stream()
                .map(name -> tagRepo.findByName(name).orElseGet(() -> tagRepo.save(new Tag(null, name))))
                .collect(Collectors.toSet());
    }
}
