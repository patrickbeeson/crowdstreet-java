package com.rusty;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class builds a randomized data set with a specific distribution of numbers.
 *
 * The distribution can be specified using a NumberBag, which accepts pairs
 * describing what value to add, and how many times it should appear.
 */
public class DataGenerator {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private NumberBag bag;
    private int[] dataSet;

    /**
     * Initialize data with a number bag.
     */
    public DataGenerator(NumberBag bag) {
        this.bag = bag;
    }

    /**
     * Initialize data via provided array (for testing).
     */
    DataGenerator(int[] dataSet) {
        this.dataSet = dataSet;
        initialized.getAndSet(true);
    }

    /**
     * Creates a data set by constructing an initial ordering that adhere's to the
     * constraints that no adjacent elements are equal, which gets harder as fewer
     * values make up a larger majority of the total.
     *
     * The naive approach of trying to add each number randomly into a place in a
     * fixed size array can lead to situations where no solution is possible while
     * maintaining the constraints. Building the list one value at a time is also tricky.
     *
     * In this method, it gets all the values from the number bag ordered by the amount
     * of each value (highest to lowest), then splits the list equally in 2 and zips
     * them together. This method, by construction, ensures that the initial list is
     * valid, but not yet randomized.
     */
    public int[] initializeData() {
        if (initialized.get()) {
            return dataSet;
        }

        List<Integer> values = bag.getValuesSortedByAmount();

        int half = values.size() / 2;
        Iterator<Integer> firstIterator = values.subList(0, half).iterator();
        Iterator<Integer> secondIterator = values.subList(half, values.size()).iterator();

        List<Integer> data = new LinkedList<>();
        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            data.add(firstIterator.next());
            data.add(secondIterator.next());
        }

        // if there was an odd amount, there will be one last element here
        if (secondIterator.hasNext()) {
            data.add(secondIterator.next());
        }

        dataSet = data.stream().mapToInt(Integer::intValue).toArray();
        initialized.getAndSet(true);
        return dataSet;
    }

    /**
     * Attempts to swap two values at random in the data set many times.
     *
     * Note, the data set is already in a valid state after initialization, so
     * the randomization process needs to maintain a valid state.
     */
    public int[] randomizeData(int iterations) {
        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            int a = random.nextInt(dataSet.length);
            int b = random.nextInt(dataSet.length);
            swap(a, b);
        }

        return dataSet;
    }

    /**
     * Swaps two values at random, but only if after the swap, no consecutive numbers
     * are equal.
     *
     * @return true if the swap occurred
     */
    @VisibleForTesting
    protected boolean swap(int a, int b) {
        if (canSwap(a, b)) {
            int tmp = dataSet[a];
            dataSet[a] = dataSet[b];
            dataSet[b] = tmp;
        }
        return false;
    }

    /**
     * Verifies that swapping the values in spot 'a' and 'b' doesn't introduce
     * consecutive duplicates in either spot, which means checking both ways.
     *
     * @return true if swapping maintains the constraints
     */
    @VisibleForTesting
    protected boolean canSwap(int a, int b) {
        return checkNeighbors(a, dataSet[b]) && checkNeighbors(b, dataSet[a]);
    }

    /**
     * Verifies that the values of the neighbors to the left and right of 'index',
     * aren't equal to 'value'.
     *
     * @return true if the value can be inserted into the index maintaining constraints
     */
    private boolean checkNeighbors(int index, int value) {
        return (index - 1 >= 0 && dataSet[index - 1] != value && index + 1 <= dataSet.length - 1 && dataSet[index + 1] != value) ||
                (index == 0 && dataSet[index + 1] != value) || (index == dataSet.length - 1 && dataSet[index - 1] != value);
    }

    /**
     * Scan the data set for a particular value and print the line number it
     * will appear on when written out to a file.
     */
    private void findValue(int[] data, int value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == value) {
                System.out.println("Value: " + value + " found at line: " + (i + 1));
            }
        }
    }

    /**
     * Write the data set to a file, one number per line.
     */
    private void writeDataSet(String fname, int[] data) {
        Charset charset = Charset.forName("US-ASCII");
        File file = new File(fname);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
            for (int i = 0; i < data.length; i++) {
                writer.write(data[i] + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void main(String[] args) {
        // 1-12:83000, 13:1000, 14:500, 15:250, 16:100, 17:50, 18:25, 19:10, 20:5
        int[] distribution = {
                83000, 83000, 83000, 83000, 83000, 83000, 83000, 83000, 83000,
                83000, 83000, 83000, 1000, 500, 250, 100, 50, 25, 10, 5
        };

        NumberBag bag = new NumberBag();
        for (int i = 0; i < distribution.length; i++) {
            bag.addNumbers(i + 1, distribution[i]);
        }

        DataGenerator dataGenerator = new DataGenerator(bag);

        System.out.println("Initialized data:");
        int[] initializedData = dataGenerator.initializeData();
        dataGenerator.findValue(initializedData, 20);
        //dataGenerator.writeDataSet("initialized.output", initializedData);

        System.out.println("Randomized data:");
        int[] randomizedData = dataGenerator.randomizeData(10000000); // ~10x
        dataGenerator.findValue(randomizedData, 20);
        dataGenerator.writeDataSet("test.output", randomizedData);
    }

}
