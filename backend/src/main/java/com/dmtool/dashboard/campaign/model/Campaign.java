package com.dmtool.dashboard.campaign.model;

import com.dmtool.dashboard.character.model.PlayerCharacter;
import com.dmtool.dashboard.session.model.Session;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "campaign")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // Replace @SuperBuilder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Version
    @Column(name = "version")
    private Long version = 0L;


    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    private LevelRange recommendedLevelRange;

    @ElementCollection
    @CollectionTable(name = "campaign_sections", joinColumns = @JoinColumn(name = "campaign_id"))
    private List<CampaignSection> sections = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignLocation> keyLocations = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "campaign_factions",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "faction_id")
    )
    private Set<Faction> involvedFactions = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "campaign_tags",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCharacter> characters = new ArrayList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Column(name = "visible_to_players", nullable = false)
    @Builder.Default
    private Boolean visibleToPlayers = false;

    @Builder.Default
    private boolean pinned = false;

    private String adventureCode;

    @Setter
    @Getter
    @Column(nullable = false)
    private boolean archived = false;

    private LocalDateTime lastSessionDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Column(columnDefinition = "TEXT")
    private String dmNotesPrivate;

    @Column(columnDefinition = "TEXT")
    private String dmNotesPublic;

    @Column(nullable = true)
    String bannerImageUrl;
}