package io.github.shinyhappydan.contacts.skills;

public record Skill(String name) {
    public Skill withName(String name) {
        return new Skill(name);
    }
}

