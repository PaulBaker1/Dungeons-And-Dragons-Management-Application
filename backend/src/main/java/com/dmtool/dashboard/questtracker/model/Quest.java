package com.dmtool.dashboard.questtracker.model;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.session.model.Session;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestPriority priority;

    // Tags stored as an element collection with a join table
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quest_tags", joinColumns = @JoinColumn(name = "quest_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    // Optional link to the session where the quest was introduced
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_session_id")
    private Session linkedSession;

    // Each quest belongs to a campaign (required)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // Optional timestamp for completed quests
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Standard timestamp fields (could be inherited from BaseEntity)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Visibility field
    @Column(name = "visible_to_players", nullable = false)
    private Boolean visibleToPlayers = true;

    // New fields: Quest Giver, Reward, and Notes
    @Column(name = "quest_giver")
    private String questGiver;

    @Column(name = "reward")
    private String reward;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = QuestStatus.PLANNED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
