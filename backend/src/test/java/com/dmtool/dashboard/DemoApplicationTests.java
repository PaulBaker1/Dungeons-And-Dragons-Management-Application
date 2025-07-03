package com.dmtool.dashboard;

import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.session.dto.*;
import com.dmtool.dashboard.session.mapper.SessionMapper;
import com.dmtool.dashboard.session.model.Session;
import com.dmtool.dashboard.session.model.SessionNote;
import com.dmtool.dashboard.session.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }
}
