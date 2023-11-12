package com.piotrholda.spring.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
