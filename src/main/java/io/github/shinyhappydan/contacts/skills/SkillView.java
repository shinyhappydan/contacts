package io.github.shinyhappydan.contacts.skills;

public record SkillView(String id, String name, Level level) implements SkillInfo {
    public static SkillView from(Skill skill, String id) {
        return new SkillView(id, skill.name(), skill.level());
    }

    public Skill toSkill() {
        return new Skill(name(), level());
    }
}
