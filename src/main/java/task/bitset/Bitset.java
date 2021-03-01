package task.bitset;

import java.util.NoSuchElementException;

public class Bitset {
    private int[] bits;
    private int size;

    public Bitset() {
        size = 32;
        bits = new int[32];
    }

    public Bitset(int size) {
        if (size < 0 || size > 32) throw new IllegalArgumentException("Invalid array length value");
        this.size = size;
        bits = new int[size];
    }

    public Bitset(int[] arr) {
        sort(arr);
        size = arr[arr.length - 1] + 1;
        bits = new int[size];
        for (int j : arr) {
            bits[j] = 1;
        }
    }

    public void set(int index) {
        checkIndex(index);
        bits[index] = 1;
    }

    public void set(int index, boolean v) {
        checkIndex(index);
        if (v) bits[index] = 1;
        else bits[index] = 0;
    }

    public void delete(int index) {
        checkIndex(index);
        bits[index] = 0;
    }

    public void delete(int start, int end) {
        checkIndex(start); checkIndex(end);
        if (start > end) throw new IllegalArgumentException("Invalid input data");
        for (int i = start; i <= end; i++) {
            bits[i] = 0;
        }
    }

    public void delete(int[] indexes) {
        for (int index : indexes) {
            delete(index);
        }
    }

    public Bitset intersection(Bitset bitset) {
        StringBuilder s = new StringBuilder();
        int minLength = Math.min(length(), bitset.length());
        for (int i = 0; i < minLength; i++) {
            if (bits[i] == 1 && bitset.bits[i] == 1) {
                s.append(i).append(" ");
            }
        }
        if (s.length() == 0) return new Bitset(0);
        String[] split = s.toString().split(" ");
        Bitset res = new Bitset(Integer.parseInt(split[split.length - 1]) + 1);
        for (int i = 0; i < split.length - 1; i++) {
            res.bits[Integer.parseInt(split[i])] = 1;
        }
        res.bits[res.size - 1] = 1;
        return res;
    }

    public Bitset union(Bitset bitset) {
        int lengthThis = length();
        int lengthBitset = bitset.length();
        int minLength = Math.min(lengthThis, lengthBitset);
        Bitset res = new Bitset(Math.max(lengthThis, lengthBitset));
        for (int i = 0; i < minLength; i++) {
            if (bits[i] == 1 || bitset.bits[i] == 1) res.bits[i] = 1;
        }
        if (lengthBitset == minLength) {
            for (int i = minLength; i < lengthThis; i++) {
                if (bits[i] == 1) res.bits[i] = 1;
            }
        } else {
            for (int i = minLength; i < lengthBitset; i++) {
                if (bitset.bits[i] == 1) res.bits[i] = 1;
            }
        }
        return res;
    }

    public Bitset complement(Bitset bitset) {
        int lengthThis = length();
        int lengthBitset = bitset.length();
        if (lengthThis > lengthBitset) {
            throw new IllegalArgumentException("The first bitset is not a subset of the second");
        }
        Bitset res = new Bitset(lengthBitset);
        for (int i = 0; i < lengthThis; i++) {
            if (bits[i] == 1 && bitset.bits[i] == 0) {
                throw new IllegalArgumentException("The first bitset is not a subset of the second");
            }
            if (bits[i] == 0 && bitset.bits[i] == 1) res.bits[i] = 1;
        }
        for (int i = lengthThis; i < lengthBitset; i++) {
            if (bitset.bits[i] == 1) res.bits[i] = 1;
        }
        return res;
    }

    public boolean contains(int index) {
        checkIndex(index);
        return bits[index] != 0;
    }

    public boolean contains(int[] indexes) {
        for (int index : indexes) {
            if (!contains(index)) return false;
        }
        return true;
    }

    public int size() {
        return size;
    }

    public int length() {
        int i;
        for (i = size - 1; i >= 0 && bits[i] == 0 ; i--) ;
        return Math.max(i + 1, 0);
    }

    private void sort(int[] arr) {
        int n = arr.length;
        for (int step = n / 2; step > 0; step /= 2) {
            for (int i = step; i < n; i++) {
                for (int j = i - step; j >= 0 && arr[j] > arr[j + step]; j -= step) {
                    int x = arr[j];
                    arr[j] = arr[j + step];
                    arr[j + step] = x;
                }
            }
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IllegalArgumentException("Invalid array index value");
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            if (bits[i] == 1) System.out.print(i + " ");
        }
    }

    public class Iterator implements java.util.Iterator<Integer> {
        private int index;

        public Iterator() {
            index = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex() != size;
        }

        @Override
        public Integer next() {
            return nextOrPrevious(nextIndex());
        }

        public boolean hasPrevious() {
            return previousIndex() != -1;
        }

        public Integer previous() {
            return nextOrPrevious(previousIndex());
        }

        public int nextIndex() {
            for (int i = index + 1; i < size; i++) {
                if (bits[i] == 1) {
                    return i;
                }
            }
            return size;
        }

        public int previousIndex() {
            for (int i = index - 1; i >= 0; i--) {
                if (bits[i] == 1) {
                    return i;
                }
            }
            return -1;
        }

        private Integer nextOrPrevious(int i) {
            if (i != size && i != -1) {
                index = i;
                return i;
            }
            throw new NoSuchElementException("The iteration has no more elements");
        }

        @Override
        public void remove() {
            if (index != -1 && bits[index] == 1) {
                bits[index] = 0;
            } else throw new IllegalStateException("The next method has not yet been called, or the remove method " +
                    "has already been called after the last call to the next method");
        }

        public void add() {
            if (index < size - 1 && bits[index + 1] == 0) bits[index + 1] = 1;
            else throw new IllegalStateException("Cannot add a new element int the iteration");
        }
    }
}
