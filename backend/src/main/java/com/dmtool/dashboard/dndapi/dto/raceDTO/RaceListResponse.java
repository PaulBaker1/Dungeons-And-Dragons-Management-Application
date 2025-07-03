package com.dmtool.dashboard.dndapi.dto.raceDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RaceListResponse {
    private int count;
    private List<RaceSummary> results;
}
