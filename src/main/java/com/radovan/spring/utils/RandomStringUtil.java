package com.radovan.spring.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomStringUtil {

	public String getAlphaNumericString(int n) {

		byte[] array = new byte[256];
		new Random().nextBytes(array);

		String randomString = new String(array, StandardCharsets.UTF_8);
		StringBuffer r = new StringBuffer();

		for (int k = 0; k < randomString.length(); k++) {

			char ch = randomString.charAt(k);

			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {

				r.append(ch);
				n--;
			}
		}

		return r.toString();
	}
}