# Inverted Index Java Program

This Java program implements an inverted index for a set of documents, allowing for efficient information retrieval. The program performs various operations such as building the inverted index, displaying the index, generating a term-document matrix, and intersecting specified terms.

## Usage

1. **Compile the Program:**

   ```bash
   javac InvertedIndex.java
   ```

2. **Run the Program:**

   ```bash
   java InvertedIndex
   ```

   Ensure that the `Doc1.txt`, `Doc2.txt`, `Doc3.txt`, `Doc4.txt`, and `Doc5.txt` files are present in the same directory.

## Description

### Inverted Index Construction

- The program reads multiple documents specified in the `filenames` list.
- It constructs an inverted index using string-based document IDs.
- The inverted index is a mapping of terms to the set of documents in which they appear.

### Displaying Inverted Index and Term-Document Matrix

- The program displays the constructed inverted index, providing insights into the occurrences of terms across documents.
- Additionally, it generates and displays a term-document matrix, illustrating the presence (1) or absence (0) of terms in each document.

### Intersecting Terms

- The program allows the user to perform an intersection operation on specified terms.
- The result shows the common set of documents where all specified terms coexist.

## Files

- **InvertedIndex.java:** Contains the main Java program.

## Results

After running the program, you will observe:

- Display of the inverted index.
- Display of the term-document matrix with terms.
- Result of the intersection operation for specified terms.

## Time Complexity

The program measures the time taken to perform operations and prints it at the end, providing insights into the efficiency of the implemented algorithms.

## Dependencies

- Java (JDK)

## License

This project is licensed under the [MIT License](LICENSE).
```

Feel free to adjust and modify the content as needed for your specific preferences or to provide more details about your code.
