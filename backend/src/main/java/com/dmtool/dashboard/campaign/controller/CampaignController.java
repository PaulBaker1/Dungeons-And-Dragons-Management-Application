package com.dmtool.dashboard.campaign.controller;

import com.dmtool.dashboard.campaign.dto.CampaignCreateDTO;
import com.dmtool.dashboard.campaign.dto.CampaignDetailsDTO;
import com.dmtool.dashboard.campaign.dto.CampaignSummaryDTO;
import com.dmtool.dashboard.campaign.dto.CampaignUpdateDTO;
import com.dmtool.dashboard.campaign.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService service;

    @Operation(summary = "Get all campaigns or filter by tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CampaignSummaryDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CampaignSummaryDTO>> all(
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<CampaignSummaryDTO> result = (tag == null || tag.isBlank())
                ? service.getAll(pageable)
                : service.getByTag(tag, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create a new campaign")
    @PostMapping("/create")
    public ResponseEntity<CampaignDetailsDTO> create(@Valid @RequestBody CampaignCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Delete a campaign by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Toggle pin status of a campaign")
    @PatchMapping("/{id}/pin")
    public ResponseEntity<CampaignDetailsDTO> pin(@PathVariable Long id) {
        return ResponseEntity.ok(service.togglePinned(id));
    }

    @Operation(summary = "Set the last session date of a campaign")
    @PatchMapping("/{id}/last-session")
    public ResponseEntity<CampaignDetailsDTO> setLastSession(
            @PathVariable Long id,
            @RequestParam("lastSessionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(service.setLastSessionDate(id, date));
    }

    @Operation(summary = "Archive a campaign")
    @PatchMapping("/{id}/archive")
    public ResponseEntity<CampaignDetailsDTO> archive(@PathVariable Long id) {
        return ResponseEntity.ok(service.archive(id));
    }

    @Operation(summary = "Update a campaign")
    @PutMapping("/{id}")
    public ResponseEntity<CampaignDetailsDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CampaignUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Get campaign statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(service.getStats());
    }

    @Operation(summary = "Get campaign details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDetailsDTO> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetails(id));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}

