package com.mo.utils;

import java.util.Random;

public class HelperUtil {

	private static Random random = new Random();

	public static String generateOtp() {

		final int otpLength = 6;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < otpLength; i++) {
			builder.append(random.nextInt(10));
		}

		return builder.toString();
	}

	public static String generateBookingId() {

		String alphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

		final int idLength = 15;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < idLength; i++) {

			int tempNum = (int) (alphaNumericStr.length() * Math.random());
			builder.append(alphaNumericStr.charAt(tempNum));

		}

		return builder.toString();
	}

	public static String generateComplaintId() {
		
		final int idLength = 6;
		final String alphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < idLength; i++) {
			int tempNum = (int)(alphaNumericStr.length()*Math.random());
			
			builder.append(alphaNumericStr.charAt(tempNum));
		}
		
		return builder.toString();
		
	}

}
