package com.dmtool.dashboard.dndapi.controller;

import com.dmtool.dashboard.dndapi.dto.backroundDto.BackgroundDTO;
import com.dmtool.dashboard.dndapi.dto.classDto.ClassDTO;
import com.dmtool.dashboard.dndapi.dto.equipmentDto.EquipmentDTO;
import com.dmtool.dashboard.dndapi.dto.languageDto.LanguageDTO;
import com.dmtool.dashboard.dndapi.dto.magicItemDTO.MagicItemDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.MonsterDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterDto;
import com.dmtool.dashboard.dndapi.dto.proficiencyDTO.ProficiencyDTO;
import com.dmtool.dashboard.dndapi.dto.raceDTO.RaceDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellListDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellSummaryDTO;
import com.dmtool.dashboard.dndapi.service.DnD5eApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dnd")
public class DnD5eController {

    private final DnD5eApiService dndService;
    private final ObjectMapper objectMapper;

    public DnD5eController(DnD5eApiService dndService, ObjectMapper objectMapper) {
        this.dndService = dndService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/spells/{index}")
    public Mono<SpellDTO> getSpell(@PathVariable String index) {
        return dndService.getSpell(index);
    }

    @GetMapping("/monsters/{index}")
    public Mono<MonsterDTO> getMonster(@PathVariable String index) {
        return dndService.getMonster(index);
    }

    @GetMapping("/monsters")
    public Mono<List<SrdMonsterDto>> getMonsters() {
        return dndService.getAllMonsters();
    }


    @GetMapping("/backgrounds/{index}")
    public Mono<BackgroundDTO> getBackground(@PathVariable String index) {
        return dndService.getBackground(index);
    }

    @GetMapping("/races/{index}")
    public Mono<RaceDTO> getRace(@PathVariable String index) {
        return dndService.getRace(index);
    }

    @GetMapping("/classes/{index}")
    public Mono<ClassDTO> getClass(@PathVariable String index) {
        return dndService.getClassByIndex(index);
    }

    @GetMapping("/equipment")
    public Mono<List<EquipmentDTO>> getAllEquipment() {
        return dndService.getAllEquipment();
    }


    @GetMapping("/equipment/{index}")
    public Mono<EquipmentDTO> getEquipment(@PathVariable String index) {
        return dndService.getEquipment(index);
    }

    @GetMapping("/languages/{index}")
    public Mono<LanguageDTO> getLanguage(@PathVariable String index) {
        return dndService.getLanguage(index);
    }

    @GetMapping("/proficiencies/{index}")
    public Mono<ProficiencyDTO> getProficiency(@PathVariable String index) {
        return dndService.getProficiency(index);
    }

    @GetMapping("/magic-items/{index}")
    public Mono<MagicItemDTO> getMagicItem(@PathVariable String index) {
        return dndService.getMagicItem(index);
    }

    @GetMapping("/list/{type}")
    public Mono<Map> getList(@PathVariable String type) {
        return dndService.getAll(type);
    }

    @GetMapping("/fetch-all")
    public Mono<Map<String, Object>> getAllSrdData() {
        return dndService.getAllSrdData();
    }

    @GetMapping("/classes/{className}/spells")
    public Mono<SpellListDTO> getSpellsForClass(@PathVariable String className) {
        return dndService.getSpellsByClass(className);
    }
    @GetMapping("/spells")
    public Mono<SpellListDTO> getAllSpells() {
        return dndService.getAll("spells")
                .map(result -> objectMapper.convertValue(result, SpellListDTO.class)); // ⬅️ here
    }

    @GetMapping("/spells/full")
    public Flux<SpellDTO> getAllSpellsFull() {
        return dndService.getAll("spells")
                .flatMapMany(result -> {
                    List<Map<String, String>> results = (List<Map<String, String>>) result.get("results");
                    return Flux.fromIterable(results)
                            .flatMap(spell -> dndService.getSpell(spell.get("index")));
                });
    }

    @GetMapping("/spells/summary")
    public Flux<SpellSummaryDTO> getAllSpellSummaries() {
        return dndService.getAll("spells")
                .flatMapMany(result -> {
                    List<Map<String, String>> results = (List<Map<String, String>>) result.get("results");
                    return Flux.fromIterable(results)
                            .flatMap(spell -> dndService.getSpellSummary(spell.get("index")));
                });
    }

    @GetMapping("/races")
    public Mono<List<RaceDTO>> getAllRaces() {
        return dndService.getAllRaces();
    }
}
