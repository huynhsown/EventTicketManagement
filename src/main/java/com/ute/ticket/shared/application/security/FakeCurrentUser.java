package com.ute.ticket.shared.application.security;

import org.springframework.stereotype.Component;

@Component
public class FakeCurrentUser implements CurrentUser {
    @Override
    public Long getUserId() {
        return 2L;
    }
}
