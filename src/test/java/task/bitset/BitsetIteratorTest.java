package task.bitset;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class BitsetIteratorTest {
    Bitset bitset = new Bitset(new int[]{0, 2, 3, 6});

    @Test
    public void hasNext() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator.next(); iterator.next(); iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void nextIndex() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertEquals(0, iterator.nextIndex());
        iterator.next();
        Assert.assertEquals(2, iterator.nextIndex());
        iterator.next(); iterator.next(); iterator.next();
        Assert.assertEquals(32, iterator.nextIndex());
    }

    @Test
    public void next() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(2, (int) iterator.next());
        iterator.next(); iterator.next();
        Assert.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void hasPrevious() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next();
        Assert.assertFalse(iterator.hasPrevious());
        iterator.next(); iterator.next();
        Assert.assertTrue(iterator.hasPrevious());
    }

    @Test
    public void previousIndex() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertEquals(-1, iterator.previousIndex());
        iterator.next();
        Assert.assertEquals(-1, iterator.previousIndex());
        iterator.next(); iterator.next();
        Assert.assertEquals(2, iterator.previousIndex());
    }

    @Test
    public void previous() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertThrows(NoSuchElementException.class, iterator::previous);
        iterator.next(); iterator.next();
        Assert.assertEquals(0, (int) iterator.previous());
        iterator.next(); iterator.next();
        Assert.assertEquals(2, (int) iterator.previous());
    }

    @Test
    public void remove() {
        Bitset.Iterator iterator = bitset.new Iterator();
        Assert.assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next(); iterator.remove();
        Assert.assertFalse(bitset.contains(0));
        Assert.assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    public void add() {
        Bitset.Iterator iterator = bitset.new Iterator();
        iterator.next(); iterator.add();
        Assert.assertTrue(bitset.contains(1));
        Assert.assertThrows(IllegalStateException.class, iterator::add);
    }
}
