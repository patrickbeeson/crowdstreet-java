package com.rusty;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NumberBagTest {

    @Test
    public void valuesSortedByAmountTest() {
        NumberBag bag = new NumberBag();
        bag.addNumbers(7, 1); // add one 7
        bag.addNumbers(5, 4); // add four 5's
        bag.addNumbers(9, 3); // add three 9's

        // should sort by largest amount (not value) and from high to low
        List<Integer> values = bag.getValuesSortedByAmount();
        assert (values.equals(new LinkedList<>(Arrays.asList(5, 5, 5, 5, 9, 9, 9, 7))));
    }

}