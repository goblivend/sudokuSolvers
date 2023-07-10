package fr.ivan.sudoku.bitProcedural;

import fr.ivan.profiler.Pair;
import junit.framework.TestCase;
import org.junit.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class QuantumCellTest extends TestCase {

    public void testGetMaxIntFromSIze() {
        List<Integer> inputs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> expected = Arrays.asList(1, 3, 7, 15, 31, 63, 127, 255, 511);

        List<Integer> actual = inputs.stream().map(QuantumCell::getMaxIntFromSize).toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testGetMaskBit() {
        List<Integer> inputs = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> expected = Arrays.asList(1, 2, 4, 8, 16, 32, 64, 128, 256, 512);

        List<Integer> actual = inputs.stream().map(QuantumCell::getMaskBit).toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testSetIthBit() {
        List<Point> inputs = Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(1, 2));
        List<Integer> expected = Arrays.asList(1, 1, 5);

        List<Integer> actual = inputs.stream().map(e -> QuantumCell.setIthBit(e.x, e.y)).toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testUnsetIthBit() {
    }

    public void testGetIthBit() {
    }
}