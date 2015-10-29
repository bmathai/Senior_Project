/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Blaise Mathai
 */
public class CSSFormat {
    
    CSSParser cssParser = null;
    String finalOut = "";
    
    public CSSFormat(CSSParser cssP){
        cssParser = cssP;
    }
    /**
     * 
     * @return 
     */
    
    public String formatCSS(){
        for (int i = 0; i < cssParser.getCSSElements().size(); i++) {//for CSSElements List
                finalOut += cssParser.getCSSElements().get(i).getName().trim() + "\n";
                for (int j = 0; j < cssParser.getCSSElements().get(i).getProperties().size(); j++) {//for CSSProperties List
                    finalOut += "\t" + cssParser.getCSSElements().get(i).getProperties().get(j).getName().trim();
                    finalOut += " " + cssParser.getCSSElements().get(i).getProperties().get(j).getValue().trim() + "\n";
                }
                finalOut += "}\n\n";
            }
        return finalOut;
    }
    
    public void removeDuplicates(){
        for(int i = 0; i < cssParser.getCSSElements().size(); i++){
            for(int j = 0; j < cssParser.getCSSElements().get(i).getProperties().size(); j++){
                if(cssParser.getCSSElements().get(i).hasMoreThanOneProperty(cssParser.getCSSElements().get(i).getProperties().get(j).getName())){
                        cssParser.getCSSElements().get(i).removeFirstProperty(cssParser.getCSSElements().get(i).getProperties().get(j).getName());
                        j--;
                }//for
            }//if
        }//for
    }
    
}
