package com.dmtool.dashboard.srd.srdmetadata.service;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassFeaturesDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassLevelDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassSpellsDto;

import java.util.List;
import java.util.Optional;

public interface SrdClassMetadataService {
    // New endpoints for class-level metadata:
    Optional<List<SrdClassLevelDto>> getClassLevels(String index, String subclass);
    Optional<SrdClassLevelDto> getClassLevel(String index, int classLevel);
    Optional<SrdClassFeaturesDto> getClassFeatures(String index, int classLevel);
    Optional<SrdClassSpellsDto> getClassSpells(String index, int spellLevel);
}
