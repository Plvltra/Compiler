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
	// 打印匹配模式串，字符串，以及匹配结果
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
		debuger.println("模式串是:");
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
	/** 将字串符号化 */
	public ArrayList<FinalToken> tokenize() {
		ArrayList<FinalToken> finalTokens = new ArrayList<FinalToken>();
		int size = matchStr.length();
		int index = 0;
		int rowNum = 1;
		while (index < size) {
			debuger.println("匹配开始位置: " + index);
			FinalToken finalToken = nextFinalToken(index, rowNum);
			if (finalToken == null) {
				System.out.println("源文本第" + rowNum + "行处出现无法匹配部分");
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
	 * 从源字符串匹配下一个token
	 * @param index:匹配开始(inclusive)
	 * @return 匹配的符号, null表示遇到无法匹配的
	 */
	private FinalToken nextFinalToken(int index, int rowNum) {
		if (index >= matchStr.length()) {
			throw new IndexOutOfBoundsException();
		}
		
		Matcher matcher = pattern.matcher(matchStr);
		matcher.region(index, matchStr.length());
		boolean hasNext = matcher.lookingAt(); // 从匹配起点匹配
		if (hasNext) {
			FinalToken ans = null;
			for (int groupID = 1; groupID <= SymbolTool.getLength(); groupID++){ 
				String content = matcher.group(groupID); // group()有效位置从1开始
				if (content != null) {
					int symbolID = groupID - 1; // groupID为组号，从1开始，而symbol从0开始
					FinalSymbol finalSymbol = SymbolTool.get(symbolID);
					FinalSymbol realFinalSymbol = dealConflict(finalSymbol, content);
					ans = TokenFactory.makeFinalToken(realFinalSymbol, rowNum, content);
				}
			}
			
			if(ans != null){
				return ans;				
			}else{
				throw new NullPointerException("明明存在确null,未知错误");
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 根据symbol和content判断真实类型s
	 * @param finalSymbol
	 * @param content
	 * @return 存在冲突，返回真实类型，不存在冲突，返回symbol
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
			debuger.println("源字符串是:");
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
//		debuger.println("-----------匹配结果--------------");
//		for(AbstractToken token : finalTokens){
//			debuger.println(token.toString());
//		}
//	}
}