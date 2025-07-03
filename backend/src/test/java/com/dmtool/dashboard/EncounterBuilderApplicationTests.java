package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.encounterbuilder.dto.*;
import com.dmtool.dashboard.encounterbuilder.repository.EncounterRepository;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.repository.SessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EncounterBuilderApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Session testSession;

    @Autowired
    private CampaignRepository campaignRepository;  // Inject this if not already

    @BeforeEach
    void setup() {
        // Create a dummy campaign for testing
        Campaign testCampaign = Campaign.builder()
                .title("Test Campaign")
                .description("Dummy campaign for encounter tests")
                .build();
        testCampaign = campaignRepository.saveAndFlush(testCampaign);

        // Create a test session with the dummy campaign
        testSession = Session.builder()
                .title("Test Session")
                .sessionDate(LocalDateTime.now())
                .campaign(testCampaign)
                .build();
        testSession = sessionRepository.saveAndFlush(testSession);
    }


    @Test
    void shouldCreateEncounterWithCombatants() throws Exception {
        CombatantDto combatant1 = new CombatantDto(
                null,                                      // id
                "Goblin x2",                               // name
                7,                                         // hp
                50,                                        // xp
                0.25,                                      // cr
                10,                                        // initiative
                true,                                      // isTemplate
                15,                                        // ac (example default)
                2,                                         // quantity (example default)
                true,                                      // legendary (example default based on special abilities)
                List.of("Scimitar Attack", "Shortbow Attack"),  // actions (or empty list if not needed)
                List.of("Nimble Escape")                   // specialAbilities (or empty list)
        );
        CombatantDto combatant2 = new CombatantDto(
                null,                  // id
                "Orc",                 // name
                15,                    // hp
                100,                   // xp
                0.5,                   // cr
                15,                    // initiative
                false,                 // isTemplate
                13,                    // ac (typical Orc AC)
                1,                     // quantity (single Orc)
                false,                 // legendary (Orcs usually aren't legendary)
                List.of("Greataxe Attack", "Javelin Throw"),  // actions
                List.of("Aggressive")  // specialAbilities (Orc's Aggressive trait)
        );
        CreateEncounterRequest request = new CreateEncounterRequest(
                "Skirmish at the Ruins",
                "A quick battle between goblins and orcs.",
                testSession.getId(),
                List.of(combatant1, combatant2)
        );

        String response = mockMvc.perform(post("/api/encounters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Skirmish at the Ruins"))
                .andReturn().getResponse().getContentAsString();

        // Verify metrics: total XP (50+100 = 150) and average CR ((0.25+0.5)/2 = 0.375)
        Long encounterId = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(get("/api/encounters/" + encounterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalXP").value(150))
                .andExpect(jsonPath("$.challengeRating").value(0.375));
    }

    @Test
    void shouldGetEncountersBySession() throws Exception {
        CreateEncounterRequest request = new CreateEncounterRequest(
                "Battle at the Bridge",
                "Defend the bridge from invaders.",
                testSession.getId(),
                List.of()  // No combatants for simplicity
        );

        mockMvc.perform(post("/api/encounters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/sessions/" + testSession.getId() + "/encounters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldImportEncounterFromJson() throws Exception {
        String importJson = """
            {
              "name": "Imported Encounter",
              "notes": "Imported via JSON",
              "sessionId": %d,
              "combatants": [
                  {"id": null, "name": "Skeleton", "hp": 13, "xp": 50, "cr": 0.25, "initiative": 12, "isTemplate": false}
              ]
            }
            """.formatted(testSession.getId());

        mockMvc.perform(post("/api/encounters/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(importJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Imported Encounter"));
    }

    @Test
    void shouldDeleteEncounter() throws Exception {
        CreateEncounterRequest request = new CreateEncounterRequest(
                "Temporary Encounter",
                "This encounter will be deleted.",
                testSession.getId(),
                List.of()
        );

        String response = mockMvc.perform(post("/api/encounters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long encounterId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/encounters/" + encounterId))
                .andExpect(status().isNoContent());
    }
}