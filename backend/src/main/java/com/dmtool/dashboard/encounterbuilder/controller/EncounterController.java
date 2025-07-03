//package com.dmtool.dashboard.encounterbuilder.controller;
//
//import com.dmtool.dashboard.encounterbuilder.dto.CreateEncounterRequest;
//import com.dmtool.dashboard.encounterbuilder.dto.EncounterDto;
//import com.dmtool.dashboard.encounterbuilder.service.EncounterService;
//import com.dmtool.dashboard.srd.srdmonster.service.MonsterImportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class EncounterController {
//    private final EncounterService encounterService;
//
//    private final MonsterImportService monsterImportService;
//
//    @PostMapping("/encounters")
//    @ResponseStatus(HttpStatus.CREATED)
//    public EncounterDto createEncounter(@RequestBody CreateEncounterRequest request) {
//        return encounterService.createEncounter(request);
//    }
//
//    @GetMapping("/encounters/{id}")
//    public EncounterDto getEncounterById(@PathVariable Long id) {
//        return encounterService.getEncounterById(id);
//    }
//
//    @GetMapping("/sessions/{sessionId}/encounters")
//    public List<EncounterDto> getEncountersBySession(@PathVariable Long sessionId) {
//        return encounterService.getEncountersBySession(sessionId);
//    }
//
//    @PostMapping("/encounters/import")
//    public EncounterDto importEncounter(@RequestBody String jsonPayload) {
//        return encounterService.importEncounter(jsonPayload);
//    }
//
//    @DeleteMapping("/encounters/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteEncounter(@PathVariable Long id) {
//        encounterService.deleteEncounter(id);
//    }
//
//    @PostMapping("/encounters/{encounterId}/import-monster")
//    public EncounterDto importMonster(
//            @PathVariable Long encounterId,
//            @RequestParam String index,
//            @RequestParam(defaultValue = "1") int quantity) {
//        return monsterImportService.importMonsterFromSrd(encounterId, index, quantity);
//    }
//
//}
