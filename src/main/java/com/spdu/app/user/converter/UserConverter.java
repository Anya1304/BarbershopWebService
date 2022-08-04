package com.spdu.app.user.converter;

import com.spdu.app.user.dto.UserDto;
import com.spdu.app.user.model.User;

import java.util.List;

public final class UserConverter {

    private UserConverter() {
    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setSalonId(user.getSalonId());

        return userDto;
    }

    public static User fromDto(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setSalonId(userDto.getSalonId());

        return user;
    }

    public static List<UserDto> toDto(List<User> users) {
        return users.stream()
                .map(UserConverter::toDto)
                .toList();
    }
}

