package task.bitset;

import org.junit.Assert;
import org.junit.Test;

public class BitsetTest {
    Bitset bitsetTest1 = new Bitset(new int[]{1, 2, 4, 6, 7, 8, 10});
    Bitset bitsetTest2 = new Bitset(new int[]{0, 1, 4, 5, 7, 8});

    private void assertEquality(Bitset expected, Bitset actual) {
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.contains(i), actual.contains(i));
        }
    }

    @Test
    public void set() {
        Bitset bitsetTest = new Bitset(new int[]{1, 2, 4});
        bitsetTest.set(0);
        assertEquality(new Bitset(new int[]{0, 1, 2, 4}), bitsetTest);
        bitsetTest.set(1, false);
        assertEquality(new Bitset(new int[]{0, 2, 4}), bitsetTest);
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest.set(-5));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest.set(50, true));
    }

    @Test
    public void delete() {
        Bitset bitsetTest = new Bitset(new int[]{0, 1, 6, 4});
        bitsetTest.delete(4);
        assertEquality(new Bitset(new int[]{0, 1, 6}), bitsetTest);
        bitsetTest.set(2); bitsetTest.set(3); bitsetTest.set(4);
        bitsetTest.delete(1, 4);
        assertEquality(new Bitset(new int[]{0, 6}), bitsetTest);
        bitsetTest.delete(new int[]{6, 0});
        assertEquality(new Bitset(7), bitsetTest);
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest.delete(-3));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest.delete(45));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest.delete(5, 3));
    }

    @Test
    public void intersection() {
        assertEquality(new Bitset(new int[]{1, 4, 7, 8}), bitsetTest1.intersection(bitsetTest2));
        assertEquality(new Bitset(0), new Bitset(5).intersection(new Bitset(3)));
        assertEquality(new Bitset(new int[]{0, 1}),
                new Bitset(new int[]{1, 0}).intersection(new Bitset(new int[]{0, 4, 1})));
    }

    @Test
    public void union() {
        assertEquality(new Bitset(new int[]{0, 1, 2, 4, 5, 6, 7, 8, 10}), bitsetTest1.union(bitsetTest2));
        assertEquality(new Bitset(new int[]{2, 5, 6}), new Bitset(6).union(new Bitset(new int[]{6, 2, 5})));
        assertEquality(new Bitset(0), new Bitset(3).union(new Bitset()));
    }

    @Test
    public void complement() {
        assertEquality(new Bitset(new int[]{5, 7, 8}), new Bitset(new int[]{1, 0, 4}).complement(bitsetTest2));
        assertEquality(new Bitset(0), new Bitset().complement(new Bitset(4)));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest2.complement(bitsetTest1));
    }

    @Test
    public void contains() {
        Assert.assertTrue(bitsetTest1.contains(6));
        Assert.assertFalse(bitsetTest2.contains(2));
        Assert.assertTrue(bitsetTest1.contains(new int[]{8, 2, 7}));
        Assert.assertFalse(bitsetTest2.contains(new int[]{0, 1, 2}));
    }
}
