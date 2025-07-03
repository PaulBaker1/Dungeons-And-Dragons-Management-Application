package com.dmtool.dashboard.character.model;

import com.dmtool.dashboard.campaign.model.Campaign;

import com.dmtool.dashboard.session.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "character_log")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PlayerCharacterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "character_name", nullable = false)
    private String characterName;

    @Column(name = "class_and_level", nullable = false)
    private String classAndLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "faction")
    private Faction faction;

    @Column(name = "inspiration_used", nullable = false)
    private boolean inspirationUsed;

    @Column(name = "death_saves_failed", nullable = false)
    private int deathSavesFailed;

    @ElementCollection
    @CollectionTable(name =  "player_log_magic_items", joinColumns = @JoinColumn(name = "player_log_id"))
    @Column(name = "magic_item")
    private List<String> magicItemsFound = new ArrayList<>();

    @Column(name = "gold_earned", nullable = false)
    private BigDecimal goldEarned;

    @Column(name = "downtime_gained", nullable = false)
    private int downtimeGained;

    @Column(name = "custom_notes", columnDefinition = "TEXT")
    private String customNotes;

    @ManyToOne
    private Campaign campaign;
}
