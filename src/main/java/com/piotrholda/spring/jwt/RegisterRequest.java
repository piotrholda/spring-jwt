package com.piotrholda.spring.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


record RegisterRequest(
        String firstname,
        String lastname,
        String username,
        String password) {
}
