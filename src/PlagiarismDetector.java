import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class PlagiarismDetector {

    // n-gram size (5 or 7 recommended)
    private static final int N = 5;

    // Hash base and mod for rolling hash
    private static final long BASE = 31;
    private static final long MOD = 1_000_000_007;

    // n-gram hash -> set of document IDs
    private Map<Long, Set<String>> ngramIndex = new HashMap<>();

    // document ID -> total number of n-grams
    private Map<String, Integer> documentGramCount = new HashMap<>();


    /* ============================
       Document Indexing
       ============================ */

    public void indexDocument(String docId, String text) {
        text = normalize(text);
        List<String> grams = generateNGrams(text, N);

        documentGramCount.put(docId, grams.size());

        for (String gram : grams) {
            long hash = computeHash(gram);

            ngramIndex
                    .computeIfAbsent(hash, k -> new HashSet<>())
                    .add(docId);
        }

        System.out.println("Indexed: " + docId +
                " (" + grams.size() + " n-grams)");
    }


    /* ============================
       Analyze New Document
       ============================ */

    public void analyzeDocument(String docId, String text) {
        text = normalize(text);
        List<String> grams = generateNGrams(text, N);

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            long hash = computeHash(gram);

            if (ngramIndex.containsKey(hash)) {
                for (String matchedDoc : ngramIndex.get(hash)) {
                    matchCount.put(
                            matchedDoc,
                            matchCount.getOrDefault(matchedDoc, 0) + 1
                    );
                }
            }
        }

        System.out.println("\nAnalyzing: " + docId);
        System.out.println("Extracted " + grams.size() + " n-grams");

        // Find most similar document
        String bestMatch = null;
        double highestSimilarity = 0;

        for (String matchedDoc : matchCount.keySet()) {
            int matches = matchCount.get(matchedDoc);
            double similarity =
                    (matches * 100.0) / grams.size();

            System.out.printf("Matched with %s → %.2f%% similarity\n",
                    matchedDoc, similarity);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = matchedDoc;
            }
        }

        if (bestMatch != null) {
            if (highestSimilarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED with " + bestMatch);
            } else if (highestSimilarity > 15) {
                System.out.println("⚠ Suspicious similarity with " + bestMatch);
            } else {
                System.out.println("No significant plagiarism detected.");
            }
        }
    }


    /* ============================
       Text Processing
       ============================ */

    private String normalize(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private List<String> generateNGrams(String text, int n) {
        String[] words = text.split(" ");
        List<String> grams = new ArrayList<>();

        if (words.length < n) return grams;

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }

        return grams;
    }

    private long computeHash(String gram) {
        long hash = 0;

        for (char c : gram.toCharArray()) {
            hash = (hash * BASE + c) % MOD;
        }

        return hash;
    }


    /* ============================
       Utility: Load File Content
       ============================ */

    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }


    /* ============================
       Demo Main Method
       ============================ */

    public static void main(String[] args) throws IOException {

        PlagiarismDetector detector = new PlagiarismDetector();

        // Example documents
        String essay1 = "Machine learning improves system performance significantly " +
                "by analyzing patterns in large datasets.";

        String essay2 = "Machine learning improves system performance significantly " +
                "through pattern recognition in large datasets.";

        String essay3 = "Cooking recipes require ingredients and careful preparation steps.";

        // Index previous essays
        detector.indexDocument("essay_001", essay1);
        detector.indexDocument("essay_002", essay2);
        detector.indexDocument("essay_003", essay3);

        // Analyze new submission
        String newEssay = "Machine learning improves system performance significantly " +
                "by analyzing patterns in large datasets.";

        detector.analyzeDocument("essay_new", newEssay);
    }
}