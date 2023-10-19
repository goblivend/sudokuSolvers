package fr.ivan.sudoku.util;

import fr.ivan.util.Utils;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class UtilsTest extends TestCase {


    public void testHexToIntValidDec() {
        List<Character> inputs = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> actual = inputs.stream().map(Utils::HexToInt).toList();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testHexToIntValidHexUpper() {
        List<Character> inputs = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F');
        List<Integer> expected = Arrays.asList(10, 11, 12, 13, 14, 15);

        List<Integer> actual = inputs.stream().map(Utils::HexToInt).toList();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testHexToIntValidHexLower() {
        List<Character> inputs = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        List<Integer> expected = Arrays.asList(10, 11, 12, 13, 14, 15);

        List<Integer> actual = inputs.stream().map(Utils::HexToInt).toList();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testIntToChar() {
        List<Integer> inputs = Arrays.asList(null , 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Character> expected = Arrays.asList('.' , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');

        List<Character> actual = inputs.stream().map(Utils::IntToChar).toList();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}