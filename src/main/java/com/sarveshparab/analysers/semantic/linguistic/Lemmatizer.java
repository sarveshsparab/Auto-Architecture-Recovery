package com.sarveshparab.analysers.semantic.linguistic;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {
    protected StanfordCoreNLP pipeline;
    /**
     * @category Constructor
     * @argument verbose : A integer flag for verbose printing
     * @purpose Initializes properties for every token
     * 			Initializes the Satnford NLP pipeline to process the properties
     * @author Sarvesh
     */
    public Lemmatizer() {
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }
    /**
     * @category Function
     * @argument documentText : A string sentence which needs to be broken into lemmas
     * @return A linked list of strings of lemmas of each word in the input
     * @author Sarvesh
     */
    public List<String> lemmatize(String documentText){
        List<String> lemmas = new LinkedList<String>();
        Annotation document = new Annotation(documentText);
        this.pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }
}