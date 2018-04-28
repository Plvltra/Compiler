package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public static Matcher match(String patternStr, String matchStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(matchStr);
		return matcher;
	}
	
	/** 检验matchStr是空行或注释 */
	public static boolean isBlank(String matchStr) {
		String patternStr = "^[\\s]*|[\\s]*//.*$";
		Matcher matcher = match(patternStr, matchStr);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isBlank("    "));
	}
}
