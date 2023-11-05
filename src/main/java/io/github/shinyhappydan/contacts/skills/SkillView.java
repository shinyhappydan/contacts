package io.github.shinyhappydan.contacts.skills;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Skill view", description = "A respresentation of a skill including its ID.")
public record SkillView(
        String id,
        String name,
        Level level) implements SkillInfo {
    public static SkillView from(Skill skill, String id) {
        return new SkillView(id, skill.name(), skill.level());
    }

    public Skill toSkill() {
        return new Skill(name(), level());
    }
}
