package lexer;

import java.util.regex.*;

import dataType.SymbolTool;

public class RegexTest {
	public static void main(String args[]) {
		
	    // �����ַ������Ƿ���ƥ��������ʽ���ַ�/�ַ���
//		String content = "I am noob from runoamob.com.";
//		Pattern pattern = Pattern.compile("([\\s]+)|(a)");
		String content = "int@@int";
		String patternStr = "";
		for (int i = 0; i < SymbolTool.getLength(); i++) {
			if (i != 0) {
				patternStr += "|";
			}
			patternStr += "(";
			patternStr += SymbolTool.getSymbol(i);
			patternStr += ")";
		}
		System.out.println(SymbolTool.getLength());
		Pattern pattern = Pattern.compile(patternStr);
		System.out.println("ģʽ����: " + patternStr);
		
		Matcher matcher = pattern.matcher(content);
		matcher.region(0, content.length());
		
		boolean have = matcher.lookingAt();
		if(have){
			for(int i=1;i<=SymbolTool.getLength();i++){
				if(matcher.group(i) != null)
					System.out.println(i + ":" + matcher.group(i));
			}
			//System.out.println(matcher.group(5));
			System.out.println("ok");
		}else{
			System.out.println("no");
		}
	}
}
