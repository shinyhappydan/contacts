package io.github.shinyhappydan.contacts.contacts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Contact update", description = "A respresentation of a contact, used when updating or creating a contact. Does not contain skills or and ID.")
public record Contact(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String fullName,
        @NotBlank String address,
        @Email String email,
        @Digits(integer = 15, fraction = 0) String phoneNumber) implements ContactInfo {

    public Contact withEmail(String email) {
        return new Contact(firstName(), lastName(), fullName(), address(), email, phoneNumber());
    }

}
