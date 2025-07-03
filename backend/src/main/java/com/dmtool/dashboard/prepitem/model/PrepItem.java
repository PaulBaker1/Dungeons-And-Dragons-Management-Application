package com.dmtool.dashboard.prepitem.model;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.session.model.Session;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prep_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrepItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrepItemType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrepStatus status;

    @Column(name = "visible_to_players", nullable = false)
    private boolean visibleToPlayers;

    @Column(name = "is_critical", nullable = false)
    private boolean isCritical;

    @Column(name = "file_url")
    private String fileUrl;

    // Optional association: linked to a session (preferred)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    // Required association: always linked to a campaign
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        // Set default status if not provided
        if (status == null) {
            status = PrepStatus.TODO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
