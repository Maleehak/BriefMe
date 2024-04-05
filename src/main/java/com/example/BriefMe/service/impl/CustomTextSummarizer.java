package com.example.BriefMe.service.impl;

import com.example.BriefMe.service.client.TextSummarizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public String summarizeText(String text){
        /* 1. Filter stop words from the text
         * 2. Get frequency of remaining words
         * 3. Sort words in order of decreasing frequency
         */
        if(text.equals("") || text.equals(" ") || text.equals("\n"))
        {
            String msg = "Nothing to summarize...";
            return msg;
        }
        List<String> filteredListOfWords = filterStopWords(text);
        Map<String, Integer> wordsWithFrequency = getWordsWithFrequency(filteredListOfWords);
        List<String> sortedWords = sortWordsInOrderOfDecreasingFrequency(wordsWithFrequency);

        sortedWords.forEach(System.out::println);

        return null;
    }

    private List<String> filterStopWords(String text)
    {
        /* Convert given text into list of words
         * And filter out all the stop words from that list
         */
        List<String> words = Arrays.asList(text.split("\\s+"));

        return words.stream()
                .filter(word -> !STOP_WORDS.contains(word.toLowerCase()))
                .toList();
    }

    private Map<String, Integer> getWordsWithFrequency(List<String> words)
    {
        /* Split paragraph to list of individual words
         * If a word appears multiple times in the map, increase it's count
         * Else add the new word to the Map
         */
        return words.stream()
                .collect(Collectors.toMap(
                        word -> word,  // Extract word as key
                        word -> 1,      // Initial value for each word
                        Integer::sum));
    }

    private List<String> sortWordsInOrderOfDecreasingFrequency(Map<String, Integer> wordFrequency)
    {
        /* Sort map based on frequency in decreasing order and then return only words(without frequency)
         */
        return wordFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .flatMap(entry -> Stream.of(entry.getKey()))
                .toList();
    }


}
