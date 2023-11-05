package io.github.shinyhappydan.contacts.skills;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkillStore {

    private final Map<String, SkillView> skills = new HashMap<>();

    public SkillView get(String skillId) {
        return skills.get(skillId);
    }

    public Collection<SkillView> getAll() {
        return skills.values();
    }

    public SkillView create(Skill skill) {
        var id = UUID.randomUUID().toString();
        var entry = SkillView.from(skill, id);
        skills.put(id, entry);
        return entry;
    }

    public void delete(String id) {
        skills.remove(id);
    }

    public SkillView update(String id, Skill skill) {
        var entry = SkillView.from(skill, id);
        if (skills.containsKey(id)) {
            skills.put(id, entry);
        } else {
            throw new NoSuchElementException();
        }
        return entry;
    }
}
