package com.hdr.learn.algorithms.sort;

/**
 * @author hdr
 */
public class SortUtils {


	public static boolean isSort(Comparable[] arr) {
		return isAsc(arr) || isDesc(arr);
	}

	/**
	 * 是否是递增的
	 * @param arr 判断是否递增的数组
	 * @return 数组是否递增
	 */
	public static boolean isAsc(Comparable[] arr) {
		for (int i = 1; i < arr.length; i++) {
			if (arr[i - 1].compareTo(arr[i]) > 0) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 是否是递减的
	 * @param arr 判断是否递减的数组
	 * @return 数组是否递减
	 */
	public static boolean isDesc(Comparable[] arr) {
		for (int i = 1; i < arr.length; i++) {
			if (arr[i - 1].compareTo(arr[i]) < 0) {
				return false;
			}
		}
		return true;
	}

}
