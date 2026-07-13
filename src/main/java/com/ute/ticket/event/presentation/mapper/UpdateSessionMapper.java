package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.UpdateSessionCommand;
import com.ute.ticket.event.presentation.dto.UpdateSessionRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateSessionMapper {

    public UpdateSessionCommand toCommand(Long sessionId, Long eventId, Long userId, UpdateSessionRequest request) {
        return UpdateSessionCommand.builder()
                .sessionId(sessionId)
                .eventId(eventId)
                .userId(userId)
                .salesStartAt(request.getSalesStartAt())
                .salesEndAt(request.getSalesEndAt())
                .build();
    }
}
