package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.UpdateSessionCommand;
import com.ute.ticket.event.application.result.SessionResult;

public interface UpdateSessionUseCase {
    SessionResult execute(UpdateSessionCommand cmd);
}
