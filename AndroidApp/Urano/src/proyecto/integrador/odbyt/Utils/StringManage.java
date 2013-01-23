package proyecto.integrador.odbyt.Utils;

public class StringManage {

	/**
	 * Function that returns a substring from left to right conformed by
	 * characters of the root string.
	 * 
	 * @param text
	 *            Root String.
	 * @param length
	 *            Number of characters that will be returned.
	 * @return A substring conformed by the number of characters given.
	 */
	public static String Left(String text, int length) {
		return text.substring(0, length);
	}

	/**
	 * Function that returns a substring from right to left conformed by
	 * characters of the root string.
	 * 
	 * @param text
	 *            Root String.
	 * @param length
	 *            Number of character that will be returned.
	 * @return A substring conformed by the number of characters given.
	 */
	public static String Right(String text, int length) {
		return text.substring(text.length() - length, text.length());
	}

	/**
	 * Function that returns a substring from <i>starts</i> to <i>ends</i>.
	 * 
	 * @param text
	 *            Root String.
	 * @param start
	 *            Start position.
	 * @param end
	 *            End position.
	 * @return A substring conformed by the characters from starts position to
	 *         ends position of the root string.
	 */
	public static String Mid(String text, int start, int end) {
		return text.substring(start - 1, (start + end - 1));
	}
}
