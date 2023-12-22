package com.piotrholda.spring.jwt;

record LoginRequest(
        String username,
        String password) {
}
