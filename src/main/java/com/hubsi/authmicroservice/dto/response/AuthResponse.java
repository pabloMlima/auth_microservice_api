package com.hubsi.authmicroservice.dto.response;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID refreshToken
) {
}
