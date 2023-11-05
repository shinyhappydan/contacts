package io.github.shinyhappydan.contacts.skills;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Skill(@NotBlank String name, @NotNull Level level) implements SkillInfo {
}

