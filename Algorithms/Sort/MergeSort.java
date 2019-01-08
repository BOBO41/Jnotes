package com.hdr.learn.algorithms.sort;

import java.util.Arrays;

/**
 * @author hdr
 */
public class MergeSort {


	public static void sort(Comparable[] arr) {
		int len = arr.length;
		Comparable[] aux = new Comparable[len];
		sort(arr, aux, 0, len - 1);
	}

	private static void sort(Comparable[] arr, Comparable[] aux, int lo, int hi) {
		if (lo == hi) {
			return;
		}

		int mid = lo + (hi - lo) / 2;
		sort(arr, aux, lo, mid);
		sort(arr, aux, mid + 1, hi);
		merge(arr, aux, lo, mid, hi);
	}

	/**
	 * @param a   需要排序的数组
	 * @param aux 用于辅助排序的数组
	 * @param lo
	 * @param mid
	 * @param hi
	 */
	private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {

		for (int k = lo; k <= hi; k++) {
			aux[k] = a[k];
		}

		int i = lo;
		int j = mid + 1;

		for (int k = lo; k <= hi; k++) {
			if (i > mid) a[k] = aux[j++];
			else if (j > hi) a[k] = aux[i++];
			else if (less(aux[i], aux[j])) a[k] = aux[i++];
			else a[k] = aux[j++];
		}

	}

	private static boolean less(Comparable a, Comparable b) {
		return a.compareTo(b) < 0;
	}

	public static void main(String[] args) {
		Integer[] arr = {100, 255, 7, 1, 344, 6, 7, 88, 55};
		MergeSort.sort(arr);
		System.out.println(Arrays.toString(arr));
	}

}
