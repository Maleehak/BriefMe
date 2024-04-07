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

@Slf4j
public class CustomTextSummarizer implements TextSummarizer {

    public static final List<String> STOP_WORDS = Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
            "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being",
            "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't",
            "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during",
            "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have",
            "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers",
            "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've",
            "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
            "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on",
            "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over",
            "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't",
            "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
            "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll",
            "they're", "they've", "this", "those", "through", "to", "too", "under", "until",
            "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were",
            "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while",
            "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you",
            "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"
    );

    public String generateSummary(String text, int numberOfLines) {

        log.info("Performing text summarization.......");

        int maxSummarySize = 4;

        if (text.equals("") || text.equals(" ") || text.equals("\n")) {
            String msg = "Nothing to summarize...";
            return msg;
        }

        // Split the original text into sentences
        List<String> sentences = splitTextIntoSentences(text);

        // Build the similarity matrix
        double[][] similarityMatrix = buildSimilarityMatrix(sentences);

        // Extract top sentences for summary
        List<String> summarySentences = extractTopSentences(similarityMatrix,sentences, 3);

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
        Map<CharSequence, Integer> outputMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
            outputMap.put(entry.getKey(), entry.getValue().intValue());
        }
        return outputMap;
    }

    private List<String> filterStopWords(String text)
    {
        // Convert given text into list of words and filter out all the stop words from that list
        List<String> words = Arrays.asList(text.split("\\s+"));
        return words.stream()
                .filter(word -> !STOP_WORDS.contains(word.toLowerCase()))
                .toList();
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


    public static List<String> extractTopSentences(double[][] similarityMatrix, List<String> sentences, int numSentences) {
        log.info("Extracting Top Sentences.......");
        List<String> topSentences = new ArrayList<>();

        // Calculate sentence scores based on the sum of similarity scores
        double[] sentenceScores = new double[similarityMatrix.length];
        for (int i = 0; i < similarityMatrix.length; i++) {
            double score = 0.0;
            for (int j = 0; j < similarityMatrix[i].length; j++) {
                score += similarityMatrix[i][j];
            }
            sentenceScores[i] = score;
        }

        // Rank sentences based on scores
        List<Integer> rankedIndices = rankSentences(sentenceScores);

        // Extract top sentences
        for (int i = 0; i < Math.min(numSentences, rankedIndices.size()); i++) {
            int index = rankedIndices.get(i);
            topSentences.add(sentences.get(index));
        }

        return topSentences;
    }

    public static List<Integer> rankSentences(double[] scores) {
        log.info("Ranking sentences.......");
        List<Integer> rankedIndices = new ArrayList<>();
        // Simple ranking by sorting indices based on scores
        for (int i = 0; i < scores.length; i++) {
            rankedIndices.add(i);
        }
        rankedIndices.sort((i1, i2) -> Double.compare(scores[i2], scores[i1])); // Sort in descending order
        return rankedIndices;
    }

    public static String convertToBulletPoints(List<String> sentences) {
        log.info("Creating bullet points.......");
        // Using Java streams to map each sentence with a bullet point
        return IntStream.range(0, sentences.size())
                .mapToObj(i -> (i + 1) + ". " + sentences.get(i))
                .collect(Collectors.joining("\n"));
    }


}
