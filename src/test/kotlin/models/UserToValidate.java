package models;

import bg.arusenov.patchable.Patchable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserToValidate {

    private String name;
    private Integer age;
    private Patchable<@NotNull @Email String> email = Patchable.of("not_email");
}