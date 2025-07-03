package com.dmtool.dashboard.srd.srdclass.service;

import com.dmtool.dashboard.srd.srdclass.client.SrdClassClient;
import lombok.RequiredArgsConstructor;
import com.dmtool.dashboard.srd.srdclass.dto.SrdClassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdMulticlassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdSpellcastingDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassDataService {

    private static final Logger log = LoggerFactory.getLogger(ClassDataService.class);
//    private final SrdClassClient srdClassClient;
    private final SrdClassClient srdClassClient;

    public Mono<Optional<SrdClassDto>> getClassData(String classIndex) {
        if (!isValidClassIndex(classIndex)) {
            log.warn("Invalid class index provided: {}", classIndex);
            return Mono.just(Optional.empty());
        }
        return srdClassClient.getClassByIndex(classIndex);
    }

    public Mono<Optional<SrdSpellcastingDto>> getSpellcastingData(String classIndex) {
        if (!isValidClassIndex(classIndex)) {
            log.warn("Invalid class index provided: {}", classIndex);
            return Mono.just(Optional.empty());
        }
        return srdClassClient.getSpellcasting(classIndex);
    }

    public Mono<Optional<SrdMulticlassDto>> getMulticlassingData(String classIndex) {
        if (!isValidClassIndex(classIndex)) {
            log.warn("Invalid class index provided: {}", classIndex);
            return Mono.just(Optional.empty());
        }
        return srdClassClient.getMulticlassing(classIndex);
    }

    private boolean isValidClassIndex(String index) {
        // Validate against the official 12 classes (example list)
        return switch (index.toLowerCase()) {
            case "barbarian", "bard", "cleric", "druid", "fighter", "monk",
                 "paladin", "ranger", "rogue", "sorcerer", "warlock", "wizard" -> true;
            default -> false;
        };
    }
}