package com.dmtool.dashboard.campaign.service;

import com.dmtool.dashboard.campaign.dto.CampaignCreateDTO;
import com.dmtool.dashboard.campaign.dto.CampaignDetailsDTO;
import com.dmtool.dashboard.campaign.dto.CampaignSummaryDTO;
import com.dmtool.dashboard.campaign.dto.CampaignUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CampaignService {
    List<CampaignSummaryDTO> getAll(Pageable pageable);

    List<CampaignSummaryDTO> getByTag(String tag, Pageable pageable);

    CampaignDetailsDTO create(CampaignCreateDTO dto);

    CampaignDetailsDTO pin(Long id);

    CampaignDetailsDTO update(Long id, CampaignUpdateDTO dto );

    CampaignDetailsDTO togglePinned(Long id);

    CampaignDetailsDTO archive(Long id);

    CampaignDetailsDTO setLastSessionDate(Long id, LocalDate date);

    CampaignDetailsDTO getDetails(Long id);

    void delete(Long id);

    Map<String, Object> getStats();
}