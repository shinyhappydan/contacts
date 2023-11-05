package io.github.shinyhappydan.contacts.skills;

import jakarta.validation.constraints.NotBlank;

public record Skill(@NotBlank String name) {
    public Skill withName(String name) {
        return new Skill(name);
    }
}

