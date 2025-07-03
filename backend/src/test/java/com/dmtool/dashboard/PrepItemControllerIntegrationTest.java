package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.prepitem.dto.CreatePrepItemRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepStatusRequest;
import com.dmtool.dashboard.prepitem.dto.UpdatePrepVisibilityRequest;
import com.dmtool.dashboard.prepitem.model.PrepItemType;
import com.dmtool.dashboard.prepitem.model.PrepStatus;
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
class PrepItemControllerIntegrationTest {

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
                .description("Campaign for testing Prep Items")
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
    void shouldCreatePrepItemForSession() throws Exception {
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "Glyph Riddle for Upper Crypt",
                "A challenging riddle inscribed in the tomb glyphs.",
                PrepItemType.PUZZLE,
                true,
                true,
                "https://example.com/assets/glyph.png",
                testSession.getId(),  // linked to session
                testCampaign.getId()
        );

        mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Glyph Riddle for Upper Crypt"))
                .andExpect(jsonPath("$.sessionId").value(testSession.getId().intValue()));
    }

    @Test
    void shouldCreatePrepItemForCampaignOnly() throws Exception {
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "NPC Stat Block Preparation",
                "Prepare stat blocks for the undead faction NPCs.",
                PrepItemType.NPC,
                false,
                false,
                null,
                null,  // no session linked
                testCampaign.getId()
        );

        mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("NPC Stat Block Preparation"))
                .andExpect(jsonPath("$.sessionId").isEmpty());
    }

    @Test
    void shouldGetPrepItemsBySession() throws Exception {
        // Create a prep item for the session
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "Map Layout for Crypt",
                "Draft an initial map layout of the crypt area.",
                PrepItemType.MAP,
                true,
                false,
                null,
                testSession.getId(),
                testCampaign.getId()
        );

        mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Retrieve prep items for the session
        mockMvc.perform(get("/api/sessions/" + testSession.getId() + "/prepitems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Map Layout for Crypt"));
    }

    @Test
    void shouldGetPrepItemsBacklogByCampaign() throws Exception {
        // Create two prep items without session assignment
        CreatePrepItemRequest request1 = new CreatePrepItemRequest(
                "Lore Handout for Tomb History",
                "Compile lore details for tomb inscriptions.",
                PrepItemType.LORE,
                false,
                false,
                null,
                null,
                testCampaign.getId()
        );
        CreatePrepItemRequest request2 = new CreatePrepItemRequest(
                "Ambient Sound for Crypt",
                "Select eerie ambient sound for crypt exploration.",
                PrepItemType.SOUND,
                true,
                false,
                null,
                null,
                testCampaign.getId()
        );

        mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());

        // Retrieve backlog (prep items not assigned to any session)
        mockMvc.perform(get("/api/campaigns/" + testCampaign.getId() + "/prepitems/backlog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdatePrepItemStatus() throws Exception {
        // Create a prep item
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "Prepare Handout for Faction Reaction",
                "Draft a handout detailing faction reaction based on moral choices.",
                PrepItemType.HANDOUT,
                true,
                false,
                null,
                testSession.getId(),
                testCampaign.getId()
        );

        String response = mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long prepItemId = objectMapper.readTree(response).get("id").asLong();

        // Update status to READY
        UpdatePrepStatusRequest statusUpdate = new UpdatePrepStatusRequest(PrepStatus.READY);

        mockMvc.perform(patch("/api/prepitems/" + prepItemId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY"));
    }

    @Test
    void shouldUpdatePrepItemVisibility() throws Exception {
        // Create a prep item
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "Prepare Puzzle Clue",
                "Design a puzzle clue for the crypt door.",
                PrepItemType.PUZZLE,
                false,
                true,
                null,
                testSession.getId(),
                testCampaign.getId()
        );

        String response = mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long prepItemId = objectMapper.readTree(response).get("id").asLong();

        // Toggle visibility to true
        UpdatePrepVisibilityRequest visibilityUpdate = new UpdatePrepVisibilityRequest(true);

        mockMvc.perform(patch("/api/prepitems/" + prepItemId + "/visibility")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visibilityUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visibleToPlayers").value(true));
    }

    @Test
    void shouldDeletePrepItem() throws Exception {
        // Create a prep item
        CreatePrepItemRequest request = new CreatePrepItemRequest(
                "Delete This Prep",
                "This prep item will be deleted.",
                PrepItemType.MISC,
                false,
                false,
                null,
                null,
                testCampaign.getId()
        );

        String response = mockMvc.perform(post("/api/prepitems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long prepItemId = objectMapper.readTree(response).get("id").asLong();

        // Delete the prep item
        mockMvc.perform(delete("/api/prepitems/" + prepItemId))
                .andExpect(status().isNoContent());
    }
}
