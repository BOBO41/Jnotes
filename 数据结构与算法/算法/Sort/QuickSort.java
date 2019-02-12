package com.hdr.learn.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author hdr
 */
public class QuickSort {

	public static void sort(Comparable[] arr) {
		sort(arr, 0, arr.length - 1);
	}

	private static void sort(Comparable[]arr, int lo , int hi) {
		if(lo>=hi) return;
		int j = partition(arr, lo, hi);
		sort(arr, lo, j - 1);
		sort(arr, j + 1, hi);
	}


	private static int partition(Comparable[] arr, int lo, int hi) {
		int i = lo, j = hi + 1;

		while (true) {

			while (less(arr[++i], arr[lo]))  if (i == hi) break;

			while (less(arr[lo], arr[--j]))  if (j == lo) break;

			if (i >= j) break;

			exch(arr,i,j);
		}

		exch(arr, lo, j);
		return j;
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

		QuickSort.sort(arr);
		System.out.println(Arrays.toString(arr));
		System.out.println("数组是否有序？ "+ SortUtils.isSort(arr));

	}
}
