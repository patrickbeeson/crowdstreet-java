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

/**
 * This class builds a randomized data set with a specific distribution of numbers.
 */
public class DataGenerator {

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
    public DataGenerator(int[] dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Creates a data set by constructing an initial ordering that adhere's to the
     * rule that no adjacent elements are equal.
     *
     * In this case, it gets all the values from the number bag ordered by the amount
     * of each number, then splits the list equally in 2 and zips them together.
     *
     * This method, by construction, ensures that the initial list is valid, but not
     * yet randomized.
     */
    public int[] initializeData() {
        if (dataSet != null) {
            return dataSet;
        }

        List<Integer> values = bag.getValuesSortedByAmount();

        int half = values.size() / 2;
        List<Integer> first = values.subList(0, half);
        List<Integer> second = values.subList(half, values.size());

        Iterator<Integer> firstIterator = first.iterator();
        Iterator<Integer> secondIterator = second.iterator();

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
        return dataSet;
    }

    /**
     * Attempts to swap two values at random in the data set.
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
     * Swaps two values at random, but only if after the swap, no consecutive numbers are equal.
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

    @VisibleForTesting
    protected boolean canSwap(int a, int b) {
        return checkNeighbors(a, dataSet[b]) && checkNeighbors(b, dataSet[a]);
    }

    /**
     * Verifies that the values of the neighbors to the left and right of 'index', aren't equal to 'value'
     */
    private boolean checkNeighbors(int index, int value) {
        return (index - 1 >= 0 && dataSet[index - 1] != value && index + 1 <= dataSet.length - 1 && dataSet[index + 1] != value) ||
                (index == 0 && dataSet[index + 1] != value) || (index == dataSet.length - 1 && dataSet[index - 1] != value);
    }

    /**
     * Scan the data set for a particular value and print the line number.
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
