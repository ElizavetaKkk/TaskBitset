package task1.bitset;

import org.junit.Assert;
import org.junit.Test;

public class BitsetTest {
    Bitset bitsetTest1 = new Bitset(new Integer[]{10, null, -5, 8, null, 0, -20, 12, 1});
    Bitset bitsetTest2 = new Bitset(new Integer[]{-5, 0, 11, -1, 8});

    private void assertEquality(Bitset expected, Bitset actual) {
        Assert.assertEquals(expected.size, actual.size);
        Assert.assertArrayEquals(expected.set, actual.set);
    }

    @Test
    public void add() {
        bitsetTest2.add(1, 15);
        assertEquality(new Bitset(new Integer[]{-5, 15, 11, -1, 8}), bitsetTest2);
        bitsetTest2.add(1, 0);
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest2.add(5, 10));
    }

    @Test
    public void delete() {
        bitsetTest2.delete(0);
        assertEquality(new Bitset(new Integer[]{null, 0, 11, -1, 8}), bitsetTest2);
        bitsetTest2.add(0, -5);
        bitsetTest2.delete(new int[]{0, 2, 4});
        assertEquality(new Bitset(new Integer[]{null, 0, null, -1, null}), bitsetTest2);
        bitsetTest2.add(0, -5); bitsetTest2.add(2, 11); bitsetTest2.add(4, 8);
        Assert.assertThrows(IllegalStateException.class, () -> bitsetTest1.delete(1));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest1.delete(-1));
    }

    @Test
    public void get() {
        Assert.assertEquals(bitsetTest1.set[2], bitsetTest1.get(2));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitsetTest1.get(bitsetTest1.size));
        Assert.assertThrows(IllegalStateException.class, () -> bitsetTest1.get(4));
    }

    @Test
    public void contains() {
        Assert.assertTrue(bitsetTest1.contains(-20));
        Assert.assertFalse(bitsetTest1.contains(220));
        Assert.assertTrue(bitsetTest1.contains(new int[]{0, -5, 12}));
        Assert.assertFalse(bitsetTest1.contains(new int[]{8, 1, 100}));
    }

    @Test
    public void intersection() {
        assertEquality(new Bitset(new Integer[]{-5, 0, 8}), bitsetTest1.intersection(bitsetTest2));
        assertEquality(
                new Bitset(new Integer[0]),
                new Bitset(new Integer[]{1, 2, 3}).intersection(new Bitset(new Integer[]{4, 5, 6, 7}))
        );
        Bitset testInt = new Bitset(new Integer[]{1, 2, 3});
        assertEquality(
                testInt,
                new Bitset(new Integer[]{2, 1, 3}).intersection(new Bitset(new Integer[]{1, 4, 3, 2}))
        );
        assertEquality(
                testInt,
                new Bitset(new Integer[]{-8, 3, 9, 1, 2}).intersection(new Bitset(new Integer[]{3, 2, 0, 1}))
        );
        assertEquality(new Bitset(), new Bitset(new Integer[]{null, null}).intersection(new Bitset()));
    }

    @Test
    public void union() {
        assertEquality(
                new Bitset(new Integer[]{-20, -5, -1, 0, 1, 8, 10, 11, 12}),
                bitsetTest1.union(bitsetTest2)
        );
        assertEquality(
                new Bitset(new Integer[]{1, 2, 3, 4}),
                new Bitset(new Integer[]{2, null, 1}).union(new Bitset(new Integer[]{null, 4, 3, null}))
        );
        assertEquality(new Bitset(), new Bitset().union(new Bitset(new Integer[]{null, null})));
    }

    @Test
    public void complement() {
        bitsetTest2.delete(2); bitsetTest2.delete(3);
        assertEquality(new Bitset(new Integer[]{-20, 1, 10, 12}), bitsetTest1.complement(bitsetTest2));
        bitsetTest2.add(2, 11); bitsetTest2.add(3, -1);
        assertEquality(new Bitset(), new Bitset().intersection(new Bitset()));
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> new Bitset(new Integer[]{-5, 6}).complement(new Bitset(new Integer[]{6, 8, null}))
        );
    }
}
