package com.dmtool.dashboard.character.repository;

import com.dmtool.dashboard.character.model.PlayerCharacterLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerCharacterLogRepository extends JpaRepository<PlayerCharacterLog, Long> {
    List<PlayerCharacterLog> findBySession_Campaign_Id(Long campaignId);
    List<PlayerCharacterLog> findBySession_Id(Long sessionId);
    List<PlayerCharacterLog> findByPlayerNameIgnoreCaseContaining(String playerName);
}
