package com.ute.ticket.shared.utils;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class UuidGenerator {

    private UuidGenerator() {
    }

    public static UUID v7() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
