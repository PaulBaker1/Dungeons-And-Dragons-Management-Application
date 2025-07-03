package com.dmtool.dashboard.srd.srdmetadata.controller;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassFeaturesDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassLevelDto;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassSpellsDto;
import com.dmtool.dashboard.srd.srdmetadata.service.SrdClassMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/external/srd")
public class SrdClassMetadataController {

    private final SrdClassMetadataService metadataService;

    public SrdClassMetadataController(SrdClassMetadataService srdClassMetadataService) {
        this.metadataService = srdClassMetadataService;
    }

    // Endpoint for "Get all level resources for a class"
    @GetMapping("/classes/{index}/levels")
    public ResponseEntity<List<SrdClassLevelDto>> getClassLevels(
            @PathVariable String index,
            @RequestParam(required = false) String subclass) {
        Optional<List<SrdClassLevelDto>> levels = metadataService.getClassLevels(index, subclass);
        return levels.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint for "Get level resource for a class and level"
    @GetMapping("/classes/{index}/levels/{class_level}")
    public ResponseEntity<SrdClassLevelDto> getClassLevel(
            @PathVariable String index,
            @PathVariable("class_level") int classLevel) {
        Optional<SrdClassLevelDto> level = metadataService.getClassLevel(index, classLevel);
        return level.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint for "Get features available to a class at the requested level"
    @GetMapping("/classes/{index}/levels/{class_level}/features")
    public ResponseEntity<SrdClassFeaturesDto> getClassFeatures(
            @PathVariable String index,
            @PathVariable("class_level") int classLevel) {
        Optional<SrdClassFeaturesDto> features = metadataService.getClassFeatures(index, classLevel);
        return features.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint for "Get spells of the requested level available to the class"
    @GetMapping("/classes/{index}/spells/{spell_level}")
    public ResponseEntity<SrdClassSpellsDto> getClassSpells(
            @PathVariable String index,
            @PathVariable("spell_level") int spellLevel) {
        Optional<SrdClassSpellsDto> spells = metadataService.getClassSpells(index, spellLevel);
        return spells.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
