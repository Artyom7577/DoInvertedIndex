import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


//fegerkekrkfrjejver

/**
 * The InvertedIndex class represents a simple inverted index with basic operations.
 * It allows building an inverted index, displaying the term-document matrix,
 * and performing set operations on terms such as intersection.
 */
public class InvertedIndex {

    /**
     * Main method to demonstrate the usage of the InvertedIndex class.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {

        // Record the start time for performance measurement
        long before = System.currentTimeMillis();

        // List of document filenames
        List<String> filenames = List.of("Doc1.txt", "Doc2.txt", "Doc3.txt", "Doc4.txt", "Doc5.txt");

        // List of terms for intersection operation
        List<String> terms = Arrays.asList("brutus", "caesar", "noble", "window");

        // Build inverted index with document IDs represented as strings
        Map<String, Set<String>> invertedIndex = buildInvertedIndexWithStringDocIDs(filenames);

        // Display term-document matrix with terms
        displayTermDocumentMatrixWithTerms(invertedIndex, filenames);

        // Display the inverted index
        displayInvertedIndexWithStringDocIDs(invertedIndex);

        // Perform intersection operation on terms
        List<String> result = intersectTerms(buildInvertedIndexWithStringDocIDs(filenames), terms);

        // Display the result of the intersection operation
        System.out.println("\nResult of INTERSECT operation for terms " + terms + ": " + result);

        // Record the end time for performance measurement
        long after = System.currentTimeMillis();

        // Display the total time taken for the operations
        System.out.println("Time taken: " + (after - before) + " milliseconds");
    }

    /**
     * Builds an inverted index with document IDs represented as strings.
     *
     * @param filenames List of document filenames.
     * @return Inverted index with terms and corresponding document IDs.
     */
    private static Map<String, Set<String>> buildInvertedIndexWithStringDocIDs(List<String> filenames) {
        Map<String, Set<String>> invertedIndex = new HashMap<>();

        for (int docID = 0; docID < filenames.size(); docID++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filenames.get(docID)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] terms = line.split(" ");

                    Arrays.sort(terms);
                    for (String term : terms) {
                        term = term.toLowerCase();
                        invertedIndex.computeIfAbsent(term, k -> new HashSet<>()).add(String.valueOf(docID + 1));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return invertedIndex;
    }

    /**
     * Displays the inverted index with document IDs represented as strings.
     *
     * @param invertedIndex Inverted index to be displayed.
     */
    private static void displayInvertedIndexWithStringDocIDs(Map<String, Set<String>> invertedIndex) {
        System.out.println("Inverted Index Representation:");
        for (Map.Entry<String, Set<String>> entry : invertedIndex.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Displays the term-document matrix with terms and document IDs.
     *
     * @param invertedIndex Inverted index containing terms and document IDs.
     * @param fileNames     List of document filenames.
     */
    private static void displayTermDocumentMatrixWithTerms(Map<String, Set<String>> invertedIndex,
                                                           List<String> fileNames) {
        Set<String> uniqueTerms = invertedIndex.keySet();
        List<String> documents = invertedIndex.values().stream()
                .flatMap(Set::stream)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        int[][] matrix = new int[uniqueTerms.size()][documents.size()];

        List<String> sortedTerms = new ArrayList<>(uniqueTerms);
        sortedTerms.sort(Comparator.naturalOrder());

        printHeader(fileNames);

        for (int termIndex = 0; termIndex < uniqueTerms.size(); termIndex++) {
            String term = sortedTerms.get(termIndex);
            Set<String> docIDs = invertedIndex.getOrDefault(term, Collections.emptySet());

            printRow(term, documents, docIDs, matrix, termIndex);
        }
    }

    /**
     * Prints the header row of the term-document matrix.
     *
     * @param fileNames List of document filenames.
     */
    private static void printHeader(List<String> fileNames) {
        System.out.printf("%-20s", "");
        for (String document : fileNames) {
            System.out.printf("%-16s", document);
        }
        System.out.println("\n-----------------------------------------------------------------------------------------------------");
    }

    /**
     * Prints a row in the term-document matrix.
     *
     * @param term      Current term.
     * @param documents List of document IDs.
     * @param docIDs    Set of document IDs containing the term.
     * @param matrix    Term-document matrix.
     * @param termIndex Index of the current term in the matrix.
     */
    private static void printRow(String term, List<String> documents, Set<String> docIDs, int[][] matrix, int termIndex) {
        System.out.printf("%-20s", term);
        for (int docIndex = 0; docIndex < documents.size(); docIndex++) {
            if (docIDs.contains(documents.get(docIndex))) {
                matrix[termIndex][docIndex] = 1;
                System.out.printf("%-16s", "1");
            } else {
                System.out.printf("%-16s", "0");
            }
        }
        System.out.println();
    }

    /**
     * Performs an intersection operation on terms in the inverted index.
     *
     * @param invertedIndex Inverted index containing terms and document IDs.
     * @param terms         List of terms to intersect.
     * @return List of document IDs resulting from the intersection operation.
     */
    private static List<String> intersectTerms(Map<String, Set<String>> invertedIndex, List<String> terms) {
        terms.sort(Comparator.comparingInt(term -> invertedIndex.getOrDefault(term, Collections.emptySet()).size()));

        List<String> result = new ArrayList<>(invertedIndex.getOrDefault(terms.get(0), Collections.emptySet()));

        for (int i = 1; i < terms.size() && !result.isEmpty(); i++) {
            Set<String> currentPostings = invertedIndex.getOrDefault(terms.get(i), Collections.emptySet());
            result.retainAll(currentPostings);
        }
        return result;
    }

    /**
     * Performs a union operation on two lists of integers.
     *
     * @param list1 The first list of integers.
     * @param list2 The second list of integers.
     * @return A list containing the union of elements from both input lists.
     */
    private static List<Integer> union(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set = new HashSet<>(list1);
        set.addAll(list2);
        return new ArrayList<>(set);
    }

    /**
     * Performs an intersection operation on two lists of integers.
     *
     * @param p1 The first list of integers.
     * @param p2 The second list of integers.
     * @return A list containing the intersection of elements from both input lists.
     */
    private static List<Integer> intersect(List<Integer> p1, List<Integer> p2) {
        List<Integer> answer = new ArrayList<>();

        int index1 = 0, index2 = 0;

        while (index1 < p1.size() && index2 < p2.size()) {
            int docID1 = p1.get(index1);
            int docID2 = p2.get(index2);

            if (docID1 == docID2) {
                answer.add(docID1);
                index1++;
                index2++;

            } else if (docID1 < docID2) {
                index1++;

            } else {
                index2++;
            }
        }
        return answer;
    }

    /**
     * Computes the complement of a list of integers with respect to totalDocuments.
     *
     * @param operand        The list of integers to complement.
     * @param totalDocuments The total number of documents.
     * @return A list containing the complement of the input list with respect to totalDocuments.
     */
    private static List<Integer> complement(List<Integer> operand, int totalDocuments) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= totalDocuments; i++) {
            if (!operand.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
}
