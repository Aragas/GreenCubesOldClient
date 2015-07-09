package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class GCUtil {

	public static final DateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String[] months2 = new String[]{"������", "�������", "�����", "������", "���", "����", "����", "�������", "��������", "�������", "������", "�������"};
	public static final Calendar calendar = Calendar.getInstance();

	public static float degreeMiddle(float deg1, float deg2) {
		double r1 = Math.toRadians(degreeRound(deg1));
		double r2 = Math.toRadians(degreeRound(deg2));
		if(r1 == r2)
			return deg1;
		double x = 0;
		double y = 0;
		x += Math.cos(r1);
		y += Math.sin(r1);
		x += Math.cos(r2);
		y += Math.sin(r2);
		double deg = Math.toDegrees(Math.atan2(y, x));
		if(deg < 0)
			deg += 360;
		return (float) deg;
	}

	public static float degreeDiff(float deg1, float deg2) {
		float par0 = Math.abs(deg1 - deg2);
		if(par0 > 180)
			return 360 - par0;
		return par0;
	}

	public static float degreeRound(float deg) {
		deg %= 360.0F;
		if(deg >= 180.0F)
			deg -= 360.0F;
		if(deg < -180.0F)
			deg += 360.0F;
		return deg;
	}

	public static float roundFloat(float a, float mod) {
		return a - (a % mod);
	}

	private static byte[] createChecksum(String string) throws NoSuchAlgorithmException {
		MessageDigest complete = MessageDigest.getInstance("MD5");
		byte[] buffer = string.getBytes();
		complete.update(buffer, 0, buffer.length);
		return complete.digest();
	}

	public static String getMD5Checksum(String string) throws NoSuchAlgorithmException {
		byte[] b = createChecksum(string);
		StringBuilder result = new StringBuilder(32);
		for(int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	public static String readFile(File f) throws IOException {
		Scanner fr = new Scanner(f);
		StringBuilder sb = new StringBuilder(64);
		while(fr.hasNextLine()) {
			if(sb.length() > 0)
				sb.append('\n');
			sb.append(fr.nextLine());
		}
		fr.close();
		return sb.toString();
	}

	public static String randomString(int length) {
		return randomString(length, new Random());
	}

	public static String getLocalizedDate2(long time) {
		calendar.setTimeInMillis(time);
		StringBuilder sb = new StringBuilder();
		sb.append(calendar.get(Calendar.DAY_OF_MONTH)).append(' ').append(months2[calendar.get(Calendar.MONTH)]).append(' ').append(calendar.get(Calendar.YEAR));
		return sb.toString();
	}

	private static final char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	public static String randomString(int length, Random rand) {
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; ++i)
			sb.append(chars[rand.nextInt(chars.length)]);
		return sb.toString();
	}

	public final static int getUnitDiff(double loc1, double loc2) {
		return MathHelper.floor_double(loc1 * 32.0D) - MathHelper.floor_double(loc2 * 32.0D);
	}

	public final static boolean arrayExactlyContains(int[] array, int obj) {
		for(int i = 0; i < array.length; ++i)
			if(array[i] == obj)
				return true;
		return false;
	}

	public static int count(CharSequence str, char c) {
		int count = 0;
		for(int i = 0; i < str.length(); ++i)
			if(str.charAt(i) == c)
				count++;
		return count;
	}

	public static String getMaxTimeString(long ms) {
		double s = ms / 1000D;
		if(s <= 60)
			return ((long) Math.ceil(s)) + "�";
		s /= 60D;
		if(s <= 60)
			return ((long) Math.ceil(s)) + "�";
		return ((long) Math.ceil(s / 60)) + "�";
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] arrayAppend(T[] src, T... append) {
		T[] newArray = (T[]) Arrays.copyOf(src, src.length + append.length, src.getClass());
		System.arraycopy(append, 0, newArray, src.length, append.length);
		return newArray;
	}
}
