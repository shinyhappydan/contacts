package io.github.shinyhappydan.contacts;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import io.github.shinyhappydan.contacts.contacts.ContactWithId;
import io.github.shinyhappydan.contacts.skills.SkillView;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ContactSkillStore {
    private final SetMultimap<String, String> contactSkills =  MultimapBuilder.hashKeys().hashSetValues().build();
    public Set<String> addSkill(ContactWithId contact, SkillView skill) {
        contactSkills.put(contact.id(), skill.id());

        return contactSkills.get(contact.id());
    }

    public Set<String> removeSkill(ContactWithId contact, SkillView skill) {
        contactSkills.remove(contact.id(), skill.id());

        return contactSkills.get(contact.id());
    }
}
