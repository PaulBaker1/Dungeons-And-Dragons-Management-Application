package com.dmtool.dashboard.dndapi.service;


import com.dmtool.dashboard.dndapi.dto.backroundDto.BackgroundDTO;
import com.dmtool.dashboard.dndapi.dto.classDto.ClassDTO;
import com.dmtool.dashboard.dndapi.dto.equipmentDto.EquipmentDTO;
import com.dmtool.dashboard.dndapi.dto.languageDto.LanguageDTO;
import com.dmtool.dashboard.dndapi.dto.magicItemDTO.MagicItemDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.MonsterDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterDto;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterListResponse;
import com.dmtool.dashboard.dndapi.dto.proficiencyDTO.ProficiencyDTO;
import com.dmtool.dashboard.dndapi.dto.raceDTO.RaceDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellListDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellSummaryDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

// Inside DnD5eApiService.java

public interface DnD5eApiService {
    Mono<SpellDTO> getSpell(String index);
    Mono<MonsterDTO> getMonster(String index);
    Mono<BackgroundDTO> getBackground(String index);
    Mono<RaceDTO> getRace(String index);
    Mono<ClassDTO> getClassByIndex(String index);
    Mono<EquipmentDTO> getEquipment(String index);
    Mono<LanguageDTO> getLanguage(String index);
    Mono<ProficiencyDTO> getProficiency(String index);
    Mono<MagicItemDTO> getMagicItem(String index);
    Mono<Map> getAll(String type);
    Mono<Map<String, Object>> getAllSrdData();
    Mono<SpellListDTO> getSpellsByClass(String className);
    Mono<SpellSummaryDTO> getSpellSummary(String index);
    Mono<List<RaceDTO>> getAllRaces();
    Mono<List<SrdMonsterDto>> getAllMonsters();
    Mono<List<EquipmentDTO>> getAllEquipment();
}

