package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class CreateUserRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Некорректный email")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен сожержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    private LocalDate birthday;
}
