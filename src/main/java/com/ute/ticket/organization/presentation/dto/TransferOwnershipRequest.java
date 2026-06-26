package com.ute.ticket.organization.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Transfer organization ownership request payload")
@NoArgsConstructor
@AllArgsConstructor
public class TransferOwnershipRequest {

    @NotNull
    @Schema(description = "Id of the member who will become the new owner", example = "2")
    private Long targetUserId;
}
