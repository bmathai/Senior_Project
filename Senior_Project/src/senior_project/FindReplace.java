/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import javax.swing.JTextArea;

/**
 *
 * @author Blaise Mathai
 */
public class FindReplace {
    private JTextArea area;
    private String str;
    private String replacement;
    
    /**
     *
     * @param textArea
     * @param chars
     * @param replace
     */
    public FindReplace(JTextArea textArea, String chars, String replace){
        str = chars;
        area = textArea;
        replacement = replace;
    }
    
    /**
     *
     * @return
     */
    public JTextArea replaceString(){
        if(str.trim().length() > 0){
            area.setText(area.getText().replace(str,replacement)); 
        }
        return area;
    }
}
