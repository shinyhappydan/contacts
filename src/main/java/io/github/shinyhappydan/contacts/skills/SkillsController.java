package io.github.shinyhappydan.contacts.skills;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
public class SkillsController {
    private final SkillStore skillStore;

    public SkillsController(@Autowired SkillStore skillStore) {
        this.skillStore = skillStore;
    }

    @GetMapping(value = "/skills", produces = "application/json")
    public Collection<SkillView> getSkills() {
        return skillStore.getAll();
    }

    @GetMapping(value = "/skills/{id}", produces = "application/json")
    public SkillView getSkill(@PathVariable String id) {
        return requireSkill(id);
    }

    @PostMapping(value = "/skills", consumes = "application/json",produces = "application/json")
    @ResponseStatus(CREATED)
    public SkillView createSkill(@RequestBody @Valid Skill skill) {
        return skillStore.create(skill);
    }

    @DeleteMapping(value = "/skills/{id}")
    public void deleteSkill(@PathVariable String id) {
        skillStore.delete(id);
    }

    @PostMapping(value = "/skills/{id}", consumes = "application/json", produces = "application/json")
    public SkillView updateSkill(@PathVariable String id, @RequestBody @Valid Skill skill) {
        return skillStore.update(id, skill);
    }

    private SkillView requireSkill(String id) {
        var skill = skillStore.get(id);
        if (skill == null) throw new NoSuchElementException();
        else return skill;
    }
}
