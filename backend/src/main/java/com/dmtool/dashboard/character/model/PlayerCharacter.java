package com.dmtool.dashboard.character.model;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.session.model.Session;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "character")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private String characterName;
    private String characterClass;
    private String avatar;
    private int experience;
    private int level;
    private boolean levelOverride;
    private int hitPoints;
    private int armorClass;
    private int spellDC;
    private int speed;
    private int initiative;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private boolean passivePerception;
    private boolean passiveInvestigation;
    private boolean passiveInsight;
    private boolean jackOfAllTrades;
    private String playerControlled;

    @ElementCollection
    private List<String> damageResistances;

    @ElementCollection
    private List<String> damageVulnerabilities;

    @ElementCollection
    private List<String> damageImmunities;

    @ElementCollection
    private List<String> conditionImmunities;

    @ElementCollection
    private List<String> skillProficiencies;

    @ManyToOne
    private Campaign campaign;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "playerCharacter")
    @JsonManagedReference
    private List<Session> sessions;
}