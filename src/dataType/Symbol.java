package dataType;

/**
 * hypothesis1: ��һ�������Լ���ս�״̬,��һ����������sp = X(������ʽ)$
 * hypothesis2: ����ĸ��д���ս��
 */
public interface Symbol {
	
	boolean isFinal();
	
	String name();
	
	int index(); // ȡ��ordinal����ʾ��SymbolList��λ��
	
}
