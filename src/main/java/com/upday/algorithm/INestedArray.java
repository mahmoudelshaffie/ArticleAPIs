package com.upday.algorithm;

import java.util.Iterator;

public interface INestedArray {
	void addInteger(Integer integer);
	void addNestedArray(INestedArray nestedArr);
	Iterator<Object> getIterator();
}
