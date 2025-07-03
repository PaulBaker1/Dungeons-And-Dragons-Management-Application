package com.dmtool.dashboard.dndapi.dto.deserializer;

import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArmorClassDeserializer extends JsonDeserializer<List<SrdMonsterDto.ArmorClass>> {

    @Override
    public List<SrdMonsterDto.ArmorClass> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonToken token = p.getCurrentToken();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        // Handle null values.
        if (token == JsonToken.VALUE_NULL) {
            return Collections.emptyList();
        }

        // If the token is the start of an array...
        if (token == JsonToken.START_ARRAY) {
            // Read the entire array as a List of Objects to examine its contents.
            List<Object> list = mapper.readValue(p, new TypeReference<>() {
            });
            if (!list.isEmpty()) {
                Object first = list.getFirst();
                // Case 1: Array contains numbers.
                if (first instanceof Number) {
                    List<SrdMonsterDto.ArmorClass> result = new ArrayList<>();
                    for (Object num : list) {
                        int value = ((Number) num).intValue();
                        result.add(new SrdMonsterDto.ArmorClass("unknown", value));
                    }
                    return result;
                }
                // Case 2: Array contains objects (maps).
                else if (first instanceof java.util.Map) {
                    return mapper.convertValue(
                            list,
                            new TypeReference<>() {
                            }
                    );
                }
                // Otherwise, return empty list.
                else {
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        }
        // If the token is directly numeric (not in an array), wrap it.
        else if (token.isNumeric()) {
            int value = p.getIntValue();
            SrdMonsterDto.ArmorClass ac = new SrdMonsterDto.ArmorClass("unknown", value);
            return Collections.singletonList(ac);
        }
        // If none of the above, return empty list.
        else {
            return Collections.emptyList();
        }
    }
}
