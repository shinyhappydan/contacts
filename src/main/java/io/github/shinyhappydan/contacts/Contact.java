package io.github.shinyhappydan.contacts;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Contact(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String fullName, @NotBlank String address, @Email String email, @Digits(integer = 15, fraction = 0) String phoneNumber) implements ContactInfo {

    public Contact withEmail(String email) {
        return new Contact(firstName(), lastName(), fullName(), address(), email, phoneNumber());
    }

}
