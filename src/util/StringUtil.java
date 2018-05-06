package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static Matcher match(String patternStr, String matchStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(matchStr);
		return matcher;
	}
	
	/**
	 * @return true: 1.matchStr是空行(只含有tab,space等) 2.matchStr首两个非空字符为斜杠注释
	 */
	public static boolean isBlank(String matchStr) {
		String patternStr = "^[\\s]*|[\\s]*//.*$";
		Matcher matcher = match(patternStr, matchStr);
		return matcher.matches();
	}
	
	
	public static String replace(String src, int start, int end, String replaced) {
		StringBuilder sb = new StringBuilder(src);
        sb.replace(start, end, replaced);
        return sb.toString();
	}
	public static String replace(String src, int pos, char replaced) {
        String ans = replace(src, pos, pos+1, Character.toString(replaced));
        return ans;
	}

	
	public static String getDelimString(ArrayList<String> elems, char delimeter) {
		return getDelimString(elems, Character.toString(delimeter));
	}
	/** 使用delimeter在两个elem之间分隔,最后一个elem后不加delimeter*/
	public static String getDelimString(ArrayList<String> elems, String delimeter) {
		if (elems == null) {
			throw new NullPointerException();
		} else if(elems.isEmpty()) {
			return "";
		} else {
			String ans = "";
			for(String elem : elems) {
				ans += elem;
				ans += delimeter;
			}
			ans = ans.substring(0, ans.length() - 1);
			return ans;
		}
	}
	
	public static void main(String[] args) {
		replace("abc", 2, 't');
		System.out.println(replace("abc", 0, 2, "hhhh"));
	}
	
}
