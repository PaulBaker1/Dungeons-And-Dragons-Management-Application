package com.dmtool.dashboard.dndapi.dto.sepllDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public record SpellDTO(
        String index, // ✅ Add this line!
        String name,
        List<String> desc,
        List<String> higher_level,
        String range,
        List<String> components,
        String material,
        String duration,
        String casting_time,
        int level,
        String url // ✅ THIS is what fixes your error!
) {}


