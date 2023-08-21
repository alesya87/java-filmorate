package ru.yandex.practicum.filmorate.controller.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserFullDto {
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Некорректный email")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен сожержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    private LocalDate birthday;
    private Set<Integer> friends;
}
