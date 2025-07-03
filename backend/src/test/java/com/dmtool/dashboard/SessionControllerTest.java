package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.model.*;
import com.dmtool.dashboard.campaign.repository.*;
import com.dmtool.dashboard.session.dto.*;
import com.dmtool.dashboard.session.model.*;
import com.dmtool.dashboard.session.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
@Transactional
@ActiveProfiles("test")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private SessionRepository sessionRepository;

    private SessionCreateDto sessionDto;
    private final Long campaignId = 1L;
    private String BASE_URL = "/api/campaigns/" + campaignId + "/sessions";

    @BeforeEach
    @Transactional
    void setup() {
        Campaign campaign = Campaign.builder()
                .title("Test Campaign")
                .description("Test Description")
                .version(0L)
                .build();

        campaign = campaignRepository.saveAndFlush(campaign);
        Long campaignId = campaign.getId();  // Get the actual generated ID

        // Now use this campaignId to build your base URL
        BASE_URL = "/api/campaigns/" + campaignId + "/sessions";

        sessionDto = new SessionCreateDto(
                "Crypt Entry",
                "Enter the crypts beneath Phlan",
                "Started delving into the tombs...",
                LocalDateTime.now(),
                Session.SessionStatus.DRAFT,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }


    @Test
    void shouldCreateSession() throws Exception {
        // Use the dynamically built baseUrl
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Crypt Entry"));
    }


    @Test
    void shouldReturnSessionsList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateSession() throws Exception {
        // Create first
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andReturn().getResponse().getContentAsString();

        SessionDto created = objectMapper.readValue(response, SessionDto.class);

        SessionUpdateDto update = new SessionUpdateDto(
                "Updated Title",
                "New objective",
                "Updated summary",
                LocalDateTime.now(),
                Session.SessionStatus.ACTIVE,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        mockMvc.perform(put(BASE_URL + "/" + created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteSession() throws Exception {
        // Create first
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andReturn().getResponse().getContentAsString();

        SessionDto created = objectMapper.readValue(response, SessionDto.class);

        mockMvc.perform(delete(BASE_URL + "/" + created.id()))
                .andExpect(status().isNoContent());
    }
}