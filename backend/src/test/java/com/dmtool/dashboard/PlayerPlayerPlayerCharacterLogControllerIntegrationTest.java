package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.character.dto.CreateCharacterLogRequest;
import com.dmtool.dashboard.character.model.Faction;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlayerPlayerPlayerCharacterLogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Session testSession;

    @BeforeEach
    void setup() {
        // Create a dummy campaign for testing (ensure Campaign has required fields)
        Campaign dummyCampaign = Campaign.builder()
                .title("Dummy Campaign")
                .description("Test campaign")
                .build();
        dummyCampaign = campaignRepository.saveAndFlush(dummyCampaign);

        // Create a test session using the correct field names and assign the dummy campaign
        testSession = Session.builder()
                .title("Test Session")
                .sessionDate(LocalDateTime.now())
                .campaign(dummyCampaign)
                .build();
        testSession = sessionRepository.saveAndFlush(testSession);
    }

    @Test
    void shouldCreatePlayerLog() throws Exception {
        CreateCharacterLogRequest request = new CreateCharacterLogRequest(
                testSession.getId(),
                "Alice",
                "Artemis",
                "Ranger 5",
                Faction.FRIENDLY,
                true,
                0,
                List.of("Longbow", "Cloak of Elvenkind"),
                BigDecimal.valueOf(250.00),
                3,
                5,
                "Played very well!"
        );

        mockMvc.perform(post("/api/playerlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerName").value("Alice"))
                .andExpect(jsonPath("$.characterName").value("Artemis"));
    }

    @Test
    void shouldGetLogsBySession() throws Exception {
        // First, create a log entry using record constructor
        CreateCharacterLogRequest request = new CreateCharacterLogRequest(
                testSession.getId(),
                "Bob",
                "Borin",
                "Cleric 4",
                null, // faction is optional
                false,
                1,
                List.of("Mace of Disruption"),
                BigDecimal.valueOf(100.00),
                2,
                3,
                "Needs improvement."
        );

        mockMvc.perform(post("/api/playerlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Then, fetch logs for the session
        mockMvc.perform(get("/api/sessions/" + testSession.getId() + "/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerName").value("Bob"));
    }

    @Test
    void shouldSearchLogsByPlayerName() throws Exception {
        // Create a log entry for searching
        CreateCharacterLogRequest request = new CreateCharacterLogRequest(
                testSession.getId(),
                "Charlie",
                "Celeste",
                "Wizard 3",
                null,
                false,
                0,
                List.of("Spellbook"),
                BigDecimal.valueOf(50.00),
                1,
                1,
                "Good casting."
        );
        mockMvc.perform(post("/api/playerlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Search for logs with playerName containing "char"
        mockMvc.perform(get("/api/playerlogs/search")
                        .param("playerName", "char"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerName").value("Charlie"));
    }
}
