package io.github.shinyhappydan.contacts;

public record ContactWithId(String id, String name) implements ContactInformation {
    public static ContactWithId from(Contact contact, String id) {
        return new ContactWithId(id, contact.name());
    }

}
