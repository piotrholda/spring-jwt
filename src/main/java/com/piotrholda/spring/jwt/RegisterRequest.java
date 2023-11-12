package com.piotrholda.spring.jwt;

import jakarta.validation.constraints.Size;

record RegisterRequest(
        String firstname,
        String lastname,
        @Size(min = 1)
        String username,
        @Size(min = 1)
        String password) {
}
