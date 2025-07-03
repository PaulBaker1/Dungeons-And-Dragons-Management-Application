package com.dmtool.dashboard.encounterbuilder.repository;


import com.dmtool.dashboard.encounterbuilder.model.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    List<Encounter> findBySession_Id(Long sessionId);
}
