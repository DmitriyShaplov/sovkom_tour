package phonenumbers.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import phonenumbers.dto.UserResultDto;
import phonenumbers.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping("/user/{id}")
    public UserResultDto getUser(@PathVariable int id) {
        log.info("Попытка получить данные по пользователю: {}", id);
        return service.mergeUserData(id);
    }
}
