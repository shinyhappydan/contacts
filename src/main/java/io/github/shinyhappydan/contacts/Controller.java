package io.github.shinyhappydan.contacts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {
    @GetMapping(value = "/contacts", produces = "application/json")
    public List<String> getContacts() {
        return new ArrayList<>();
    }
}
