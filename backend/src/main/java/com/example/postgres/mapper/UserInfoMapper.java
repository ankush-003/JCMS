package com.example.postgres.mapper;


import com.example.postgres.Dto.UserRegistrationDto;
import com.example.postgres.classes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public User convertToEntity(UserRegistrationDto userRegistrationDto) {
        User userInfoEntity = new User();
        userInfoEntity.setName(userRegistrationDto.userName());
        userInfoEntity.setUsername(userRegistrationDto.userName());
        userInfoEntity.setEmail(userRegistrationDto.userEmail());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}
