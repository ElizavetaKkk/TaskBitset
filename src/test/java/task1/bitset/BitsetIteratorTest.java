package task1.bitset;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BitsetIteratorTest {
    Bitset bitset = new Bitset(new Integer[]{-13, null, -20, 1});

    private void assertEquality(Bitset expected, Bitset actual) {
        Assert.assertEquals(expected.size, actual.size);
        Assert.assertArrayEquals(expected.set, actual.set);
    }

    @Test
    public void hasNext() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertTrue(iterator.hasNext());
        iterator.next(); iterator.next(); iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void nextIndex() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertEquals(0, iterator.nextIndex());
        iterator.next();
        Assert.assertEquals(2, iterator.nextIndex());
        iterator.next(); iterator.next();
        Assert.assertEquals(4, iterator.nextIndex());
    }

    @Test
    public void next() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertEquals(bitset.set[0], iterator.next());
        Assert.assertEquals(bitset.set[2], iterator.next());
        iterator.next();
        Assert.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void hasPrevious() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertTrue(iterator.hasPrevious());
    }

    @Test
    public void previousIndex() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertEquals(-1, iterator.previousIndex());
        iterator.next(); iterator.next();
        Assert.assertEquals(0, iterator.previousIndex());
    }

    @Test
    public void previous() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertThrows(NoSuchElementException.class, iterator::previous);
        iterator.next(); iterator.next();
        Assert.assertEquals(bitset.set[0], iterator.previous());
    }

    @Test
    public void remove() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next(); iterator.remove();
        Assert.assertNull(bitset.set[0]);
        Assert.assertThrows(IllegalStateException.class, iterator::remove);
        bitset.add(0, -13);
    }

    @Test
    public void set() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        Assert.assertThrows(IllegalStateException.class, () -> iterator.set(8));
        iterator.next(); iterator.set(9);
        Assert.assertEquals(9, (int) bitset.set[0]);
        iterator.next(); iterator.remove();
        Assert.assertThrows(IllegalStateException.class, () -> iterator.set(4));
    }

    @Test
    public void add() {
        BitsetIterator iterator = new BitsetIterator(bitset);
        iterator.next(); iterator.add(5);
        Assert.assertEquals(5, (int) bitset.set[1]);
        iterator.next();
        Assert.assertThrows(IllegalStateException.class, () -> iterator.add(6));
        iterator.next(); iterator.next();
        Assert.assertThrows(IllegalStateException.class, () -> iterator.add(7));
    }
}
