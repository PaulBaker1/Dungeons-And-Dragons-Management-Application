package com.dmtool.dashboard.campaign.service;

import com.dmtool.dashboard.campaign.dto.CampaignCreateDTO;
import com.dmtool.dashboard.campaign.dto.CampaignDetailsDTO;
import com.dmtool.dashboard.campaign.dto.CampaignSummaryDTO;
import com.dmtool.dashboard.campaign.dto.CampaignUpdateDTO;
import com.dmtool.dashboard.campaign.mapper.CampaignMapper;
import com.dmtool.dashboard.campaign.model.Campaign;
import com.dmtool.dashboard.campaign.model.CampaignStatus;
import com.dmtool.dashboard.campaign.model.Tag;
import com.dmtool.dashboard.campaign.repository.CampaignRepository;
import com.dmtool.dashboard.campaign.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository repo;
    private final CampaignMapper mapper;
    private final TagRepository tagRepository; // 🆕 ADD THIS

    @Override
    public List<CampaignSummaryDTO> getAll(Pageable pageable) {
        return repo.findByArchivedFalse(pageable)
                .map(mapper::toSummaryDTO)
                .toList();
    }

    @Override
    public List<CampaignSummaryDTO> getByTag(String tag, Pageable pageable) {
        return repo.findByTagNameContainingIgnoreCaseAndArchivedFalse(tag, pageable)
                .map(mapper::toSummaryDTO)
                .toList();
    }


    @Override
    public CampaignDetailsDTO create(CampaignCreateDTO dto) {
        Campaign campaign = mapper.fromCreateDTO(dto);
        campaign.setStatus(CampaignStatus.DRAFT);
        return mapper.toDetailsDTO(repo.save(campaign));
    }

    @Override
    public CampaignDetailsDTO pin(Long id) {
        Campaign campaign = repo.findById(id).orElseThrow();
        campaign.setPinned(true);
        return mapper.toDetailsDTO(repo.save(campaign));
    }

    // Inside update() before saving
    @Override
    public CampaignDetailsDTO update(Long id, CampaignUpdateDTO dto) {
        Campaign campaign = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        mapper.update(campaign, dto);

        // 🧠 Deduplicate tags here
        if (campaign.getTags() != null) {
            Set<Tag> deduped = campaign.getTags().stream()
                    .map(tag -> tagRepository.findByName(tag.getName()).orElse(tag))
                    .collect(Collectors.toSet());
            campaign.setTags(deduped);
        }

        return mapper.toDetailsDTO(repo.save(campaign));
    }

    @Override
    public CampaignDetailsDTO togglePinned(Long id) {
        Campaign campaign = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));
        campaign.setPinned(!campaign.isPinned());
        return mapper.toDetailsDTO(repo.save(campaign));
    }

    @Override
    public CampaignDetailsDTO archive(Long id) {
        Campaign campaign = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));
        campaign.setArchived(true);
        return mapper.toDetailsDTO(repo.save(campaign));
    }

    @Override
    public CampaignDetailsDTO setLastSessionDate(Long id, LocalDate date) {
        Campaign campaign = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found"));
        campaign.setLastSessionDate(date.atStartOfDay());
        return mapper.toDetailsDTO(repo.save(campaign));
    }

    @Override
    public CampaignDetailsDTO getDetails(Long id) {
        return mapper.toDetailsDTO(repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found")));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        }
        repo.deleteById(id);
    }

    @Override
    public Map<String, Object> getStats() {
        long total = repo.count();
        long archived = repo.countByArchivedTrue();
        long active = total - archived;
        LocalDateTime lastUpdated = repo.findTopByOrderByCreatedAtDesc().map(Campaign::getCreatedAt).orElse(null);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("active", active);
        stats.put("archived", archived);
        stats.put("lastUpdate", lastUpdated);
        return stats;
    }
}
