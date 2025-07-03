package com.dmtool.dashboard.campaign.repository;

import com.dmtool.dashboard.campaign.model.Faction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactionRepository extends JpaRepository<Faction, Long> {
    Optional<Faction> findByName(String name);
}
