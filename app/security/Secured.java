package security;

import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.play.store.PlayCacheStore;
import org.pac4j.play.store.PlaySessionStore;

import com.google.inject.AbstractModule;

public class Secured extends AbstractModule {

	/*public final static String JWT_SALT = "12345678901234567890123456789012";

	public final static List<String> signingSecrets = ImmutableList.of(
			"VE54dbpnE4rH7RGGsCkN5ZzLzYeaVxQKsygxtZfafQKcf8UU2DwZmHnU9B3qTDj9",
			"JkueE9DJY75VmpAMfDhnujxv92AcAbmnTUj2p8dG6NLcsZtSfYPPPzBYrq5eKrmE",
			"ywBLfAeCD74aWUuKYTtgRhbA9Q9PcVAGvr7MvuFWtwFrcm523HH4khh4ZTmSDg2z",
			"GKQ8FGspNmnT8WnCKqPb4zNg2gKmccFDt8jbd6fnL48mycDxXVx7GTD2tRyWQuCS",
			"GsCSEjXrKEL3x2r9seBtXPmwEu4qgwym7jw2mKyEJMsezX9nr4xLFa2FpF7AUv5z",
			"zU7LpvJdn7E2T5PpzV2PxxUgkDnfy3RjksqhDag5j4H9RrWEMJQ9CYFbTjjGnEgL",
			"eSFbwGSusUZFWAebzwfEUSDkc9wZ7PnDmwreh2jD3qxCJ8zMQSrpxkTaRff6Lx57",
			"9RL3ZeQXAM7zHEvQNKsHxkARaqcPPUhY5ame4B3g3Z6HhsVGDGaVJsF6vFafrVtx",
			"9F9fgbDzGFyQZjTj2S8raH5Z2Y89D4vTtJDEmg85VjG7UgVJmLYEdwxdUECS2M5Y",
			"RQNuj4bp9DNybcAqNF7zPqM7xLgPePpq8enhPFcMsNgkUdSSZCJTA33Fbt4QwVYE",
			"TfntnDcQg3cw4vp3prTnuS5mdJjSXUA9b32a3x6HnhbB5EXYdrzKfYCLzjJMBN45");
	public final static List<String> encryptionSecrets = ImmutableList.of(
			"5drQWvRhDpEdrC6Fp6TxzfSAY5PwS3G29ZTUcrXTD7Q2dQGWMaR5eAgzrRHb8dUx",
			"vBuvUXkkZggpJrJte7QTDMSFcAvy65smpfJBPv8B8GD7wt9s4RD2E6Sb4VGfxksV",
			"sdnRA3E4BWp5TLnRwpWvGs7EcyAsk5KFuE4JugmBFCAwj3sfrWJ5nd9H5Bt7qcWD",
			"vVSSggYBayXNJvd5XRKhQGqM6MNzX5rawmYHaA3TanT9nB2xSww3xRqwwtZJfvey",
			"GzyeNvz8KEXXPHt7jGKpdsya7EUFLGY8kNFp2XTkTmN54HRAwCe8HDbPFENBQ9c2",
			"QDbeCQaYGBfqC446K8vhkxWB63PFKEvVbzdS4pnLJWvkS4by49qkhTwsHbAdALtQ",
			"ahz8N2wCzEmTbCwJcwMjDYHrtK7bUgducLvgLAvvtbRmtHW9xhkqkWwxf6L5BvaT",
			"hmFQKNuYLm5guWGqfTAH4uGaJZuGjPxTfmNTZuEvndjvprTQPdqSQU2FPejRLJKa",
			"E7RRUjDP5gqQ5hm7ZhJNwBzQcdx6U3hz6A2VzfupK26TvvBGGwKNJBp6bpfWWHpT",
			"wtyfqRFWa9YBFER4JafmuRshKYTrp5Qp2YGbB6zqpejsjS27NHT6ec2gjFYzfByq");*/

	@Override
	protected void configure() {// go for JWT!!!
		bind(PlaySessionStore.class).to(PlayCacheStore.class);

		JwtAuthenticator auth = new JwtAuthenticator();
		/*auth.setSignatureConfigurations(
				signingSecrets.stream().map(secret -> new SecretSignatureConfiguration(secret))
						.collect(Collectors.toList()));
		auth.setEncryptionConfigurations(
				signingSecrets.stream().map(secret -> new SecretEncryptionConfiguration(secret))
						.collect(Collectors.toList()));*/
		
		ParameterClient parameterClient = new ParameterClient("token", auth);
		parameterClient.setSupportGetRequest(true);
		parameterClient.setSupportPostRequest(false);

		// final IndirectBasicAuthClient indirectBasicAuthClient = new
		// IndirectBasicAuthClient(new
		// SimpleTestUsernamePasswordAuthenticator());
	}

}
