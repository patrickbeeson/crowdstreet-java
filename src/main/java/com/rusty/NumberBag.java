package com.rusty;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class allows easy construction of a distribution of numbers by specifying
 * value / amount pairs, where each 'value' is included 'amount' times in the bag.
 *
 * Once all the values have been added to the bag, call a method to return the
 * values in a particular way.
 */
public class NumberBag {

    private final HashMap<Integer, Integer> distribution;

    public NumberBag() {
        distribution = new HashMap<>();
    }

    public void addNumbers(int value, int amount) {
        distribution.put(value, amount);
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
