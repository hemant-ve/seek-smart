package com.nutanix.hack.seeksmart.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SentimentAnalyzer {

    public int findSentiment(String line) {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, index");
        StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP(properties);
        int sentimentIndex = 0;
        if (line != null && !line.isEmpty()) {
            int longest = 0;
            Annotation annotation = stanfordCoreNLP.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    sentimentIndex = sentiment;
                    longest = partText.length();
                }
            }
        }
        if (sentimentIndex == 2 || sentimentIndex > 4 || sentimentIndex < 0) {
            return 2;
        }
        return sentimentIndex;
    }

    private String toSentiment(int sentiment) {
        switch (sentiment) {
            case 0:
                return "Very Negative";
            case 1:
                return "Negative";
            case 2:
                return "Neutral";
            case 3:
                return "Positive";
            case 4:
                return "Very Positive";
            default:
                return "";
        }
    }
}
