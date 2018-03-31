package com.upday.algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FlatteningArrayUtil {
	
	private static void flattenArr(Iterator<Object> it, List<Integer> flattenArr) { // Output object to avoid headache of two lists addition
		while(it.hasNext()) {
			Object el = it.next();
			if (el instanceof Integer) {
				Integer i = (Integer) el;
				flattenArr.add(i);
			} else {
				INestedArray nestedArr = (INestedArray) el;
				flattenArr(nestedArr.getIterator(), flattenArr);
			}
		}
	}
	
	public static Integer[] toFlatArray(INestedArray nestedArray) {
		LinkedList<Integer> flattenArr = new LinkedList<Integer>();
		
		Iterator<Object> it = nestedArray.getIterator();
		flattenArr(it, flattenArr);
		
		Integer[] arr = new Integer[flattenArr.size()];
		return flattenArr.toArray(arr);
	}
}
