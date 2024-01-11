package it.unicam.cs.ids.loyalty.util;

import java.util.Random;

public class PasswordGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!'£$%&";
	private static final int CODE_LENGTH = 8;

	public static String generatePassword() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(CODE_LENGTH);

		for (int i = 0; i < CODE_LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String generatedPassword = generatePassword();
		System.out.println("Password generata: " + generatedPassword);
	}
}
