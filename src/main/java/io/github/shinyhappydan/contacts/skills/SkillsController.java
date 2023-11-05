package io.github.shinyhappydan.contacts.skills;

import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
public class SkillsController {
    private final Set<String> skills = new HashSet<>();

    @GetMapping(value = "/skills", produces = "application/json")
    public Set<String> getSkills() {
        return skills;
    }

    @PutMapping(value = "/skills/{skill}", produces = "application/json")
    @ResponseStatus(CREATED)
    public String createSkill(@PathVariable String skill) {
        skills.add(skill);
        return skill;
    }
}
