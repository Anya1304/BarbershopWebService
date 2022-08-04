package com.spdu.app.user.controller;

import com.spdu.app.user.dto.UserDto;
import com.spdu.app.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spdu.app.user.converter.UserConverter.fromDto;
import static com.spdu.app.user.converter.UserConverter.toDto;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "salon")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add-user")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("permitAll")
    public UserDto postUser(@RequestBody @Valid UserDto userDto) {
        return toDto(userService.save(fromDto(userDto)));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getUsers() {
        return toDto(userService.getList());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public UserDto getUser(@PathVariable int id) {
        return toDto(userService.get(id));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }
}
