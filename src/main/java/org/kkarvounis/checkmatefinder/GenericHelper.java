package org.kkarvounis.checkmatefinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenericHelper {
    public static boolean inRange(int number, int start, int end) {
        return inRange(number, start, end, true);
    }

    public static boolean inRange(int number, int start, int end, boolean inclusive) {
        if (inclusive) {
            return number >= start && number <= end;
        }
        return number > start && number < end;
    }

    public static <T> ArrayList<T> merge(ArrayList<T> list1, ArrayList<T> list2) {
        ArrayList<T> mergedList = new ArrayList<>(list1);
        mergedList.addAll(list2);

        return mergedList;
    }

    public static <T> void addUnique(ArrayList<T> list, T item) {
        if (!list.contains(item)) {
            list.add(item);
        }
    }

    public static <T> boolean equalContents(ArrayList<T> list1, ArrayList<T> list2) {
        return list1.size() == list2.size() && list1.containsAll(list2);
    }

    public static <K, V> HashMap<V, K> reverse(HashMap<K, V> map) {
        HashMap<V, K> reversedMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }

        return reversedMap;
    }
}
