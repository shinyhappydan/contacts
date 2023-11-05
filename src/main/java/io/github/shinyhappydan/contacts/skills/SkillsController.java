package io.github.shinyhappydan.contacts.skills;

import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
public class SkillsController {
    private final Map<String, SkillWithId> skills = new HashMap<>();

    @GetMapping(value = "/skills", produces = "application/json")
    public Collection<SkillWithId> getSkills() {
        return skills.values();
    }

    @GetMapping(value = "/skills/{id}", produces = "application/json")
    public SkillWithId getSkill(@PathVariable String id) {
        return Optional.ofNullable(skills.get(id)).orElseThrow();
    }

    @PostMapping(value = "/skills", consumes = "application/json",produces = "application/json")
    @ResponseStatus(CREATED)
    public SkillWithId createSkill(@RequestBody Skill skill) {
        var id = UUID.randomUUID().toString();
        var entry = SkillWithId.from(skill, id);
        skills.put(id, entry);
        return entry;
    }

    @DeleteMapping(value = "/skills/{id}")
    public void deleteSkill(@PathVariable String id) {
        skills.remove(id);
    }

    @PostMapping(value = "/skills/{id}", consumes = "application/json", produces = "application/json")
    public SkillWithId updateSkill(@PathVariable String id, @RequestBody Skill skill) {
        var entry = SkillWithId.from(skill, id);
        if (skills.containsKey(id)) {
            skills.put(id, entry);
        } else {
            throw new NoSuchElementException();
        }
        return entry;
    }
}
