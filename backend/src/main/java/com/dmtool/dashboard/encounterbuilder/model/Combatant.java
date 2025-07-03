package com.dmtool.dashboard.encounterbuilder.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Combatant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int xp;

    @Column(nullable = false)
    private double cr;

    private int initiative;

    @Column(name = "is_template", nullable = false)
    private boolean isTemplate;

    // New fields added:
    @Column(nullable = false)
    private int ac; // Armor Class

    @Column(nullable = false)
    private int quantity; // Number of monsters represented

    @Column(nullable = false)
    private boolean legendary; // True if monster has special abilities

    // Existing collections for additional details
    @ElementCollection
    @CollectionTable(name = "combatant_actions", joinColumns = @JoinColumn(name = "combatant_id"))
    @Column(name = "action")
    private List<String> actions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "combatant_special_abilities", joinColumns = @JoinColumn(name = "combatant_id"))
    @Column(name = "special_ability")
    private List<String> specialAbilities = new ArrayList<>();
}
