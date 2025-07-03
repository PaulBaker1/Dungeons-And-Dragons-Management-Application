package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.questtracker.dto.CreateQuestRequest;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestStatusRequest;
import com.dmtool.dashboard.questtracker.dto.UpdateQuestVisibilityRequest;
import com.dmtool.dashboard.questtracker.model.QuestPriority;
import com.dmtool.dashboard.questtracker.model.QuestStatus;
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
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Campaign testCampaign;
    private Session testSession;

    @BeforeEach
    void setup() {
        // Create a test campaign
        testCampaign = Campaign.builder()
                .title("Test Campaign")
                .description("Campaign for testing Quest Tracker")
                .build();
        testCampaign = campaignRepository.saveAndFlush(testCampaign);

        // Create a test session linked to the campaign
        testSession = Session.builder()
                .title("Test Session")
                .sessionDate(LocalDateTime.now())
                .campaign(testCampaign)
                .build();
        testSession = sessionRepository.saveAndFlush(testSession);
    }

    @Test
    void shouldCreateQuest() throws Exception {
        CreateQuestRequest request = new CreateQuestRequest(
                testCampaign.getId(),
                "Recover the Bone Relic",
                "Retrieve the artifact from the lower catacombs.",
                Set.of("undead", "lore", "artifact"),
                QuestPriority.HIGH,
                false,
                testSession.getId()
        );

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Recover the Bone Relic"))
                .andExpect(jsonPath("$.linkedSessionId").value(testSession.getId().intValue()));
    }

    @Test
    void shouldUpdateQuestStatus() throws Exception {
        CreateQuestRequest createRequest = new CreateQuestRequest(
                testCampaign.getId(),
                "Find the Glyph Key",
                "Search for the key hidden in ancient tombs.",
                Set.of("glyph", "puzzle"),
                QuestPriority.MEDIUM,
                true,
                testSession.getId()
        );

        String response = mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long questId = objectMapper.readTree(response).get("id").asLong();

        UpdateQuestStatusRequest statusUpdate = new UpdateQuestStatusRequest(QuestStatus.ACTIVE);

        mockMvc.perform(patch("/api/quests/" + questId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldUpdateQuestVisibility() throws Exception {
        CreateQuestRequest createRequest = new CreateQuestRequest(
                testCampaign.getId(),
                "Secret Faction Quest",
                "A quest only the DM should see.",
                Set.of("faction:welcomers"),
                QuestPriority.CRITICAL,
                false,
                testSession.getId()
        );

        String response = mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long questId = objectMapper.readTree(response).get("id").asLong();

        UpdateQuestVisibilityRequest visibilityUpdate = new UpdateQuestVisibilityRequest(true);

        mockMvc.perform(patch("/api/quests/" + questId + "/visibility")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visibilityUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visibleToPlayers").value(true));
    }

    @Test
    void shouldGetQuestsByCampaign() throws Exception {
        // Create two quests for the campaign
        CreateQuestRequest request1 = new CreateQuestRequest(
                testCampaign.getId(),
                "Quest One",
                "First quest description.",
                Set.of("tag1"),
                QuestPriority.LOW,
                true,
                null
        );
        CreateQuestRequest request2 = new CreateQuestRequest(
                testCampaign.getId(),
                "Quest Two",
                "Second quest description.",
                Set.of("tag2"),
                QuestPriority.MEDIUM,
                false,
                testSession.getId()
        );

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/campaigns/" + testCampaign.getId() + "/quests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldDeleteQuest() throws Exception {
        CreateQuestRequest request = new CreateQuestRequest(
                testCampaign.getId(),
                "Obsolete Quest",
                "This quest will be deleted.",
                Set.of("obsolete"),
                QuestPriority.MEDIUM,
                true,
                null
        );

        String response = mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long questId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/quests/" + questId))
                .andExpect(status().isNoContent());
    }
}
