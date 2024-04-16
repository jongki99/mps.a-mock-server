package com.example.demo.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;

/**
 * BinaryOperator 두개를 이용한 값을 처리한다.
 * 
 * reduce 샘플을 이용해서 IntBinaryOperator 샘플링.
 * 
 * reduce( identity, IntBinaryOperator )
 * reduce( IntBinaryOperator ) return OptionalInt 해서 후속 조치가 필요하다.
 */
public class TestBinaryOperator {
	public static void main(String[] args) {
		{
			IntBinaryOperator add = (a, b) -> a + b;
			int result = add.applyAsInt(10, 20);
			System.out.println(result); // 출력: 30
		}
		
		{
			int[] numbers = {};
//			int[] numbers = null;// null 은 안됨.
			Collections.singletonList("test");
			int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b); // 0 은 기본값 같은건가?
			System.out.println("sum="+sum); // 출력: 15
			// int sum2 = Arrays.stream(numbers).reduce((a, b) -> a + b).getAsInt(); // 0 은 기본값 같은건가? // no value present error
			OptionalInt reduce = Arrays.stream(numbers).reduce((a, b) -> a + b); // identity 은 기본값 같은건가? input 으로도 사용되므로 조금 다르네...
			int sum2 = 0;
			if ( reduce.isPresent() ) {
				sum2 = reduce.getAsInt();
			} else {
				sum2 = Integer.MIN_VALUE;
			}
			System.out.println("sum2="+sum2); // 출력: 15
		}
		{
			int[] numbers = {1, 2, 3, 4, 5};
			int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b); // 0 은 기본값 같은건가?
			System.out.println(sum); // 출력: 15
		}
		{
			int[] numbers = {1, 2, 3, 4, 5};
			int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b); // 0 은 기본값 같은건가?
			System.out.println(sum); // 출력: 15
		}
		{
			int[] numbers = {1, 2, 3, 4, 5};
			int sum = Arrays.stream(numbers).reduce((a, b) -> a + b).getAsInt(); // 위의 것과 다른점은... 뒤의 메소드를 써야 한다. 값이 없을수 있다는 건데...
			System.out.println(sum); // 출력: 15
		}
		
		{
			int[] numbers = {3, 2, 1, 4, 5};
			int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a > b ? a : b); // max
			System.out.println(sum); // 출력: 5
		}
		{
			int[] numbers = {3, 2, 1, 4, 5};
			int sum = Arrays.stream(numbers).reduce((a, b) -> a > b ? a : b).getAsInt(); // max
			System.out.println(sum); // 출력: 5
		}
		
		{
			int[] numbers = {2, 3, 1, 4, 5};
			int sum = Arrays.stream(numbers).reduce((a, b) -> a > b ? b : a).getAsInt(); // min
			System.out.println(sum); // 출력: 1 기본값이 없네...
		}
	}
}
