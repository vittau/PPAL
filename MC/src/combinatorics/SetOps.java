package combinatorics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Combinatorics for Sets.
 */
public class SetOps {

    /**
     * Generate the power set of a given set.
     * @param originalSet Original Set.
     * @param <T> Class of the elements in the Set.
     * @return Power set of the given Set.
     */
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    /**
     * Permutations of a list of sets containing sets of objects.
     * @param lists Original list of sets of sets.
     * @param <T> Object type.
     * @return Resultant set of sets with all permutations of original list.
     */
    public static <T> Set<Set<T>> permutations(List<Set<Set<T>>> lists) {
        if (lists == null || lists.isEmpty()) {
            HashSet<Set<T>> returnSet = new HashSet<Set<T>>();
            returnSet.add(new HashSet<T>());
            return returnSet;
        } else {
            Set<Set<T>> res = new HashSet<Set<T>>();
            permutationsImpl(lists, res, 0, new HashSet<T>());
            return res;
        }
    }

    /** Recursive implementation for {@link #permutations(List)}. */
    private static <T> void permutationsImpl(List<Set<Set<T>>> ori, Set<Set<T>> res, int d, Set<T> current) {
        // if depth equals number of original collections, final reached, add and return
        if (d == ori.size()) {
            res.add(current);
            return;
        }

        //Iterate from current collection and copy 'current' element N times, one for each element.
        Set<Set<T>> currentSet = ori.get(d);
        for (Set<T> element : currentSet) {
            Set<T> copy = new HashSet<T>(current);
            copy.addAll(element);
            permutationsImpl(ori, res, d + 1, copy);
        }
    }
}
