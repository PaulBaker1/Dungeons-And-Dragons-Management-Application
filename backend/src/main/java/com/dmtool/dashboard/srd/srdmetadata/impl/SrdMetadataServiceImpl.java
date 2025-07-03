package com.dmtool.dashboard.srd.srdmetadata.impl;

import com.dmtool.dashboard.srd.srdmetadata.client.*;
import com.dmtool.dashboard.srd.srdmetadata.dto.*;
import com.dmtool.dashboard.srd.srdmetadata.service.SrdMetadataService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SrdMetadataServiceImpl implements SrdMetadataService {


    private final SrdAbilityScoreClient abilityScoreClient;
    private final SrdAlignmentClient alignmentClient;
    private final SrdBackgroundClient backgroundClient;
    private final SrdLanguageClient languageClient;
    private final SrdSkillClient skillClient;
    private final SrdProficiencyClient proficiencyClient;


    public SrdMetadataServiceImpl(
            SrdAbilityScoreClient abilityScoreClient,
            SrdAlignmentClient alignmentClient,
            SrdBackgroundClient backgroundClient,
            SrdLanguageClient languageClient,
            SrdSkillClient skillClient,
            SrdProficiencyClient proficiencyClient) {
        this.abilityScoreClient = abilityScoreClient;
        this.alignmentClient = alignmentClient;
        this.backgroundClient = backgroundClient;
        this.languageClient = languageClient;
        this.skillClient = skillClient;
        this.proficiencyClient = proficiencyClient;
    }

    @Override
    public Optional<SrdAbilityDto> getAbility(String index) {
        return abilityScoreClient.getAbilityByIndex(index);
    }

    @Override
    public Optional<SrdAlignmentDto> getAlignment(String index) {
        return alignmentClient.getAlignmentByIndex(index);
    }

    @Override
    public Optional<SrdBackgroundDto> getBackground(String index) {
        return backgroundClient.getBackgroundByIndex(index);
    }

    @Override
    public Optional<SrdLanguageDto> getLanguage(String index) {
        return languageClient.getLanguageByIndex(index);
    }

    @Override
    public Optional<SrdSkillDto> getSkill(String index) {
        return skillClient.getSkillByIndex(index);
    }

    @Override
    public Optional<SrdProficiencyDto> getProficiency(String index) {
        return proficiencyClient.getProficiencyByIndex(index);
    }
}