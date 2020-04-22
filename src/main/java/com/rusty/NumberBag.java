package com.rusty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class allows easy construction of a distribution of numbers by specifying
 * (value, amount) pairs, where each 'value' is included 'amount' times in the bag.
 */
public class NumberBag {

    private final HashMap<Integer, Integer> distribution;

    public NumberBag() {
        distribution = new HashMap<>();
    }

    /**
     * Specify that 'value' should appear in the distribution 'amount' times.
     */
    public void addNumbers(int value, int amount) {
        distribution.put(value, amount);
    }

    /**
     * Get the total count of all values in the bag.
     */
    public int getSize() {
        return distribution.values().stream().reduce(0, (a, b) -> a + b);
    }

    /**
     * Get the value which appears the most in the bag.
     */
    public int getValueOfLargestAmount() {
        return distribution.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get().getKey();
    }

    /**
     * Generate a list of all the values in the bag, sorted by amount, from high to low.
     * The order of ties doesn't matter.
     *
     * The default sort is low to high, so insert each value into the beginning of the list
     * for efficiency, and to maintain the desired order of highest to lowest.
     */
    public List<Integer> getValuesSortedByAmount() {
        List<Integer> values = new LinkedList<>();
        distribution.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
                    for (int i = 0; i < e.getValue(); i++) {
                        values.add(0, e.getKey());
                    }
                }
        );
        return values;
    }

}
