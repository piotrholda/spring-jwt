package com.piotrholda.spring.jwt;

import lombok.Value;

record ProfileResponse(
        String firstname,
        String lastname,
        String username,
        Role role) {
}
