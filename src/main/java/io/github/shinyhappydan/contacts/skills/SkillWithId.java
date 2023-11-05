package io.github.shinyhappydan.contacts.skills;

public record SkillWithId(String id, String name, Level level) implements SkillInfo {
    public static SkillWithId from(Skill skill, String id) {
        return new SkillWithId(id, skill.name(), skill.level());
    }

    public Skill toSkill() {
        return new Skill(name(), level());
    }
}
