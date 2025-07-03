package com.dmtool.dashboard.session.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "session_note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SessionNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteType type; // DM, PLAYER, or GENERAL

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private Session session;

    public enum NoteType {
        DM, PLAYER, GENERAL
    }
}
