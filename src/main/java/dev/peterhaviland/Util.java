package dev.peterhaviland;

import java.util.List;

import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;

import dev.peterhaviland.beans.Post;

public class Util {

    public static void ConvertBBCodeToHTML(List<Post> posts) {
        TextProcessor textProcessor = BBProcessorFactory.getInstance().create();
        for (Post post : posts) 
            post.setBody(textProcessor.process(post.getBody()));
    }
    
}
