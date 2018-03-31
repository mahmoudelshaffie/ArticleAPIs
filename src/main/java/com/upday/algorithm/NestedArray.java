package com.upday.algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NestedArray implements INestedArray {
	
	private List<Object> elements = new LinkedList<Object>();

	public void addInteger(Integer integer) {
		this.elements.add(integer);
	}

	public void addNestedArray(INestedArray nestedArr) {
		this.elements.add(nestedArr);
	}

	public Iterator<Object> getIterator() {
		return this.elements.iterator();
	}

}
