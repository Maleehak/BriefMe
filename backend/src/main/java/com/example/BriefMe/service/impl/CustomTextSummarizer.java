package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.TextSummarizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomTextSummarizer implements TextSummarizer {

    private static final int MAX_ITERATIONS = 50;
    private static final double DAMPING_FACTOR = 0.85; // Damping factor for TextRank

    public String generateSummary(String text, int numberOfLines) {

        log.info("Performing text summarization.......");

        if (text.equals("") || text.equals(" ") || text.equals("\n")) {
            String msg = "Nothing to summarize...";
            return msg;
        }

        // Split the original text into sentences
        List<String> sentences = splitTextIntoSentences(text);

        // Build the similarity matrix
        double[][] similarityMatrix = buildSimilarityMatrix(sentences);

        // Perform text ranking
        double[] scores = calculateTextRank(similarityMatrix);

        // Extract top sentences for summary
        List<String> summarySentences = extractTopSentences(scores, sentences, numberOfLines);

        String summary = convertToBulletPoints(summarySentences);

        return summary;
    }

    // Split text into sentences
    private static List<String> splitTextIntoSentences(String text) {
        return Arrays.asList(text.split("[.!?]\\s*"));
    }

    private double[][] buildSimilarityMatrix(List<String> sentences) {
        log.info("Calculating Similarity.......");
        int n = sentences.size();
        double[][] similarityMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    similarityMatrix[i][j] = calculateSimilarity(sentences.get(i), sentences.get(j));
                }
            }
        }

        return similarityMatrix;
    }

    private double calculateSimilarity(String sentence1, String sentence2) {
        // TODO: Optimize it: Create map of (sentence, words)
        List<String> wordsInSentence1 = filterStopWords(sentence1);
        List<String> wordsInSentence2 = filterStopWords(sentence2);

        // TODO: Optimize it: Create Hashmap of (sentence, (words, frequency))
        Map<String, Integer> wordsInSentence1WithFrequency = calculateWordsFrequency(wordsInSentence1);
        Map<String, Integer> wordsInSentence2WithFrequency = calculateWordsFrequency(wordsInSentence2);

        // covert it to map of (CharSequence, integer) as required for calculating cosine similarity
        Map<CharSequence, Integer> wordsInCharSequence1WithFrequency = convertToCharSequenceMap(wordsInSentence1WithFrequency);
        Map<CharSequence, Integer> wordsInCharSequence2WithFrequency = convertToCharSequenceMap(wordsInSentence2WithFrequency);

        // Calculate cosine similarity
        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        return cosineSimilarity.cosineSimilarity(wordsInCharSequence1WithFrequency, wordsInCharSequence2WithFrequency);
    }

    private Map<CharSequence, Integer> convertToCharSequenceMap(Map<String, Integer> inputMap) {
        return new HashMap<>(inputMap);
    }

    private List<String> filterStopWords(String text)
    {
        // Convert given text into list of words and filter out all the stop words from that list
        return Arrays.asList(text.split("\\s+"));
    }

    private Map<String, Integer> calculateWordsFrequency(List<String> words)
    {
        // Split paragraph to list of individual words
        // Split a word appears multiple times in the map, increase it's count else add the new word to the Map
        return words.stream()
                .collect(Collectors.toMap(
                        word -> word,  // Extract word as key
                        word -> 1,      // Initial value for each word
                        Integer::sum));
    }

    private static double[] calculateTextRank(double[][] similarityMatrix) {
        int n = similarityMatrix.length;
        double[] scores = new double[n];
        double[] prevScores = new double[n];

        // Initialize scores with equal values
        for (int i = 0; i < n; i++) {
            scores[i] = 1.0 / n;
        }

        // Iterate TextRank algorithm
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            System.arraycopy(scores, 0, prevScores, 0, n);

            for (int i = 0; i < n; i++) {
                double score = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        score += similarityMatrix[j][i] * prevScores[j];
                    }
                }
                scores[i] = (1 - DAMPING_FACTOR) + DAMPING_FACTOR * score;
            }
        }
        return scores;
    }

    private static List<String> extractTopSentences(double[] scores, List<String> sentences, int numberOfSentences) {
        List<String> topSentences = new ArrayList<>();

        for (int i = 0; i < numberOfSentences; i++) {
            int topIndex = findMaxIndex(scores);
            topSentences.add(sentences.get(topIndex));
            scores[topIndex] = Double.MIN_VALUE; // Mark as visited
        }
        return topSentences;
    }

    private static int findMaxIndex(double[] arr) {

        return IntStream.range(0, arr.length)
                .reduce((i, j) -> arr[i] > arr[j] ? i : j)
                .orElse(-1);
    }

    public static String convertToBulletPoints(List<String> sentences) {
        log.info("Creating bullet points.......");
        // Using Java streams to map each sentence with a bullet point
        return IntStream.range(0, sentences.size())
                .mapToObj(i -> (i + 1) + ". " + sentences.get(i))
                .collect(Collectors.joining("\n"));
    }

}
