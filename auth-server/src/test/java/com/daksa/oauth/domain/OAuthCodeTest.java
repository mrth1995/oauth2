package com.daksa.oauth.domain;

import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuthCodeTest {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthCodeTest.class);

	@Test
	public void testChallengeCode() {
		String key = "zg3pnwuR20OvnsDM1FtpfBMfJaUf1v0Y";
		String codeVerifier = "12345";
		String codeChallenge = HmacUtils.hmacSha256Hex(key, codeVerifier);
		LOG.debug(codeChallenge);
	}
}
