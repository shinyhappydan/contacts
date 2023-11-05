package io.github.shinyhappydan.contacts.skills;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@RestController
public class SkillsController {
    private final Set<String> skills = new HashSet<>();

    @GetMapping(value = "/skills", produces = "application/json")
    public Set<String> getSkills() {
        return skills;
    }
}
