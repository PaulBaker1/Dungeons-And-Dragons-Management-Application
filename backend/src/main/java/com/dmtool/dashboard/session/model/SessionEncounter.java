package com.dmtool.dashboard.session.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "session_encounter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SessionEncounter {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(name = "estimated_difficulty")
    private String estimatedDifficulty;

    @Column(name = "outcome", length = 1000)
    private String outcome;

    @Column(name = "player_visible", nullable = false)
    private boolean playerVisible;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private Session session;
}
