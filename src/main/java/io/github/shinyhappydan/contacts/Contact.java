package io.github.shinyhappydan.contacts;

public record Contact(String firstName, String lastName, String fullName, String address, String email, String phoneNumber) implements ContactInfo {

    public Contact withEmail(String email) {
        return new Contact(firstName(), lastName(), fullName(), address(), email, phoneNumber());
    }

}
