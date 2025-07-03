package com.dmtool.dashboard.session.model;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.character.model.PlayerCharacter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank // Validation constraint
    private String title;

    @Column(length = 1000)
    private String objective;

    @Column(length = 4000)
    private String summary;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column
    private LocalDateTime sessionDate;

    @JsonIgnoreProperties("sessions")
    @ManyToOne(optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionAgendaItem> agenda = new ArrayList<>(); // ✅ alias: getAgendaItems()

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionEncounter> encounters = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionNote> notes = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionDecision> decisions = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyItemTracker> keyItems = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ✅ Optional alias for backward compatibility
    public List<SessionAgendaItem> getAgendaItems() {
        return agenda;
    }

    public enum SessionStatus {
        DRAFT, ACTIVE, COMPLETED, ARCHIVED
    }

    @ManyToOne
    @JoinColumn(name = "character_id", referencedColumnName = "id") // Ensure the column name matches
    private PlayerCharacter playerCharacter;

    @Column(columnDefinition = "TEXT")
    private String upcomingNotes;

    @Column(columnDefinition = "TEXT")
    private String pastNotes;

    @Column(columnDefinition = "TEXT")
    private String attendanceNotes;


}
