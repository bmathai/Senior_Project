/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Blaise Mathai
 */
public class SyntaxHighlight {
    StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet redText = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.red);
            AttributeSet greenText = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.green.darker());
            AttributeSet blueText = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.blue);
            AttributeSet blackText = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
            AttributeSet greyText = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.gray);
            
    /**
     *
     * @param area
     * @param doc
     * @return
     */
    public StyledDocument highlightSyntax(JTextPane area, StyledDocument doc){
        String text = area.getText();
        int start;
        int end;
        
        text = text.replaceAll("\n","");
        start = text.indexOf("\"");
        end = text.indexOf("\"", start+1);
        //color "strings" second to last because they override most things
        doc.setCharacterAttributes(0, text.length(), redText, true);
        while(start!= -1){
            System.out.println(start);
            if(end!= -1){
                doc.setCharacterAttributes(start, end+1, greenText, true);
                doc.setCharacterAttributes(end+1, end+1, blackText, false);
                } else{
                    doc.setCharacterAttributes(start, start+1, greenText, true);
                    doc.setCharacterAttributes(start+1, start+1, blackText, false);
                }

                if(end!= -1){
                    start = text.indexOf("\"", end+1);
                    end = text.indexOf("\"", start+1);
                } else{
                    start = text.indexOf("\"", start+1);
                }
            }
        
        //comments coloring last because they override everything
        text = area.getText();
        //color comments
        String[] lines = text.split(System.getProperty("line.separator"));
        for(int i = 1; i < lines.length; i++){
            start = lines[i].indexOf("//");
            if(start!=-1){
                int lengthAll = 0;
                for(int j = 0; j < i; j++){
                    lengthAll = lengthAll + lines[j].length() + 1;
                }
                doc.setCharacterAttributes((start + lengthAll), lines[i].length() + lengthAll, greyText, true);
                doc.setCharacterAttributes(lines[i].length() + lengthAll, lines[i].length() + lengthAll, blackText, false);
            }
        }
        
        return doc;
    }
}
