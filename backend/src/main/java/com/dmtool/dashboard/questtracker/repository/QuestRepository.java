package com.dmtool.dashboard.questtracker.repository;

import com.dmtool.dashboard.questtracker.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findByCampaign_Id(Long campaignId);

    List<Quest> findByLinkedSession_Id(Long sessionId);
}
