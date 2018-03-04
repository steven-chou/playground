package playground;

import org.junit.jupiter.api.Test;

import java.util.*;

public class QuestionTestsOne {


    /**
     * PowerSet - all the subsets of a given set.
     * Ex: {"A", "B", "C"}, the power set are : {{"A"}, {"A", "B"}, {"B"}, {"A", "C"}, {"A", "B", "C"}, {"B", "C"}, {"C"}, {}}
     * https://stackoverflow.com/questions/24365954/how-to-generate-a-power-set-of-a-given-set
     */
    @Test
    void powerSet() {
        powerSet(List.of("A", "B", "C"));
    }

    static <T> List<List<T>> powerSet(List<T> input) {
        List<List<T>> results = new ArrayList<>();
        for (T element : input) {

            ListIterator<List<T>> listIterator = results.listIterator();
            while (listIterator.hasNext()) {
                // Create a new List with copied List and add a new item
                List<T> newSet = new ArrayList<>(listIterator.next());
                newSet.add(element);
                listIterator.add(newSet);
            }

//            for (ListIterator<List<T>> setsIterator = results.listIterator(); setsIterator.hasNext(); ) {
//                List<T> newSet = new ArrayList<>(setsIterator.next());
//                newSet.add(element);
//                setsIterator.add(newSet);
//            }
            // Add the single item set {"A"}, {"B"}, {"C"}
            results.add(new ArrayList<>(List.of(element)));
        }
        results.add(new ArrayList<>());
        return results;
    }

}
