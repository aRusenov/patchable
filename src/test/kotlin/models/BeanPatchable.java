package models;

import bg.arusenov.patchable.Patchable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class BeanPatchable {

    private Patchable<@Valid School> school = Patchable.of(new School());

    static class School {
        @NotEmpty
        private String name = "";
    }
}
