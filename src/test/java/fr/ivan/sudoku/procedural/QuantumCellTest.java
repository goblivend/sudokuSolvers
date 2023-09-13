package fr.ivan.sudoku.procedural;

import junit.framework.TestCase;
import org.junit.Assert;

public class QuantumCellTest extends TestCase {

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
        boolean[] expected = new boolean[]{false,false,false,true,false,false,false,false,false};
        Assert.assertArrayEquals(expected, qc.getPossibilities());
    }

    public void testGetPossibilitiesUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        boolean[] expected = new boolean[]{true,true,true,true,true,true,true,true,true};
        Assert.assertArrayEquals(expected, qc.getPossibilities());
    }

    public void testGetPossibilitiesKnownReseted() {
        QuantumCell qc = new QuantumCell(null, 9, 4);
        qc.resetPossibilities();
        boolean[] expected = new boolean[]{false,false,false,false,false,false,false,false,false};
        Assert.assertArrayEquals(expected, qc.getPossibilities());
    }


    public void testSetPossibilityKnownNotSet() {
        QuantumCell qc = new QuantumCell(null, 9, 7);
        boolean[] expected = new boolean[]{true,false,false,false,false,false,true,false,false};
        qc.setPossibility(1);
        Assert.assertArrayEquals(expected, qc.getPossibilities());
    }

    public void testSetPossibilityKnownSet() {
        QuantumCell qc = new QuantumCell(null, 9, 7);
        boolean[] expected = new boolean[]{false,false,false,false,false,false,true,false,false};
        qc.setPossibility(7);
        Assert.assertArrayEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }
    public void testSetPossibilityUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        boolean[] expected = new boolean[]{true,true,true,true,true,true,true,true,true};
        qc.setPossibility(3);
        Assert.assertArrayEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }


    public void testUnsetPossibilityFromKnownDifferent() {
        QuantumCell qc = new QuantumCell(null, 9, 3);
        boolean[] expected = new boolean[]{false,false,true,false,false,false,false,false,false};
        qc.unsetPossibility(1);
        Assert.assertArrayEquals(expected, qc.getPossibilities());
        Assert.assertTrue(qc.isCompletable());
    }

    public void testUnsetPossibilityFromKnownSame() {
        QuantumCell qc = new QuantumCell(null, 9, 3);
        boolean[] expected = new boolean[]{false,false,false,false,false,false,false,false,false};
        qc.unsetPossibility(3);
        Assert.assertArrayEquals(expected, qc.getPossibilities());
        Assert.assertFalse(qc.isCompletable());
    }

    public void testUnsetPossibilityFromUnknown() {
        QuantumCell qc = new QuantumCell(null, 9, null);
        boolean[] expected = new boolean[]{false,true,true,true,true,true,true,true,true};
        qc.unsetPossibility(1);
        Assert.assertArrayEquals(expected, qc.getPossibilities());

        Assert.assertTrue(qc.isCompletable());
    }

}
