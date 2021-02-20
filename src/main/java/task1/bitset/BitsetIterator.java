package task1.bitset;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class BitsetIterator implements ListIterator<Integer> {
    private int index = -1;
    private int size;
    private Integer[] set;

    public BitsetIterator(Bitset bitset) {
        this.size = bitset.size;
        this.set = bitset.set;
    }

    @Override
    public boolean hasNext() {
        return nextIndex() != size;
    }

    @Override
    public boolean hasPrevious() {
        return previousIndex() != -1;
    }

    @Override
    public int nextIndex() {
        for (int i = index + 1; i < size; i++) {
            if (set[i] != null) {
                return i;
            }
        }
        return size;
    }

    @Override
    public int previousIndex() {
        for (int i = index - 1; i >= 0; i--) {
            if (set[i] != null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Integer next() {
        return nextOrPrevious(nextIndex());
    }

    @Override
    public Integer previous() {
        return nextOrPrevious(previousIndex());
    }

    private Integer nextOrPrevious(int i) {
        if (i != size && i != -1) {
            index = i;
            return set[i];
        }
        throw new NoSuchElementException("The iteration has no more elements");
    }

    public void remove() {
        if (index != -1 && set[index] != null) {
            set[index] = null;
        } else throw new IllegalStateException("The next method has not yet been called, or the remove method " +
                "has already been called after the last call to the next method");
    }

    @Override
    public void set(Integer integer) {
        if (index != -1 && set[index] != null) {
            set[index] = integer;
        } else throw new IllegalStateException("Neither next nor previous have been called, or the remove " +
                "has been called after the last call to next or previous");
    }

    @Override
    public void add(Integer integer) {
        if (index < size - 1 && set[index + 1] == null) set[index + 1] = integer;
        else throw new IllegalStateException("Cannot add a new element int the iteration");
    }
}
