package task1.bitset;

public class Bitset {
    int size;
    Integer[] set;

    public Bitset() {
        this.size = 0;
        set = new Integer[]{};
    }

    public Bitset(int size) {
        this.size = size;
        set = new Integer[size];
    }

    public Bitset(Integer[] set) {
        size = set.length;
        this.set = set;
    }

    public void add(int index, int element) {
        checkIndex(index);
        set[index] = element;
    }

    public void delete(int index) {
        nullCheck(index);
        set[index] = null;
    }

    public void delete(int[] indexes) {
        for (int index : indexes) {
            delete(index);
        }
    }

    public Integer get(int index) {
        nullCheck(index);
        return set[index];
    }

    public boolean contains(int element) {
        for (int i = 0; i < size; i++) {
            Integer t = set[i];
            if (t != null && element == t) return true;
        }
        return false;
    }

    public boolean contains(int[] elements) {
        for (int element : elements) {
            if (!contains(element)) return false;
        }
        return true;
    }

    public Bitset intersection(Bitset bitsetTesting) {
        return intersectionOrUnionOrComplement(bitsetTesting, 0);
    }

    public Bitset union(Bitset bitsetTesting) {
        return intersectionOrUnionOrComplement(bitsetTesting, 1);
    }

    public Bitset complement(Bitset bitsetTesting) {
        return intersectionOrUnionOrComplement(bitsetTesting, 2);
    }

    private Bitset intersectionOrUnionOrComplement(Bitset bitsetTesting, int flag) {
        Integer[] set1 = new Integer[size];
        System.arraycopy(set, 0, set1, 0, size);
        int amountOfNulls1 = insertionSort(set1);
        Integer[] set2 = new Integer[bitsetTesting.set.length];
        System.arraycopy(bitsetTesting.set, 0, set2, 0, bitsetTesting.set.length);
        int amountOfNulls2 = insertionSort(set2);
        StringBuilder s = new StringBuilder();
        int length = 0, start = 0;
        for (int i = 0; i < set2.length - amountOfNulls2; i++) { //looking for the same elements of two arrays
            Integer element = set2[i];
            int index = commonElInd(set1, element, start, set1.length - amountOfNulls1);
            if ((flag == 0 && index != -1) || (flag == 1 && index == -1)) { //branch for intersection() & union()
                s.append(element).append(" ");
                length++;
            } else if (flag == 2) { //branch for complement()
                if (index != -1) {
                    set1[index] = null;
                } else throw new IllegalArgumentException("One set is not a subset of another");
            }
            start = index + 1;
        }
        String[] split = s.toString().split(" ");
        Bitset res;
        switch (flag) {
            case 0 -> { //intersection()
                res = new Bitset(length);
                for (int i = 0; i < length; i++) {
                    res.add(i, Integer.parseInt(split[i]));
                }
            }
            case 1 -> { //union()
                res = new Bitset(size - amountOfNulls1 + length);
                int ind = 0;
                for (int i = 0; i < size - amountOfNulls1; i++) {
                    res.add(ind, set1[i]);
                    ind++;
                }
                for (int i = 0; i < length; i++) {
                    res.add(ind, Integer.parseInt(split[i]));
                    ind++;
                }
                insertionSort(res.set);
            }
            case 2 -> { //complement()
                amountOfNulls1 = insertionSort(set1);
                res = new Bitset(size - amountOfNulls1);
                for (int i = 0; i < size - amountOfNulls1; i++) {
                    res.add(i, set1[i]);
                }
            }
            default -> res = new Bitset(0);
        }
        return res;
    }

    private int commonElInd(Integer[] arr, Integer el, int start, int end) {
        for (int i = start; i < end; i++) {
            Integer k = arr[i];
            if (k == el) return i;
            if (k > el) return -1;
        }
        return -1;
    }

    private int insertionSort(Integer[] arr) {
        int amountOfNulls = 0;
        int length = arr.length;
        if (length != 0 && arr[0] == null) {
            if (length != 1) {
                while (amountOfNulls != length - 1 && arr[length - 1 - amountOfNulls] == null) amountOfNulls++;
                Integer t = arr[length - 1 - amountOfNulls];
                arr[length - 1 - amountOfNulls] = arr[0];
                arr[0] = t;
            }
            amountOfNulls++;
        }
        for (int i = 1; i < length - amountOfNulls; i++) {
            Integer t = arr[i];
            if (t == null) {
                if (i == length - 1 - amountOfNulls) {
                    amountOfNulls++;
                    break;
                } else if (i != length - amountOfNulls) {
                    while (arr[length - 1 - amountOfNulls] == null) {
                        amountOfNulls++;
                    }
                    t = arr[length - 1 - amountOfNulls];
                    arr[length - 1 - amountOfNulls] = arr[i];
                    arr[i] = t;
                    amountOfNulls++;
                }
            }
            int j;
            for (j = i - 1; j >= 0 && t < arr[j]; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = t;
        }
        return amountOfNulls;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IllegalArgumentException("Illegal index value");
    }

    private void nullCheck(int index) {
        checkIndex(index);
        if (set[index] == null) {
            throw new IllegalStateException("Element with this index has not been added or has been deleted");
        }
    }

    public void print() {
        if (size == 0) System.out.println("There are no elements in this set");
        for (int i = 0; i < size; i++) {
            if (set[i] != null) System.out.println("set[" + i + "] = " + set[i]);
        }
    }
}
