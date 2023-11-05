package io.github.shinyhappydan.contacts.contacts;

import io.github.shinyhappydan.contacts.skills.SkillView;

import java.util.Set;

public record ContactView(String id, String firstName, String lastName, String fullName, String address, String email, String phoneNumber, Set<SkillView> skills) implements ContactInfo {
    public static ContactView from(ContactWithId contact, Set<SkillView> skills) {
        return new ContactView(contact.id(), contact.firstName(), contact.lastName(), contact.fullName(), contact.address(), contact.email(), contact.phoneNumber(), skills);
    }
}
