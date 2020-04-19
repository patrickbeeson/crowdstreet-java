package com.rusty;

import org.junit.Test;

import java.util.Arrays;

public class DataGeneratorTest {

    @Test
    public void initializeTest() {
        NumberBag bag = new NumberBag();
        bag.addNumbers(7, 1); // add one 7
        bag.addNumbers(5, 4); // add four 5's
        bag.addNumbers(9, 3); // add three 9's

        DataGenerator dataGenerator = new DataGenerator(bag);
        int[] list = dataGenerator.initializeData();

        // the list from the bag: [5, 5, 5, 5, 9, 9, 9, 7]
        // split in half: [5, 5, 5, 5] [9, 9, 9, 7]
        // zipped together: [5, 9, 5, 9, 5, 9, 5, 7]
        assert Arrays.equals(list, new int[]{5, 9, 5, 9, 5, 9, 5, 7});
    }

    @Test
    public void randomizeTest() {
        NumberBag bag = new NumberBag();
        bag.addNumbers(7, 70);
        bag.addNumbers(5, 100);
        bag.addNumbers(9, 30);
        bag.addNumbers(6, 20);

        DataGenerator dataGenerator = new DataGenerator(bag);
        int[] list = dataGenerator.initializeData();
        assert (validState(list));

        for (int i = 0; i < 1000; i++) {
            int[] randomized = dataGenerator.randomizeData(1000);
            assert (validState(randomized));
        }
    }

    @Test
    public void canSwapTest() {
        int[] list = new int[]{1, 2, 1, 2, 1, 3, 1, 3};
        DataGenerator dataGenerator = new DataGenerator(list);

        // valid swaps
        assert (dataGenerator.canSwap(0, 0));
        assert (dataGenerator.canSwap(0, 2));
        assert (dataGenerator.canSwap(0, 4));
        assert (dataGenerator.canSwap(0, 6));
        assert (dataGenerator.canSwap(1, 3));
        assert (dataGenerator.canSwap(1, 5));
        assert (dataGenerator.canSwap(1, 7));
        assert (dataGenerator.canSwap(2, 4));
        assert (dataGenerator.canSwap(2, 6));
        assert (dataGenerator.canSwap(3, 5));
        assert (dataGenerator.canSwap(7, 7));

        // invalid swaps
        assert (!dataGenerator.canSwap(0, 1));
        assert (!dataGenerator.canSwap(0, 5));
        assert (!dataGenerator.canSwap(0, 7));
        assert (!dataGenerator.canSwap(1, 2));
        assert (!dataGenerator.canSwap(1, 4));
        assert (!dataGenerator.canSwap(3, 4));
        assert (!dataGenerator.canSwap(3, 6));
        assert (!dataGenerator.canSwap(4, 5));
        assert (!dataGenerator.canSwap(4, 7));
        assert (!dataGenerator.canSwap(5, 6));
        assert (!dataGenerator.canSwap(6, 7));
    }

    private boolean validState(int[] data) {
        int a = data[0];
        for (int i = 1; i < data.length; i++) {
            if (a == data[i]) {
                return false;
            } else {
                a = data[i];
            }
        }
        return true;
    }

}