package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassSpellsDto;

import java.util.Optional;

public interface SrdClassSpellsClient {
    Optional<SrdClassSpellsDto> getClassSpells(String index, int spellLevel);
}
