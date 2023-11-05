package io.github.shinyhappydan.contacts.skills;

public record SkillWithId(String id, String name) {
    public static SkillWithId from(Skill skill, String id) {
        return new SkillWithId(id, skill.name());
    }

    public Skill toSkill() {
        return new Skill(name());
    }
}
