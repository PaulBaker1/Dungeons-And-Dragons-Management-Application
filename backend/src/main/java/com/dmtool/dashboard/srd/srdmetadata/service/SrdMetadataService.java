package com.dmtool.dashboard.srd.srdmetadata.service;

import com.dmtool.dashboard.srd.srdmetadata.dto.*;

import java.util.List;
import java.util.Optional;

public interface SrdMetadataService {
    Optional<SrdAbilityDto> getAbility(String index);
    Optional<SrdAlignmentDto> getAlignment(String index);
    Optional<SrdBackgroundDto> getBackground(String index);
    Optional<SrdLanguageDto> getLanguage(String index);
    Optional<SrdSkillDto> getSkill(String index);
    Optional<SrdProficiencyDto> getProficiency(String index);
}
