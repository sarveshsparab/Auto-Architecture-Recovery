package com.sarveshparab.analysers.semantic.linguistic;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

/**
 * @category Class
 * @purpose For stripping the sentence and creating tokens
 * @author Sarvesh
 */
public class Tokenizer {
    public TokenizerFactory<CoreLabel> tf;
    /**
     * @category Constructor
     * @author Sarvesh
     */
    public Tokenizer(){

    }
    /**
     * @category Function(Private)
     * @argument sentence : Sentence String
     * @return A list of tokenWords
     * @author Sarvesh
     */
    private List<CoreLabel> makeTokens (String sentence){
        if(tf == null){
            tf = PTBTokenizer.factory(new CoreLabelTokenFactory(),"untokenizable=noneDelete");
        }
        List<CoreLabel> tokenWords = tf.getTokenizer(new StringReader(sentence)).tokenize();
        return tokenWords;
    }
    /**
     * @category Function
     * @argument sentence : Sentence String
     * @return A list of tokens
     * @author Sarvesh
     */
    public List<String> tokenize (String sentence){
        List<String> tokens = new LinkedList<String>();
        List<CoreLabel> tokenWords = makeTokens(sentence);
        Iterator<CoreLabel> itr = tokenWords.iterator();
        while(itr.hasNext()) {
            CoreLabel element = itr.next();
            tokens.add(element.word());
        }
        return tokens;
    }
}
