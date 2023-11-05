package io.github.shinyhappydan.contacts;

import io.github.shinyhappydan.contacts.contacts.ContactWithId;
import io.github.shinyhappydan.contacts.skills.SkillView;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContactSkillStore {
    private final MultiValueMap<String, String> contactSkills = CollectionUtils.toMultiValueMap(new ConcurrentHashMap<>());
    public Set<String> addSkill(ContactWithId contact, SkillView skill) {
        contactSkills.add(contact.id(), skill.id());

        return Set.copyOf(contactSkills.get(contact.id()));
    }
}
