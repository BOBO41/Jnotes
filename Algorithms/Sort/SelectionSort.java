package com.hdr.learn.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author hdr
 */
public class SelectionSort {


	public static void sort(Comparable[] arr) {
		int len = arr.length;

		for (int i = 0; i < len; i++) {

			int min = i;

			for (int j = i+1; j < len; j++) {
				if (less(arr[j], arr[min])) {
					min = j;
				}
			}

			exch(arr, i, min);
		}
	}


	private static boolean less(Comparable a, Comparable b) {
		return a.compareTo(b) < 0;
	}


	private static void exch(Comparable[] arr, int i, int j) {
		Comparable swap = arr[i];
		arr[i] = arr[j];
		arr[j] = swap;
	}

	public static void main(String[] args) {
		Integer[] arr = new Integer[1000];
		Random random = new Random();

		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt();
		}

		SelectionSort.sort(arr);
		System.out.println(Arrays.toString(arr));
		System.out.println("数组是否有序？ "+ SortUtils.isSort(arr));

	}

}
