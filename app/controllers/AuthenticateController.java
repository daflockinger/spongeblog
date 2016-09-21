package controllers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.jwt.config.encryption.EncryptionConfiguration;
import org.pac4j.jwt.config.encryption.RSAEncryptionConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.store.PlaySessionStore;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthenticateController extends Controller {

	@Inject
	private PlaySessionStore playSessionStore;

	public Result getToken() throws NoSuchAlgorithmException, NoSuchProviderException {

		CommonProfile prof = new CommonProfile();
		prof.setClientName("flo");
		prof.setId(UUID.randomUUID().toString());
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyGen.initialize(1024, random);

		KeyPair pair = keyGen.generateKeyPair();
		EncryptionConfiguration ee = new RSAEncryptionConfiguration(pair, JWEAlgorithm.RSA1_5,
				EncryptionMethod.A256CBC_HS512);

		final List<CommonProfile> profiles = ImmutableList.of(prof);
		final JwtGenerator<CommonProfile> generator = new JwtGenerator<>();
		generator.setEncryptionConfiguration(ee);
		String token = "";
		if (CommonHelper.isNotEmpty(profiles)) {
			token = generator.generate(profiles.get(0));
		}

		return ok(Json.toJson(token));
	}


	/*
	 * private List<CommonProfile> getProfiles() { final PlayWebContext context
	 * = new PlayWebContext(ctx(), playSessionStore); final
	 * ProfileManager<CommonProfile> profileManager = new
	 * ProfileManager<>(context); return profileManager.getAll(true); }
	 */
}
