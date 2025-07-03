package com.dmtool.dashboard.prepitem.repository;

import com.dmtool.dashboard.prepitem.model.PrepItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrepItemRepository extends JpaRepository<PrepItem, Long> {

    // Find prep items linked to a session
    List<PrepItem> findBySession_Id(Long sessionId);

    // Find backlog: prep items for a campaign that are not yet assigned to a session
    List<PrepItem> findByCampaign_IdAndSessionIsNull(Long campaignId);
}
