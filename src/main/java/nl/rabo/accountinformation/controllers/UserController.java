package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.UserRequest;
import nl.rabo.accountinformation.models.entity.UserEntity;
import nl.rabo.accountinformation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lakshmaiah Tatikonda
 * User controller contains user crud operations
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserEntity getUser(@PathVariable @NotNull Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody @Valid UserRequest userRequest) {
        userService.addUser(userRequest);
    }

    @DeleteMapping(path = "/users/{userId}")
    public void deleteUser(@PathVariable @NotNull Long userId) {
        userService.deleteUser(userId);
    }

}

