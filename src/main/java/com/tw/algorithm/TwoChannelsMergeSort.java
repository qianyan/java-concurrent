package com.tw.algorithm;

import java.util.concurrent.*;

public class TwoChannelsMergeSort extends RecursiveTask<int[]>{

    private static final int CHANNELS = 2;
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final int[] source;

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        int[] data = {
                1, 3, 3, 2, 10, 4, 1,
                1, 3, 3, 2, 10, 4, 1,
                1, 3, 3, 2, 10, 4, 1,
                1, 3, 3, 2, 10, 4, 1,
                1, 3, 3, 2, 10, 4, 1};
        long timeOfStarted = System.nanoTime();
        plainMergeSort(data);
        System.out.println("cost of time for plain: " + (System.nanoTime() - timeOfStarted) / 1.0e9);

        forkJoinPool.invoke(new TwoChannelsMergeSort(data));
        timeOfStarted = System.nanoTime();
        System.out.println("cost of time for fork join: " + (System.nanoTime() - timeOfStarted) / 1.0e9);
    }

    private TwoChannelsMergeSort(int[] source) {
        this.source = source;
    }

    public static int[] merge(int[] left, int[] right) {
        int[] mergedArray = new int[left.length + right.length];
        int leftCursor = 0, rightCursor = 0, mergedArrayCursor = 0;
        int leftEndPoint = left.length - 1, rightEndPoint = right.length - 1;

        while (leftCursor <= leftEndPoint && rightCursor <= rightEndPoint) {
            if (left[leftCursor] <= right[rightCursor]) {
                mergedArray[mergedArrayCursor] = left[leftCursor];
                leftCursor++;
            } else {
                mergedArray[mergedArrayCursor] = right[rightCursor];
                rightCursor++;
            }

            mergedArrayCursor++;
        }

        if (rightCursor > rightEndPoint) {
            System.arraycopy(left, leftCursor, mergedArray, mergedArrayCursor, leftEndPoint - leftCursor + 1);
        } else {
            System.arraycopy(right, rightCursor, mergedArray, mergedArrayCursor, rightEndPoint - rightCursor + 1);
        }

        return mergedArray;
    }

    public static int[] plainMergeSort(int[] source) throws InterruptedException, ExecutionException, TimeoutException {
        if (source.length <= 1) {
            return source;
        }

        int lengthOfLeft = source.length / CHANNELS;
        int lengthOfRight = source.length - lengthOfLeft;
        int[] leftOfSource = new int[lengthOfLeft];
        int[] rightOfSource = new int[lengthOfRight];

        System.arraycopy(source, 0, leftOfSource, 0, lengthOfLeft);
        System.arraycopy(source, lengthOfLeft, rightOfSource, 0, lengthOfRight);

        return merge(plainMergeSort(leftOfSource), plainMergeSort(rightOfSource));
    }

    @Override
    protected int[] compute() {
        if (source.length <= 1) {
            return source;
        }

        int lengthOfLeft = source.length / CHANNELS;
        int lengthOfRight = source.length - lengthOfLeft;
        int[] leftOfSource = new int[lengthOfLeft];
        int[] rightOfSource = new int[lengthOfRight];

        System.arraycopy(source, 0, leftOfSource, 0, lengthOfLeft);
        System.arraycopy(source, lengthOfLeft, rightOfSource, 0, lengthOfRight);

        return merge(new TwoChannelsMergeSort(leftOfSource).invoke(), new TwoChannelsMergeSort(rightOfSource).invoke());
    }
}
