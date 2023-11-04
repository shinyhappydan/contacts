package io.github.shinyhappydan.contacts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class Controller {

    private final Set<String> contacts = new HashSet<>();

    @GetMapping(value = "/contacts", produces = "application/json")
    public Set<String> getContacts() {
        return contacts;
    }

    @PostMapping(value = "/contacts", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String createContact(@RequestBody String name) {
        contacts.add(name);
        return name;
    }

    @DeleteMapping(value = "/contacts/{name}")
    public void deleteContact(@PathVariable String name) {
        contacts.remove(name);
    }
}
