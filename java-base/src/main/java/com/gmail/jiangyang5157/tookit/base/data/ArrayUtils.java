package com.gmail.jiangyang5157.tookit.base.data;

import java.util.Arrays;

/**
 * @author Yang 8/18/2015.
 */
public class ArrayUtils {

    /**
     * Sort a array in ascending order
     *
     * @param array the given array to be modified
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        sort(array, 0, array.length - 1);
    }

    /**
     * @param array the given array to be modified
     * @param left  left lower bound of the array
     * @param right right upper bound of the array
     */
    private static <T extends Comparable<T>> void sort(T[] array, int left, int right) {
        if (left < right) {
            int pivot = partition(array, left, right);
            sort(array, left, pivot - 1);
            sort(array, pivot + 1, right);
        }
    }

    /**
     * @param array the given array to be modified
     * @param left  left lower bound of the array
     * @param right right upper bound of the array
     * @return the integer which points to the sorted pivot index where elements to its left are less than it
     * and elements to its right are more than it
     */
    private static <T extends Comparable<T>> int partition(T[] array, int left, int right) {
        int i = left;
        for (int j = left; j < right; j++) {
            if (array[j].compareTo(array[right]) < 0) {
                swap(array, i, j);
                i++;
            }
        }
        swap(array, i, right);
        return i;
    }

    /**
     * Swap the values between the two given index
     *
     * @param array the given array to be modified
     * @param i     index of first element to be swapped
     * @param j     index of second element to be swapped
     */
    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Test case
     */
    public static void main(String[] args) {
        Integer[] intArray = new Integer[]{1, 222, 6, -2, -4, -5, 7, 3};
        ArrayUtils.sort(intArray);
        System.out.println(Arrays.asList(intArray));
    }
}
