package au.com.ko.samples.unittest;

import au.com.ko.samples.json.protocol.SensitiveValue;
import au.com.ko.samples.json.protocol.User;
import au.com.ko.samples.json.prov.SensitiveValueModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * Test the SensitiveValueModule functionality.
 */
@RunWith(JUnit4.class)
public class SensitiveValueModuleTest {

	private static final String PASSWORD = "secret!";

	@Test
	public void test_maskedPassword() throws IOException, URISyntaxException {
		ObjectMapper om = new ObjectMapper()
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.registerModule(SensitiveValueModule.masking());

		User user = new User("Kevin", new SensitiveValue(PASSWORD));
		String json = om.writeValueAsString(user);

		System.out.println("Serialized object using masking:\n" + json);

		assertTrue("Sensitive value should not be serialized.", !json.contains(PASSWORD));
	}

	@Test
	public void test_NonMaskedPassword() throws IOException, URISyntaxException {
		ObjectMapper om = new ObjectMapper()
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.registerModule(SensitiveValueModule.nonMasking());

		User user = new User("Kevin", new SensitiveValue(PASSWORD));
		String json = om.writeValueAsString(user);

		System.out.println("Serialized object without using masking:\n"+json);

		assertTrue("Sensitive value should be serialized.", json.contains(PASSWORD));
	}

	@Test
	public void test_serializeDeserialize() throws IOException, URISyntaxException {
		ObjectMapper om = new ObjectMapper()
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.registerModule(SensitiveValueModule.masking());

		User user = null;
		URL url = SensitiveValueModuleTest.class.getResource("/user.json");
		InputStream in = url.openStream();

		try {
			user = om.readValue(in, User.class);
		} finally {
			in.close();
		}

		String userPassword = new String(user.getPassword().getValue());

		System.out.println("Read password from user.json file: '" + userPassword + "'");

		assertTrue("Sensitive value should be read from json file.", userPassword.equals(PASSWORD));
	}

}
