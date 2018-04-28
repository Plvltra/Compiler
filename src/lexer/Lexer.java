package lexer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataType.FinalSymbol;
import dataType.SymbolTool;
import lexer.token.FinalToken;
import lexer.token.Position;
import lexer.token.TokenFactory;
import util.Constant;
import util.Debuger;
import util.FileUtil;

public class Lexer {
	//------------------ Static part ----------------------// 
	private static final Pattern pattern;
	// 打印匹配模式串，字符串，以及匹配结果
	private static Debuger debuger = new Debuger(true);
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
		String matchStr = FileUtil.readFromFile(filename);
		this.matchStr = matchStr;
	}
	
	// Functions
	/** 将字串符号化 */
	public ArrayList<FinalToken> tokenize() {
		ArrayList<FinalToken> finalTokens = new ArrayList<FinalToken>();
		int size = matchStr.length();
		int index = 0;
		Position currPos = new Position(1);
		while (index < size) {
			debuger.println("匹配开始位置: " + index);
			FinalToken finalToken = nextFinalToken(index, currPos);
			if (finalToken == null) {
				System.out.println(currPos.toString() + "处出现无法匹配部分");
				System.exit(0);
			} else if (finalToken.getSymbol() == FinalSymbol.NEW_LINE) {
				currPos = currPos.nextRow();
			} else if (finalToken.getSymbol() != FinalSymbol.SPACE) {
				finalTokens.add(finalToken);
			}
			index += finalToken.length();
			debuger.println("token:" + finalToken.toString());
		}
		return finalTokens;
	}

	/**
	 * 从源字符串匹配下一个token
	 * @param index 源代码匹配开始的字符串位置(inclusive)
	 * @param position 源文件中的位置信息
	 * @return 匹配的符号, null表示遇到无法匹配的
	 */
	private FinalToken nextFinalToken(int index, Position position) {
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
					ans = TokenFactory.makeFinalToken(realFinalSymbol, position, content);
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
	
	public static void main(String args[]) {
		Lexer lexer = new Lexer(Constant.codeFile);
		ArrayList<FinalToken> finalTokens = lexer.tokenize();
		debuger.println("-----------匹配结果--------------");
		for(FinalToken token : finalTokens){
			debuger.println(token.toString());
		}
	}
	
}