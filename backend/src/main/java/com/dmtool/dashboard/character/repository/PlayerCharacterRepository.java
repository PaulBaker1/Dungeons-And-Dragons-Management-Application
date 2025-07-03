package com.dmtool.dashboard.character.repository;

import com.dmtool.dashboard.character.model.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    List<PlayerCharacter> findByCampaignId(Long campaignId);

    PlayerCharacter findCharacterById(Long id);

    List<PlayerCharacter> findByCampaignIsNull();
}
