package proyecto.integrador.odbyt.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
	/**
	 * Encrypt a string with MD5 algorithm.
	 * @param s String to be encrypted.
	 * @return The encrypted string.
	 */
	public static String MD5(String s) {
        String r = null;
        try {
            if (s != null) {
                MessageDigest algorithm =MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(s.getBytes());
                byte bytes[] = algorithm.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xff & bytes[i]);
                    if (hex.length() == 1)
                    sb.append('0');
                    sb.append(hex);
                }
                r = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return r;
    }
}
