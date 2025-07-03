package com.dmtool.dashboard.srd.srdmetadata.controller;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdAbilityDto;
import com.dmtool.dashboard.srd.srdmetadata.service.SrdMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/external/srd")
public class SrdMetadataController {

    private final SrdMetadataService metadataService;

    public SrdMetadataController(SrdMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/ability")
    public ResponseEntity<SrdAbilityDto> getAbility(@RequestParam String index) {
        Optional<SrdAbilityDto> ability = metadataService.getAbility(index);
        return ability.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
