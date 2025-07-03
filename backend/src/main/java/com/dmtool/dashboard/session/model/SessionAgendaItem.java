package com.dmtool.dashboard.session.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "session_agenda_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SessionAgendaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content; // Renamed from 'description'

    @Column(name = "dm_only", nullable = false)
    private boolean dmOnly; // Renamed from 'playerVisible'

    @Column(name = "sort_oder")
    private Integer order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private Session session;
}
