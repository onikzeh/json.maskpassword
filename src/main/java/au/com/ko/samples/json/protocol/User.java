package au.com.ko.samples.json.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	private final String username;
	private final SensitiveValue password;

	@JsonCreator
	public User(@JsonProperty("username") String username,
				@JsonProperty("password") SensitiveValue password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public SensitiveValue getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "User{" +
				"\n username: '" + username + '\'' +
				"\n password: '" + password + '\'' +
				"\n}";
	}
}
