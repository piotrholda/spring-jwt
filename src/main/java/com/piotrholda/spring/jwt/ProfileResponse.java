package com.piotrholda.spring.jwt;

record ProfileResponse(
        String firstname,
        String lastname,
        String username,
        Role role) {
}
