package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataType.FinalSymbol;
import dataType.SymbolTool;
import debuger.Debuger;

public class Lexer {
	//------------------ Static part ----------------------// 
	private static final Pattern pattern;
	// ��ӡƥ��ģʽ�����ַ������Լ�ƥ����
	private static Debuger debuger = new Debuger(false);
	// build patternStr
	static {
		String patternStr = "";
		for (int i = 0; i < SymbolTool.getLength(); i++) {
			if (i != 0) {
				patternStr += "|";
			}
			patternStr += "(";
			patternStr += SymbolTool.getSymbol(i);
			patternStr += ")";
		}
		pattern = Pattern.compile(patternStr);
		debuger.println("ģʽ����:");
		debuger.println(patternStr);
	}
	
	//------------------ Dynamic part ----------------------// 
	// Attributes
	private final String matchStr;
	
	// Constructors
	public Lexer(String filename) {
		String matchStr = readFromFile(filename);
		this.matchStr = matchStr;
	}
	
	// Functions
	/** ���ִ����Ż� */
	public ArrayList<FinalToken> tokenize() {
		ArrayList<FinalToken> finalTokens = new ArrayList<FinalToken>();
		int size = matchStr.length();
		int index = 0;
		int rowNum = 1;
		while (index < size) {
			debuger.println("ƥ�俪ʼλ��: " + index);
			FinalToken finalToken = nextFinalToken(index, rowNum);
			if (finalToken == null) {
				System.out.println("Դ�ı���" + rowNum + "�д������޷�ƥ�䲿��");
				System.exit(0);
			} else if (finalToken.symbol == FinalSymbol.NEW_LINE) {
				rowNum++;
			} else if (finalToken.symbol != FinalSymbol.SPACE) {
				finalTokens.add(finalToken);
			}
			index += finalToken.length();
			debuger.println("token:" + finalToken.toString());
		}
		return finalTokens;
	}

	/**
	 * ��Դ�ַ���ƥ����һ��token
	 * @param index:ƥ�俪ʼ(inclusive)
	 * @return ƥ��ķ���, null��ʾ�����޷�ƥ���
	 */
	private FinalToken nextFinalToken(int index, int rowNum) {
		if (index >= matchStr.length()) {
			throw new IndexOutOfBoundsException();
		}
		
		Matcher matcher = pattern.matcher(matchStr);
		matcher.region(index, matchStr.length());
		boolean hasNext = matcher.lookingAt(); // ��ƥ�����ƥ��
		if (hasNext) {
			FinalToken ans = null;
			for (int groupID = 1; groupID <= SymbolTool.getLength(); groupID++){ 
				String content = matcher.group(groupID); // group()��Чλ�ô�1��ʼ
				if (content != null) {
					int symbolID = groupID - 1; // groupIDΪ��ţ���1��ʼ����symbol��0��ʼ
					FinalSymbol finalSymbol = SymbolTool.get(symbolID);
					FinalSymbol realFinalSymbol = dealConflict(finalSymbol, content);
					ans = TokenFactory.makeFinalToken(realFinalSymbol, rowNum, content);
				}
			}
			
			if(ans != null){
				return ans;				
			}else{
				throw new NullPointerException("��������ȷnull,δ֪����");
			}
		} else {
			return null;
		}
	}
	
	/**
	 * ����symbol��content�ж���ʵ����s
	 * @param finalSymbol
	 * @param content
	 * @return ���ڳ�ͻ��������ʵ���ͣ������ڳ�ͻ������symbol
	 */
	private FinalSymbol dealConflict(FinalSymbol finalSymbol, String content) {
		if(finalSymbol == FinalSymbol.IDENTIFIER){
			FinalSymbol kword = SymbolTool.toKeyword(content);
			if (kword != null) {
				return(kword);
			} else {
				return finalSymbol;
			}
		} else {
			return finalSymbol;
		}
	}
	
	private static String readFromFile(String fileName){
		try {
			String str;
	        File file = new File(fileName);
	        FileReader reader;
			reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
	        StringBuilder sb = new StringBuilder();
	        String s = "";
	        while ((s =bReader.readLine()) != null) {
	            sb.append(s + "\n");
	            // System.out.println(s); // rowPrint
	        }
	        bReader.close();
	        str = sb.toString();
			debuger.println("Դ�ַ�����:");
			debuger.println(str);
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			throw new NullPointerException();
		} 
	}
	
//	public static void main(String args[]) {
//		Lexer lexer = new Lexer("C:/Users/asus/Desktop/code.txt");
//		ArrayList<FinalToken> finalTokens = lexer.tokenize();
//		debuger.println("-----------ƥ����--------------");
//		for(AbstractToken token : finalTokens){
//			debuger.println(token.toString());
//		}
//	}
}