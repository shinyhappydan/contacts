package io.github.shinyhappydan.contacts.skills;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Skill update", description = "A respresentation of a skill, used when updating or creating a contact. Does not contain an ID.")
public record Skill(
        @NotBlank String name,
        @NotNull Level level) implements SkillInfo {
}

