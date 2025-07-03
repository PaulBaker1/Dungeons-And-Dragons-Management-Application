package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.*;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassFeaturesDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassLevelDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassSpellsDto;
import com.dmtool.dashboard.srd.srdmetadata.service.SrdClassMetadataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SrdClassMetadataServiceImpl implements SrdClassMetadataService {
    // Previously defined clients for abilities, alignments, etc.
    private final SrdAbilityScoreClient abilityScoreClient;
    private final SrdAlignmentClient alignmentClient;
    private final SrdBackgroundClient backgroundClient;
    private final SrdLanguageClient languageClient;
    private final SrdSkillClient skillClient;
    private final SrdProficiencyClient proficiencyClient;

    // New clients for class-level endpoints
    private final SrdClassLevelClient classLevelClient;
    private final SrdClassFeaturesClient classFeaturesClient;
    private final SrdClassSpellsClient classSpellsClient;

    public SrdClassMetadataServiceImpl(
            SrdAbilityScoreClient abilityScoreClient,
            SrdAlignmentClient alignmentClient,
            SrdBackgroundClient backgroundClient,
            SrdLanguageClient languageClient,
            SrdSkillClient skillClient,
            SrdProficiencyClient proficiencyClient,
            SrdClassLevelClient classLevelClient,
            SrdClassFeaturesClient classFeaturesClient,
            SrdClassSpellsClient classSpellsClient) {
        this.abilityScoreClient = abilityScoreClient;
        this.alignmentClient = alignmentClient;
        this.backgroundClient = backgroundClient;
        this.languageClient = languageClient;
        this.skillClient = skillClient;
        this.proficiencyClient = proficiencyClient;
        this.classLevelClient = classLevelClient;
        this.classFeaturesClient = classFeaturesClient;
        this.classSpellsClient = classSpellsClient;
    }

    // Existing methods...

    @Override
    public Optional<List<SrdClassLevelDto>> getClassLevels(String index, String subclass) {
        return classLevelClient.getClassLevels(index, subclass);
    }

    @Override
    public Optional<SrdClassLevelDto> getClassLevel(String index, int classLevel) {
        return classLevelClient.getClassLevel(index, classLevel);
    }

    @Override
    public Optional<SrdClassFeaturesDto> getClassFeatures(String index, int classLevel) {
        return classFeaturesClient.getClassFeatures(index, classLevel);
    }

    @Override
    public Optional<SrdClassSpellsDto> getClassSpells(String index, int spellLevel) {
        return classSpellsClient.getClassSpells(index, spellLevel);
    }
}

