package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdSkillDto;

import java.util.Optional;

public interface SrdSkillClient {
    Optional<SrdSkillDto> getSkillByIndex(String index);
}
