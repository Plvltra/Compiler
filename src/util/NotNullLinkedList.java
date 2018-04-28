package util;

import java.util.Collection;
import java.util.LinkedList;

/** �����ԱΪ�� */
@SuppressWarnings("serial")
public class NotNullLinkedList<E> extends LinkedList<E> {
	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException();
		} else {
			return super.add(e);	
		}
	}
	
	public NotNullLinkedList() {
		super();
	}
	
	public NotNullLinkedList(Collection<? extends E> c) {
		super(c);
	}
}