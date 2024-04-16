package com.example.demo.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomUtils {
	public static int rand(int min, int max) {
		if (min > max || (max - min + 1 > Integer.MAX_VALUE)) {
			throw new IllegalArgumentException("Invalid range");
		}

//		return new Random().nextInt(max - min + 1) + min;
		return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
	}

	// Function to generate random nums from a list according to the
	// given probabilities
	public static int random(int[] nums, int[] probability) {
		int n = nums.length;
		if (n != probability.length) {
			return -1; // error
		}

		// construct a sum array from the given probabilities
		int[] prob_sum = new int[n];

		// `prob_sum[i]` holds sum of all `probability[j]` for `0 <= j <=i`
		prob_sum[0] = probability[0];
		for (int i = 1; i < n; i++) {
			prob_sum[i] = prob_sum[i - 1] + probability[i];
		}

		// generate a random integer from 1 to 100 and check where it lies
		// in `prob_sum[]`
		int r = rand(1, prob_sum[prob_sum.length - 1]);

		// based on the comparison result, return the corresponding
		// element from the input list

		if (r <= prob_sum[0]) { // handle 0th index separately
			return nums[0];
		}

		for (int i = 1; i < n; i++) {
			if (r > prob_sum[i - 1] && r <= prob_sum[i]) {
				return nums[i];
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		{
			double testCount = 100000D;
			long start = System.nanoTime();
			main1(testCount);
			long end = System.nanoTime();
			log.debug("nano start={}, end={}, term sec={}", start, end, ((double) end - start) / 1000000000D);
		}

		
		{
			Map<Integer, Integer> freq = new HashMap<>();
			Map<Integer, Integer> freqWeight = new HashMap<>();
			Map<Integer, Integer> freqIndex = new HashMap<>();
			int startVal = 1, endVal = 10;
			double testCount = 100000D;
			long start = System.nanoTime();
			for (int i = 0; i < testCount; i++) {
				int prob = getProbability(startVal, endVal); // startVal ~ endVal
				freq.put(prob, freq.getOrDefault(prob, 0) + 1);
				int weight = getProbabilityWeight(endVal); // startVal ~ endVal
				freqWeight.put(weight, freqWeight.getOrDefault(weight, 0) + 1);
				int index = getProbabilityIndex(endVal); // startVal ~ endVal
				freqIndex.put(index, freqIndex.getOrDefault(index, 0) + 1);
			}
			long end = System.nanoTime();
			log.debug("freq={}", freq);
			log.debug("freqWeight={}", freqWeight);
			log.debug("freqIndex={}", freqIndex);
			log.debug("nano start={}, end={}, term sec={}", start, end, ((double) end - start) / 1000000000D);
		}
		
		{

			long start = System.nanoTime();
			for(int index=0; index<100; index++) {
				double testCount = 1000000D;
				int trueCount = 0;
				long start1 = System.nanoTime();
				for (int i = 0; i < testCount; i++) {
					if ( subLucky(100) ) {
						trueCount++;
					}
				}
				long end1 = System.nanoTime();
				log.debug("testCount={}, {}", testCount, trueCount/testCount);
				//log.debug("nano start1={}, end1={}, term sec={}", start1, end1, ((double) end1 - start1) / 1000000000D);
			}
			long end = System.nanoTime();
			//log.debug("nano start={}, end={}, term sec={}", start, end, ((double) end - start) / 1000000000D);
		}
	}

	private static int getProbability(int start, int end) {
		return ThreadLocalRandom.current().nextInt(start, end+1);
	}

	private static int getProbabilityWeight(int totalWeight) {
		return ThreadLocalRandom.current().nextInt(1, totalWeight+1);
	}

	private static int getProbabilityIndex(int arrayLength) {
		return ThreadLocalRandom.current().nextInt(0, arrayLength);
	}

	public static void main1(double testCount) {
		// Input: list of integers and their probabilities
		// Goal: generate `nums[i]` with probability equal to `probability[i]`

		int[] nums = { 1, 2, 3, 4, 5 };
		int[] probability = { 30, 10, 20, 15, 2 };

		// maintain a frequency map to validate the results
		Map<Integer, Integer> freq = new HashMap<>();

		// make 1000000 calls to the `random()` function and store results in a map
		for (int i = 0; i < testCount; i++) {
			int val = random(nums, probability);
			freq.put(val, freq.getOrDefault(val, 0) + 1);
		}

		// print the results
		for (int i = 0; i < nums.length; i++) {
//			System.out.println(nums[i] + " ~ " + freq.get(nums[i]) / 100000.0 + "%");
			if ( freq.get(nums[i]) != null ) {
				log.debug("expected={} = {} ~ {}%", probability[i], nums[i], freq.get(nums[i]) / testCount * 100);
			}
		}
		
		log.debug("freq={}", freq);
	}

	public static boolean subLucky(Integer strdVal) {
		// subLucky1 보다 10배 빠르다.
		if ( strdVal != null && strdVal > 0 ) { // 0 이거나 null 이면... true 반환.
			return ThreadLocalRandom.current().nextInt(strdVal) == 0; // 확율. 1/strdVal
		} else {
			return true;
		}
	}

	public static boolean subLucky1(Integer strdVal) {
        Random random2 = new Random();
        int randomVal2 = random2.nextInt(strdVal) + 1; // 1-100 까지
        // int compareVal2 = random2.nextInt(strdVal) + 1; // 1-100 까지
        // log.debug("randomVal2={}, compareVal2={}, equals={}", randomVal2, compareVal2, randomVal2 == compareVal2);
//        return randomVal2 == compareVal2; // 1/strdVal
        return randomVal2 == 1; // 1/strdVal 확율.
	}
}
