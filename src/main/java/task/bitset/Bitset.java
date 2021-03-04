package task.bitset;

import java.util.NoSuchElementException;

public class Bitset {
    private int[] bits;
    private int size;

    public Bitset() {
        size = 1;
        bits = new int[1];
    }

    public Bitset(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Invalid array length value");
        if ((amount & 0x1f) != 0) size = 1;
        size += amount >>> 5;
        bits = new int[size];
    }

    public Bitset(int[] arr) {
        sort(arr);
        int max = arr[arr.length - 1] + 1;
        if (max < 0) throw new IllegalArgumentException("Invalid array length value");
        if ((max & 0x1f) != 0) size = 1;
        size += max >>> 5;
        bits = new int[size];
        for (int j : arr) {
            set(j);
        }
    }

    public void set(int index) {
        int ind = index >> 5;
        checkIndex(ind);
        bits[ind] |= 1 << index;
    }

    public void set(int index, boolean v) {
        if (v) set(index);
        else delete(index);
    }

    public void delete(int index) {
        int ind = index >> 5;
        checkIndex(ind);
        bits[ind] &= ~(1 << index);
    }

    public void delete(int start, int end) {
        if (start < 0 || start > end) throw new IllegalArgumentException("Invalid input data");
        int startInd = start >> 5;
        int endInd = end >> 5;
        checkIndex(endInd);
        if (startInd != endInd) {
            for (int i = startInd; i < endInd; i++) {
                bits[i] = 0;
            }
            bits[endInd] &= -1 << end + 1;
            bits[startInd] &= (1 << start) - 1;
        } else {
            bits[startInd] &= (-1 << end + 1) | ((1 << start) - 1);
        }
    }

    public void delete(int[] indexes) {
        for (int index : indexes) {
            delete(index);
        }
    }

    public Bitset intersection(Bitset bitset) {
        int i = Math.min(length(), bitset.length()) - 1;
        for (; i >= 0 && (bits[i] & bitset.bits[i]) == 0; i--) ;
        Bitset res = new Bitset(i + 1);
        for (int j = i; j >= 0; j--) {
            res.bits[i] = bits[i] & bitset.bits[i];
        }
        return res;
    }

    public Bitset union(Bitset bitset) {
        int maxLength = Math.max(length(), bitset.length());
        Bitset res = new Bitset(maxLength);
        for (int i = maxLength - 1; i >= 0; i--) {
            res.bits[i] = bits[i] | bitset.bits[i];
        }
        return res;
    }

    public Bitset complement(Bitset bitset) {
        int lengthThis = length();
        int lengthBitset = bitset.length();
        if (lengthThis > lengthBitset ||
                (lengthThis != 0 && lengthBitset != 0 && bits[lengthThis - 1] > bitset.bits[lengthBitset - 1])) {
            throw new IllegalArgumentException("The first bitset is not a subset of the second");
        }
        Bitset res = new Bitset(lengthBitset);
        int i;
        for (i = lengthBitset - 1; i >= lengthThis ; i--) {
            res.bits[i] = bitset.bits[i];
        }
        for (; i >= 0 ; i--) {
            int el = bits[i];
            if (el != (el & (bitset.bits[i] & ~(-1 << (int) (Math.log(el) / Math.log(2)) + 1))))
                throw new IllegalArgumentException("The first bitset is not a subset of the second");
            res.bits[i] = bitset.bits[i] - bits[i];
        }
        return res;
    }

    public boolean contains(int index) {
        int ind = index >> 5;
        checkIndex(ind);
        return (bits[ind] & (1 << index)) != 0;
    }

    public boolean contains(int[] indexes) {
        for (int index : indexes) {
            if (!contains(index)) return false;
        }
        return true;
    }

    public int size() {
        return size << 5;
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
        if (index >= size || index < 0) throw new IllegalArgumentException("Invalid input data");
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
            return nextIndex() != size << 5;
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
            for (int i = index + 1; i < size << 5; i++) {
                if (((bits[i >> 5] >> i) & 1) == 1) {
                    return i;
                }
            }
            return size * 32;
        }

        public int previousIndex() {
            for (int i = index - 1; i >= 0; i--) {
                if (((bits[i >> 5] >> i) & 1) == 1) {
                    return i;
                }
            }
            return -1;
        }

        private Integer nextOrPrevious(int i) {
            if (i != size << 5 && i != -1) {
                index = i;
                return i;
            }
            throw new NoSuchElementException("The iteration has no more elements");
        }

        @Override
        public void remove() {
            if (index != -1 && ((bits[index >> 5] >> index) & 1) == 1) {
                bits[index >> 5] &= ~(1 << index);
            } else throw new IllegalStateException("The next method has not yet been called, or the remove method " +
                    "has already been called after the last call to the next method");
        }

        public void add() {
            if (index < size << 5 - 1 && ((bits[(index + 1) >> 5] >> (index + 1)) & 1) == 0) {
                bits[(index + 1) >> 5] |= 1 << (index + 1);
            }
            else throw new IllegalStateException("Cannot add a new element int the iteration");
        }
    }
}
