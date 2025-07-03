package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdClassLevelDto;

import java.util.List;
import java.util.Optional;

public interface SrdClassLevelClient {
    Optional<List<SrdClassLevelDto>> getClassLevels(String index, String subclass);
    Optional<SrdClassLevelDto> getClassLevel(String index, int classLevel);
}
