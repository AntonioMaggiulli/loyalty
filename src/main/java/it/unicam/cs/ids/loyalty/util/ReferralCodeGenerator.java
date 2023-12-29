package it.unicam.cs.ids.loyalty.util;

import java.util.Random;

public class ReferralCodeGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
	private static final int CODE_LENGTH = 8;

	public static String generateReferralCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(CODE_LENGTH);

		for (int i = 0; i < CODE_LENGTH; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String referralCode = generateReferralCode();
		System.out.println("Referral Code: " + referralCode);
	}
}
