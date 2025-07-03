package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.encounterbuilder.model.Encounter;
import com.dmtool.dashboard.encounterbuilder.repository.EncounterRepository;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import com.dmtool.dashboard.srd.srdmonster.client.SrdMonsterClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EncounterMonsterImportIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SrdMonsterClient srdMonsterClient;

    private Session testSession;
    private Encounter testEncounter;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SrdMonsterClient srdMonsterClient() {
            return org.mockito.Mockito.mock(SrdMonsterClient.class);
        }
    }

    @BeforeEach
    void setup() {
        // Create a dummy campaign for the test.
        Campaign campaign = Campaign.builder()
                .title("Test Campaign")
                .description("Dummy campaign for monster import test")
                .build();
        campaign = campaignRepository.saveAndFlush(campaign);

        // Create a test session associated with the campaign.
        testSession = Session.builder()
                .title("Test Session")
                .sessionDate(LocalDateTime.now())
                .campaign(campaign)
                .build();
        testSession = sessionRepository.saveAndFlush(testSession);

        // Create a test encounter associated with the session.
        testEncounter = Encounter.builder()
                .name("Test Encounter")
                .notes("Encounter for monster import test")
                .session(testSession)
                .build();
        testEncounter = encounterRepository.saveAndFlush(testEncounter);
    }

    @Test
    void shouldImportMonsterIntoEncounter() throws Exception {
        // Prepare a sample SRD monster DTO to return when the client is called.
        SrdMonsterDto srdMonster = new SrdMonsterDto(
                "goblin",
                "Goblin",
                0.25,
                7,
                15,
                50,
                List.of("Scimitar Attack", "Shortbow Attack"),
                List.of("Nimble Escape")
        );
        when(srdMonsterClient.getByIndex("goblin")).thenReturn(Optional.of(srdMonster));

        // Invoke the endpoint to import a monster into the encounter.
        mockMvc.perform(post("/api/encounters/" + testEncounter.getId() + "/import-monster")
                        .param("index", "goblin")
                        .param("quantity", "2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                // Check that the imported combatant has the expected values.
                .andExpect(jsonPath("$.combatants[0].name").value("Goblin x2"))
                .andExpect(jsonPath("$.combatants[0].ac").value(15))
                .andExpect(jsonPath("$.combatants[0].xp").value(50))
                .andExpect(jsonPath("$.combatants[0].legendary").value(true));
    }
}