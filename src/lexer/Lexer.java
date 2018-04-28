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
	// ��ӡƥ��ģʽ�����ַ������Լ�ƥ����
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
		debuger.println("ģʽ����:");
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
	/** ���ִ����Ż� */
	public ArrayList<FinalToken> tokenize() {
		ArrayList<FinalToken> finalTokens = new ArrayList<FinalToken>();
		int size = matchStr.length();
		int index = 0;
		Position currPos = new Position(1);
		while (index < size) {
			debuger.println("ƥ�俪ʼλ��: " + index);
			FinalToken finalToken = nextFinalToken(index, currPos);
			if (finalToken == null) {
				System.out.println(currPos.toString() + "�������޷�ƥ�䲿��");
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
	 * ��Դ�ַ���ƥ����һ��token
	 * @param index Դ����ƥ�俪ʼ���ַ���λ��(inclusive)
	 * @param position Դ�ļ��е�λ����Ϣ
	 * @return ƥ��ķ���, null��ʾ�����޷�ƥ���
	 */
	private FinalToken nextFinalToken(int index, Position position) {
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
					ans = TokenFactory.makeFinalToken(realFinalSymbol, position, content);
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
	
	public static void main(String args[]) {
		Lexer lexer = new Lexer(Constant.codeFile);
		ArrayList<FinalToken> finalTokens = lexer.tokenize();
		debuger.println("-----------ƥ����--------------");
		for(FinalToken token : finalTokens){
			debuger.println(token.toString());
		}
	}
	
}