package com.upday.algorithm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FlatteningArrayUtilTest {
	
	@Test
	public void testFlatteningFlatArrayShouldBeFlattenWithSameOrderAndSameElements() {
		INestedArray nestedArray = new NestedArray();
		nestedArray.addInteger(1);
		nestedArray.addInteger(2);
		nestedArray.addInteger(3);
		nestedArray.addInteger(4);
		nestedArray.addInteger(5);
		
		Integer[] actualArr = FlatteningArrayUtil.toFlatArray(nestedArray);
		Integer expectedLength = 5;
		assertTrue("Invalid Size of Flatten Arr", expectedLength == actualArr.length);
		Integer[] expectedArray = {1, 2, 3, 4, 5};
		assertArrayEquals("", expectedArray, actualArr);
	}
	
	@Test
	public void testFlatteningNestedArrayShouldBeFlattenWithSameOrderAndSameElements() {
		INestedArray nestedArray = new NestedArray();
		nestedArray.addInteger(1);
		nestedArray.addInteger(2);
		nestedArray.addInteger(3);
		
		INestedArray nestedArray1 = new NestedArray();
		nestedArray1.addInteger(11);
		nestedArray1.addInteger(12);
		
		INestedArray nestedArray2 = new NestedArray();
		nestedArray2.addInteger(21);
		nestedArray2.addInteger(22);
		nestedArray2.addInteger(23);
		
		nestedArray1.addNestedArray(nestedArray2);
		nestedArray1.addInteger(13);
		nestedArray1.addInteger(14);
		
		nestedArray.addNestedArray(nestedArray1);
		
		INestedArray nestedArray3 = new NestedArray();
		nestedArray3.addInteger(31);
		nestedArray3.addInteger(32);
		nestedArray.addNestedArray(nestedArray3);
		
		Integer[] actualArr = FlatteningArrayUtil.toFlatArray(nestedArray);
		Integer expectedLength = 12;
		assertTrue("Invalid Size of Flatten Arr", expectedLength == actualArr.length);
		Integer[] expectedArray = {1, 2, 3, 11, 12, 21, 22, 23, 13, 14, 31, 32};
		assertArrayEquals("", expectedArray, actualArr);
	}
}
