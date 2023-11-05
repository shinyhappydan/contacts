package io.github.shinyhappydan.contacts;

import io.github.shinyhappydan.contacts.skills.SkillView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.shinyhappydan.contacts.Fixtures.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EndToEndTests {

	@Autowired
	private MockMvc mvc;

	private ApiClient client;

	@BeforeEach
	void setUp() {
		client = new ApiClient(mvc);
	}

	@Test
	public void testAddSkillToContact() throws Exception {
		var java6Id = client.createSkill(Java6).id();

		var andreId = client.createContact(Andre).id();

		var result = client.addSkillToContact(andreId, java6Id);

		assertEquals(List.of(Java6.name()), result.skills().stream().map(SkillView::name).toList());
		assertEquals(List.of(Java6.level()), result.skills().stream().map(SkillView::level).toList());
	}

	@Test
	public void testRemoveSkillFromContact() throws Exception {
		var java6Id = client.createSkill(Java6).id();
		var kotlinId = client.createSkill(Kotlin).id();

		var andreId = client.createContact(Andre).id();

		client.addSkillToContact(andreId, java6Id);
		client.addSkillToContact(andreId, kotlinId);

		var result = client.removeSkillFromContact(andreId, java6Id);
		assertEquals(List.of(Kotlin.name()), result.skills().stream().map(SkillView::name).toList());
		assertEquals(List.of(Kotlin.level()), result.skills().stream().map(SkillView::level).toList());
	}

}
