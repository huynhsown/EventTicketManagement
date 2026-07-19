package com.ute.ticket.event.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Builder
@Setter
@Schema(description = "Create event request payload")
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotNull
    @Schema(description = "Organization id the event belongs to", example = "1")
    private Long organizationId;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Event title", example = "UTE Tech Conference 2026")
    private String title;

    @Size(max = 255)
    @Schema(description = "Unique slug (auto-generated from title if empty)", example = "ute-tech-conference-2026")
    private String slug;

    @Size(max = 4000)
    @Schema(description = "Event description", example = "Annual technology conference at UTE")
    private String description;

    @Schema(description = "Venue id (optional, required before publish)", example = "1")
    private Long venueId;

    @Schema(description = "Category ids to assign (optional)", example = "[\"1\", \"2\"]")
    private Set<Long> categoryIds;

    @Size(max = 2048)
    @Schema(description = "URL to event banner", example = "https://example.com/banner.png")
    private String bannerUrl;
}
