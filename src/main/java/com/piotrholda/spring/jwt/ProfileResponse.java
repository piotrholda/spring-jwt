package com.piotrholda.spring.jwt;

import lombok.Value;

@Value
class ProfileResponse {
    String firstname;
    String lastname;
    String username;
    Role role;
}
