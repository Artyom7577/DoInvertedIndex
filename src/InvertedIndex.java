import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndex {
    public static void main(String[] args){

        long before = System.currentTimeMillis();
        List<String> filenames = List.of("Doc1.txt", "Doc2.txt", "Doc3.txt", "Doc4.txt", "Doc5.txt");

        List<String> terms = Arrays.asList("brutus", "caesar", "noble", "window");
        Map<String, Set<String>> invertedIndex = buildInvertedIndexWithStringDocIDs(filenames);
        displayTermDocumentMatrixWithTerms(invertedIndex, filenames);

        displayInvertedIndexWithStringDocIDs(invertedIndex);

        List<String> result = intersectTerms(buildInvertedIndexWithStringDocIDs(filenames), terms);

        System.out.println();

        System.out.println("Result of INTERSECT operation for terms " + terms + ": " + result);

        long after = System.currentTimeMillis();

        System.out.println("Time taken: " + (after - before) + " milliseconds");
    }

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

    private static void displayInvertedIndexWithStringDocIDs(Map<String, Set<String>> invertedIndex) {
        System.out.println("Inverted Index Representation:");
        for (Map.Entry<String, Set<String>> entry : invertedIndex.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void displayTermDocumentMatrixWithTerms(Map<String, Set<String>> invertedIndex, List<String> fileNames) {
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

    private static void printHeader(List<String> fileNames) {
        System.out.printf("%-20s", "");
        for (String document : fileNames) {
            System.out.printf("%-16s", document);
        }
        System.out.println("\n-----------------------------------------------------------------------------------------------------");
    }

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


    private static List<String> intersectTerms(Map<String, Set<String>> invertedIndex, List<String> terms) {
        terms.sort(Comparator.comparingInt(term -> invertedIndex.getOrDefault(term, Collections.emptySet()).size()));

        List<String> result = new ArrayList<>(invertedIndex.getOrDefault(terms.get(0), Collections.emptySet()));

        for (int i = 1; i < terms.size() && !result.isEmpty(); i++) {
            Set<String> currentPostings = invertedIndex.getOrDefault(terms.get(i), Collections.emptySet());
            result.retainAll(currentPostings);
        }
        return result;
    }

    private static List<Integer> union(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set = new HashSet<>(list1);
        set.addAll(list2);
        return new ArrayList<>(set);
    }

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

