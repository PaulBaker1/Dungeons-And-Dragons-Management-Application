package com.dmtool.dashboard.srd.srdmetadata.client;

import com.dmtool.dashboard.srd.srdmetadata.dto.SrdAlignmentDto;

import java.util.Optional;

public interface SrdAlignmentClient {
    Optional<SrdAlignmentDto> getAlignmentByIndex(String index);
}
