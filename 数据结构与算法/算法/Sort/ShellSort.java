package com.hdr.learn.algorithms.sort;

import java.util.Arrays;

/**
 * @author hdr
 */
public class ShellSort {

	public static void sort(Comparable[] arr) {
		int len = arr.length;
		int h = 1;
		while (h < len / 3) {
			h = 3 * h + 1;
		}

		while (h >= 1) {
			for (int i = h; i < len; i++) {
				for (int j = i; j >= h; j -= h) {
					if (less(arr[j], arr[j - h])) {
						exch(arr, j, j - h);
					}else{
						break;
					}
				}
			}

			h = h / 3;
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
		Integer[] arr = {100, 255, 7, 1, 344, 6, 7, 88, 55};
		ShellSort.sort(arr);
		System.out.println(Arrays.toString(arr));
	}


}
