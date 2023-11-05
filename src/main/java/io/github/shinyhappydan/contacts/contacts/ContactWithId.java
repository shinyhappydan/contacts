package io.github.shinyhappydan.contacts.contacts;

public record ContactWithId(String id, String firstName, String lastName, String fullName, String address, String email, String phoneNumber) implements ContactInfo {
    public static ContactWithId from(Contact contact, String id) {
        return new ContactWithId(id, contact.firstName(), contact.lastName(), contact.fullName(), contact.address(), contact.email(), contact.phoneNumber());
    }

    public Contact toContact() {
        return new Contact(firstName(), lastName(), fullName(), address(), email(), phoneNumber());
    }
}
