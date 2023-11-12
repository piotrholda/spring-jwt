package com.piotrholda.spring.jwt;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
interface UserMapper {
    ProfileResponse toProfileResponse(UserEntity userEntity);
    UserEntity toUserEntity(RegisterRequest registerRequest);
}
