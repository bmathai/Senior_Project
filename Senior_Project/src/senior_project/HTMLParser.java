/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Blaise Mathai
 */
public class HTMLParser {
    
    private Document doc;
    
    public HTMLParser(String f){
            doc = Jsoup.parse(f);
            System.out.println(doc.toString());
    }
    
    public Document getHTMLDoc(){
        return doc;
    }
    
    public String cleanHTML(){
        return doc.normalise().toString();
    }
    
}
