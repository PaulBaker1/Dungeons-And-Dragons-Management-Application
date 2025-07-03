package com.dmtool.dashboard.dndapi.service;


import com.dmtool.dashboard.dndapi.dto.backroundDto.BackgroundDTO;
import com.dmtool.dashboard.dndapi.dto.classDto.ClassDTO;
import com.dmtool.dashboard.dndapi.dto.equipmentDto.EquipmentDTO;
import com.dmtool.dashboard.dndapi.dto.languageDto.LanguageDTO;
import com.dmtool.dashboard.dndapi.dto.magicItemDTO.MagicItemDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.MonsterDTO;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterDto;
import com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterListResponse;
import com.dmtool.dashboard.dndapi.dto.proficiencyDTO.ProficiencyDTO;
import com.dmtool.dashboard.dndapi.dto.raceDTO.RaceDTO;
import com.dmtool.dashboard.dndapi.dto.raceDTO.RaceListResponse;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellListDTO;
import com.dmtool.dashboard.dndapi.dto.sepllDto.SpellSummaryDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DnD5eApiServiceImpl implements DnD5eApiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper; // ✅ declare it

    public DnD5eApiServiceImpl(
            @Value("${dnd.api.baseurl:https://www.dnd5eapi.co/api/2014}") String baseUrl,
            ObjectMapper objectMapper // ✅ inject it via constructor
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.objectMapper = objectMapper;
    }

    private static void accept(String json, SynchronousSink<List<SrdMonsterDto>> sink) {
        try {
// Instantiate an ObjectMapper and parse the JSON into our DTO
            ObjectMapper mapper = new ObjectMapper();
            SrdMonsterListResponse response = mapper.readValue(json, SrdMonsterListResponse.class);
            sink.next(response.results());
        } catch (Exception e) {
            sink.error(new RuntimeException("❌ Failed to parse SrdMonsterListResponse", e));
        }
    }

    @Override
    public Mono<SpellDTO> getSpell(String index) {
        return webClient.get()
                .uri("/spells/{index}", index)
                .retrieve()
                .bodyToMono(SpellDTO.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("API error: " + e.getMessage(), e)));
    }

    @Override
    public Mono<MonsterDTO> getMonster(String index) {
        return webClient.get()
                .uri("/monsters/{index}", index)
                .retrieve()
                .bodyToMono(MonsterDTO.class)
                .onErrorResume(e -> Mono.error(
                        new RuntimeException("Failed to fetch monster [" + index + "]: " + e.getMessage(), e)
                ));
    }


    @Override
    public Mono<BackgroundDTO> getBackground(String index) {
        return webClient.get()
                .uri("/backgrounds/{index}", index)
                .retrieve()
                .bodyToMono(BackgroundDTO.class);
    }

    @Override
    public Mono<RaceDTO> getRace(String index) {
        return webClient.get()
                .uri("/races/{index}", index)
                .retrieve()
                .bodyToMono(RaceDTO.class);
    }

    @Override
    public Mono<ClassDTO> getClassByIndex(String index) {
        return webClient.get()
                .uri("/classes/{index}", index)
                .retrieve()
                .bodyToMono(ClassDTO.class);
    }

    @Override
    public Mono<List<EquipmentDTO>> getAllEquipment() {
        return webClient.get()
                .uri("/equipment")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMapMany(result -> {
                    List<Map<String, String>> results = (List<Map<String, String>>) result.get("results");
                    return Flux.fromIterable(results)
                            .flatMap(item -> getEquipment(item.get("index")));
                })
                .collectList();
    }

    @Override
    public Mono<EquipmentDTO> getEquipment(String index) {
        return webClient.get()
                .uri("/equipment/{index}", index)
                .retrieve()
                .bodyToMono(EquipmentDTO.class);
    }


    @Override
    public Mono<LanguageDTO> getLanguage(String index) {
        return webClient.get()
                .uri("/languages/{index}", index)
                .retrieve()
                .bodyToMono(LanguageDTO.class);
    }

    @Override
    public Mono<ProficiencyDTO> getProficiency(String index) {
        return webClient.get()
                .uri("/proficiencies/{index}", index)
                .retrieve()
                .bodyToMono(ProficiencyDTO.class);
    }

    @Override
    public Mono<MagicItemDTO> getMagicItem(String index) {
        return webClient.get()
                .uri("/magic-items/{index}", index)
                .retrieve()
                .bodyToMono(MagicItemDTO.class);
    }

    @Override
    public Mono<Map> getAll(String type) {
        return webClient.get()
                .uri("/{type}", type)
                .retrieve()
                .bodyToMono(Map.class);
    }

    @Override
    public Mono<SpellListDTO> getSpellsByClass(String className) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/spells")
                        .queryParam("classes", className.toLowerCase())
                        .build())
                .retrieve()
                .bodyToMono(SpellListDTO.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch spells for class: " + className, e)));
    }

    public Mono<SpellSummaryDTO> getSpellSummary(String index) {
        return getSpell(index)
                .map(spell -> new SpellSummaryDTO(
                        spell.index(),
                        spell.name(),
                        spell.level(),
                        (spell.desc() != null && !spell.desc().isEmpty()) ? spell.desc().get(0) : "No description",
                        spell.casting_time(),
                        spell.duration()
                ));
    }

    public Mono<List<RaceDTO>> getAllRaces() {
        return webClient.get()
                .uri("/races")  // Uses the base URL configured in the WebClient
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class) // Get the raw JSON response as a String
                .doOnNext(json -> System.out.println("📦 Raw race JSON: " + json))
                .map(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        RaceListResponse response = mapper.readValue(json, RaceListResponse.class);
                        return response.getResults();
                    } catch (Exception e) {
                        throw new RuntimeException("❌ Failed to parse RaceListResponse", e);
                    }
                })
                .flatMapMany(Flux::fromIterable)
                .flatMap(summary ->
                        // Manually concatenate the host URL with the relative URL from the summary.
                        webClient.get()
                                .uri("https://www.dnd5eapi.co" + summary.getUrl())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(RaceDTO.class)
                )
                .collectList();
    }

    @Override
    public Mono<List<com.dmtool.dashboard.dndapi.dto.monsterDTO.SrdMonsterDto>> getAllMonsters() {
        return webClient.get()
                .uri("/monsters")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)  // Get the raw JSON as a String
                .doOnNext(json -> System.out.println("📦 Raw monster JSON: " + json))
                .<List<SrdMonsterDto>>handle(DnD5eApiServiceImpl::accept)
                .flatMapMany(Flux::fromIterable)
                .flatMap(monsterSummary ->
                        // Construct the full URL using the 'url' field from the summary
                        webClient.get()
                                .uri("https://www.dnd5eapi.co" + monsterSummary.url())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(SrdMonsterDto.class)
                )
                .collectList();
    }



    @Override
    public Mono<Map<String, Object>> getAllSrdData() {
        Mono<Map> spells = getAll("spells");
        Mono<Map> monsters = getAll("monsters");
        Mono<Map> backgrounds = getAll("backgrounds");
        Mono<Map> races = getAll("races");
        Mono<Map> classes = getAll("classes");
        Mono<Map> equipment = getAll("equipment");
        Mono<Map> languages = getAll("languages");
        Mono<Map> proficiencies = getAll("proficiencies");
        Mono<Map> magicItems = getAll("magic-items");

        return Mono.zip(
                objects -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("spells", objects[0]);
                    result.put("monsters", objects[1]);
                    result.put("backgrounds", objects[2]);
                    result.put("races", objects[3]);
                    result.put("classes", objects[4]);
                    result.put("equipment", objects[5]);
                    result.put("languages", objects[6]);
                    result.put("proficiencies", objects[7]);
                    result.put("magicItems", objects[8]);
                    return result;
                },
                spells, monsters, backgrounds, races, classes, equipment, languages, proficiencies, magicItems
        );
    }
}
