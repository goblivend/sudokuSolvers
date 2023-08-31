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
        List<Point> inputs = Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(5, 2));
        List<Integer> expected = Arrays.asList(0, 0, 1);

        List<Integer> actual = inputs.stream().map(e -> QuantumCell.unsetIthBit(e.x, e.y)).toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    public void testGetIthBit() {
        List<Point> inputs = Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(5, 2), new Point(3, 3));
        List<Integer> expected = Arrays.asList(0, 1, 1, 0);

        List<Integer> actual = inputs.stream().map(e -> QuantumCell.getIthBit(e.x, e.y)).toList();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }


    public void testIsCompletableTrueUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Assert.assertTrue(qc.isCompletable());
    }

    public void testIsCompletableTrueKnown() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        Assert.assertTrue(qc.isCompletable());
    }

    public void testGetEntropyKnown() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        Assert.assertEquals(1, qc.getEntropy());
    }

    public void testGetEntropyUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Assert.assertEquals(9, qc.getEntropy());
    }

    public void testGetValueKnown() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        Integer expected = 4;
        Assert.assertEquals(expected, qc.getValue());
    }
    public void testGetValueUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Assert.assertNull(qc.getValue());
    }

    public void testGetPossibilitiesKnown() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        Integer expected = 8;
        Assert.assertEquals(expected, qc.getPossibilities());
    }

    public void testGetPossibilitiesUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Integer expected = 511;
        Assert.assertEquals(expected, qc.getPossibilities());
    }

    public void testGetPossibilitiesKnownReseted() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        qc.resetPossibilities();
        Integer expected = 0;
        Assert.assertEquals(expected, qc.getPossibilities());
    }


    public void testSetPossibilityKnownNotSet() {
        QuantumCell qc = new QuantumCell(null, 9, 7);
        Integer expected = 65;
        qc.setPossibility(1);
        Assert.assertEquals(expected, qc.getPossibilities());
    }

    public void testSetPossibilityKnownSet() {
        QuantumCell qc = new QuantumCell(null, 9, 7);
        Integer expected = 64;
        qc.setPossibility(7);
        Assert.assertEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }
    public void testSetPossibilityUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Integer expected = 511;
        qc.setPossibility(3);
        Assert.assertEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }


    public void testUnsetPossibilityFromKnownDifferent() {
        QuantumCell qc = new QuantumCell(null, 9, 3);
        Integer expected = 4;
        qc.unsetPossibility(1);
        Assert.assertEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }

    public void testUnsetPossibilityFromKnownSame() {
        QuantumCell qc = new QuantumCell(null, 9, 3);
        Integer expected = 0;
        qc.unsetPossibility(3);
        Assert.assertEquals(expected, qc.getPossibilities());
        Assert.assertFalse(qc.isCompletable());
    }

    public void testUnsetPossibilityFromUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        Integer expected = 510;
        qc.unsetPossibility(1);
        Assert.assertEquals(expected, qc.getPossibilities());

        Assert.assertTrue(qc.isCompletable());
    }

}
