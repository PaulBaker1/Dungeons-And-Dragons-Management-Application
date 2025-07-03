package com.dmtool.dashboard.srd.srdmetadata.client;
import com.dmtool.dashboard.srd.srdmetadata.client.SrdBackgroundClient;
import com.dmtool.dashboard.srd.srdmetadata.dto.SrdBackgroundDto;

import java.util.Optional;

public interface SrdBackgroundClient {
        Optional<SrdBackgroundDto> getBackgroundByIndex(String index);
}
