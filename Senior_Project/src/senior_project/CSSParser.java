/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.text.Document;

/**
 *
 * @author Blaise Mathai
 */
public class CSSParser {
    String originalContent;
    private List<CSSElement> cssElements = new ArrayList<CSSElement>(3);
    
    public CSSParser(String f){
        String aLine = "";
        
        try {
            InputStream is = new ByteArrayInputStream(f.getBytes());
            //fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            int elementNumber = -1;
            boolean skipCheck = false;//if we alread have element, skip other checks
            aLine = br.readLine();//read first line in
            do{           
                //if line contains {, then it's the start of a new element
                int lineLength = aLine.length();
                for (int i = 0; i < lineLength; i++) {
                    if(i < lineLength-1){
                        if(aLine.charAt(i) == '/' && aLine.charAt(i+1) == '*'){//then it's a comment
                            break;
                        }
                    }
                    if(aLine.charAt(i) == '%'){//then it's a property, not an element
                        break;
                    }
                    if ((aLine.charAt(i) == '{')) {
                        this.cssElements.add(new CSSElement(aLine));
                        elementNumber++;
                        skipCheck = true;
                        break;
                    }
                }
               
                //if it contains : or is a comment
                if(!skipCheck){
                for (int i = 0; i < lineLength; i++) {
                    if(i < lineLength-1){
                        if (aLine.charAt(i) == '/' && aLine.charAt(i+1) == '*'){
                            this.cssElements.get(elementNumber).addProperty(new CSSProperty(aLine, ""));//just add comment as name, no value
                        }
                    }
                    if ((aLine.charAt(i) == ':')) {
                        String name = ""; 
                        String value = "";
                        for(int j = i; j >= 0; j--){//get stuff in form: ".boxed:" for name. Need to include colon for CBC check
                            name = aLine.charAt(j) + name;
                        }
                        for(int j = i+1; j < lineLength; j++){//get stuff in form: " 5s;" for value.
                            value = value + aLine.charAt(j);
                        }
                        System.out.println(name + value);
                        this.cssElements.get(elementNumber).addProperty(new CSSProperty(name, value));
                        break;
                    }
                }
                }
                
                //if it contains }
                for (int i = 0; i < lineLength; i++) {
                    if(aLine.charAt(i) == '%'){//then it's a property, not an element
                        break;
                    }
                    if ((aLine.charAt(i) == '}')) {
                        //elementNumber++;
                        break;
                    }
                }
                skipCheck = false;//reset skipCheck
                aLine = br.readLine();//read new line every time
            }while(aLine != null);
            
            //System.out.println(aLine);
            
            
            //build list of CSSElements
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSSParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSSParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<CSSElement> getCSSElements(){
        return this.cssElements;
    }
    
}
