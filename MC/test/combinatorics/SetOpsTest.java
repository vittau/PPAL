package combinatorics;

import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by Vitor on 18/08/2015.
 */
public class SetOpsTest {

    @Test
    public void powerSetTest() {
        Set<String> set = new HashSet<String>(Arrays.asList(new String[]{"A", "B", "C"}));

        Set<Set<String>> expectedSet = new HashSet<Set<String>>();
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"B"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"C"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A", "B"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A", "C"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"B", "C"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A", "B", "C"})));

        assertEquals(SetOps.powerSet(set), expectedSet);
    }

    @Test
    public void permutationsTest() {
        Set<String> setA = new HashSet<String>(Arrays.asList(new String[]{"A"}));
        Set<String> setB = new HashSet<String>(Arrays.asList(new String[]{"B"}));
        Set<Set<String>> setAB = new HashSet<Set<String>>();
        setAB.add(setA);
        setAB.add(setB);
        Set<String> set1 = new HashSet<String>(Arrays.asList(new String[]{"1"}));
        Set<String> set2 = new HashSet<String>(Arrays.asList(new String[]{"2"}));
        Set<Set<String>> set12 = new HashSet<Set<String>>();
        set12.add(set1);
        set12.add(set2);
        List<Set<Set<String>>> list = new ArrayList<Set<Set<String>>>();
        list.add(setAB);
        list.add(set12);

        Set<Set<String>> expectedSet = new HashSet<Set<String>>();
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A", "1"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"A", "2"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"B", "1"})));
        expectedSet.add(new HashSet<String>(Arrays.asList(new String[]{"B", "2"})));

        assertEquals(SetOps.permutations(list), expectedSet);
    }
}
