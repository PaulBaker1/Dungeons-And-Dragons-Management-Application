package com.dmtool.dashboard;

import com.dmtool.dashboard.srd.srdclass.dto.SrdClassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdMulticlassDto;
import com.dmtool.dashboard.srd.srdclass.dto.SrdSpellcastingDto;
import com.dmtool.dashboard.srd.srdclass.service.ClassDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class SrdClassControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ClassDataService classDataService() {
            return Mockito.mock(ClassDataService.class);
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    private WebTestClient webTestClient;

    @org.springframework.beans.factory.annotation.Autowired
    private ClassDataService classDataService;

    private SrdClassDto sampleClassDto;
    private SrdSpellcastingDto sampleSpellcastingDto;
    private SrdMulticlassDto sampleMulticlassDto;

    @BeforeEach
    public void setup() {
        sampleClassDto = new SrdClassDto(
                "wizard",
                "Wizard",
                "d6",
                java.util.List.of("Arcana", "History"),
                java.util.List.of("Intelligence", "Wisdom"),
                "School of Evocation"
        );

        sampleSpellcastingDto = new SrdSpellcastingDto(
                "1",
                "Intelligence",
                java.util.List.of("Fire Bolt", "Mage Hand"),
                java.util.List.of("Shield", "Magic Missile")
        );

        sampleMulticlassDto = new SrdMulticlassDto(
                "Prerequisite: 13 Intelligence",
                "Proficiencies: Light armor, simple weapons",
                "Bonus: +1 to spell slots"
        );
    }

    @Test
    void testGetClassData() {
        when(classDataService.getClassData("wizard"))
                .thenReturn(Mono.just(Optional.of(sampleClassDto)));

        webTestClient.get()
                .uri("/api/external/srd/classes/wizard")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Wizard")
                .jsonPath("$.hit_die").isEqualTo("d6");
    }

    @Test
    void testGetSpellcastingData() {
        when(classDataService.getSpellcastingData("wizard"))
                .thenReturn(Mono.just(Optional.of(sampleSpellcastingDto)));

        webTestClient.get()
                .uri("/api/external/srd/classes/wizard/spellcasting")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.spellcasting_ability").isEqualTo("Intelligence")
                .jsonPath("$.level").isEqualTo("1");
    }

    @Test
    void testGetMulticlassingData() {
        when(classDataService.getMulticlassingData("wizard"))
                .thenReturn(Mono.just(Optional.of(sampleMulticlassDto)));

        webTestClient.get()
                .uri("/api/external/srd/classes/wizard/multiclassing")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.prerequisites").isEqualTo("Prerequisite: 13 Intelligence")
                .jsonPath("$.multiclassing_bonus").isEqualTo("Bonus: +1 to spell slots");
    }

    @Test
    void testInvalidClassIndex() {
        when(classDataService.getClassData("invalid"))
                .thenReturn(Mono.just(Optional.empty()));

        webTestClient.get()
                .uri("/api/external/srd/classes/invalid")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("null");
    }
}