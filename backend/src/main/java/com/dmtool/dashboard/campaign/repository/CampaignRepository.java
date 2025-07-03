package com.dmtool.dashboard.campaign.repository;

import com.dmtool.dashboard.campaign.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    // ❌ This method is still invalid and should be removed or fixed
    // List<Campaign> findByTagsContainingIgnoreCase(String tag);

    // ✅ Proper custom query with JOIN on tag name
    Page<Campaign> findByArchivedFalse(Pageable pageable);

    @Query("""
                SELECT DISTINCT c FROM Campaign c 
                JOIN c.tags t 
                WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%')) 
                AND c.archived = false
            """)
    Page<Campaign> findByTagNameContainingIgnoreCaseAndArchivedFalse(@Param("tagName") String tag, Pageable pageable);

    List<Campaign> findByArchivedFalse();

    long countByArchivedTrue();

    Optional<Campaign> findTopByOrderByCreatedAtDesc();

    @Query(value = """
            SELECT DISTINCT c.* FROM campaign c
            JOIN campaign_tags ct ON c.id = ct.campaign_id
            JOIN tag t ON ct.tag_id = t.id
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%'))
              AND c.archived = false
            """, nativeQuery = true)
    List<Campaign> searchCampaignsByTagNameNative(@Param("tagName") String tagName);

}
