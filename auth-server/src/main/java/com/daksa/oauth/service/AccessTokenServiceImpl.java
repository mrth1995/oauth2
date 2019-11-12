package com.daksa.oauth.service;

import io.olivia.webutil.IDGen;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.util.Base64;

@Dependent
public class AccessTokenServiceImpl implements AccessTokenService {
	private static final Logger LOG = LoggerFactory.getLogger(AccessTokenService.class);

	@Override
	public String createAccessToken(String clientId) {
		String uuid = IDGen.generate();
		String random = RandomStringUtils.randomAlphanumeric(32);
		String tokenRaw = clientId + ":01:" + uuid + random;
		return Base64.getEncoder().encodeToString(tokenRaw.getBytes());
	}

	@Override
	public String createAuthCode(String code) {
		String uuid = IDGen.generate();
		String random = RandomStringUtils.randomAlphanumeric(32);
		String tokenRaw = code + ":00:" + uuid + random;
		return Base64.getEncoder().encodeToString(tokenRaw.getBytes());
	}
}
