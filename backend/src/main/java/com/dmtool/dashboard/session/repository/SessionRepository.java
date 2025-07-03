package com.dmtool.dashboard.session.repository;

import com.dmtool.dashboard.session.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByCampaignId(Long campaignId);
    Optional<Session> findByIdAndCampaignId(Long id, Long campaignId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Session s WHERE s.id = :id AND s.campaign.id = :campaignId")
    void deleteByIdAndCampaignId(@Param("id") Long id, @Param("campaignId") Long campaignId);


    boolean existsByIdAndCampaignId(Long id, Long campaignId);
}
