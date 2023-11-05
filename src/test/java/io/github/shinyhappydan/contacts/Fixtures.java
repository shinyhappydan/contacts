package io.github.shinyhappydan.contacts;

import io.github.shinyhappydan.contacts.contacts.Contact;
import io.github.shinyhappydan.contacts.skills.Level;
import io.github.shinyhappydan.contacts.skills.Skill;

public class Fixtures {

    public static final Contact Andre = new Contact("Andre", "O'Connor", "Andre O'Connor", "12 Bonio Lane", "andre@rules.ok", "07755667788");
    public static final Contact Bernie = new Contact("Bernie", "Bell-Bamford", "Bernie Bell-Bamford", "Pat's Paws", "bernie@zoomies.org", "077666778899");

    public static final Skill Java6 = new Skill("Java 6", Level.BASIC);
    public static final Skill Kotlin = new Skill("Kotlin", Level.BASIC);
    public static final Skill TDD = new Skill("Test-driven development", Level.INTERMEDIATE);
    public static final Skill Communication = new Skill("Communication", Level.ADVANCED);
}
