package com.dmtool.dashboard.dndapi.dto.sepllDto;

import java.util.List;

public record SpellListDTO(
        int count,
        String next,
        String previous,
        List<SpellDTO> results
) {
}
