/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

/**
 *
 * @author Blaise Mathai
 */
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *  This class highlights a given string everywhere in the given JTextArea.
 */
public class MultiHighlight {

    private JTextArea area;

    private String chars;
    private DefaultHighlighter.DefaultHighlightPainter highlightPainter
            = new DefaultHighlighter.DefaultHighlightPainter(Color.getHSBColor((float).72, (float).15, (float)1));

    /**
     *
     * @param a
     * @param chars
     */
    public MultiHighlight(JTextArea a, String chars) {
        this.chars = chars;
        this.area = a;
    }

    /**
     *
     */
    public void highlight() {
        // highlight all characters that appear in charsToHighlight
        Highlighter h = area.getHighlighter();
        h.removeAllHighlights();
        int pos = area.getText().indexOf(chars, 0);
        if(chars.trim().length() < 1){
           try {
                h.addHighlight(pos ,
                        pos  + chars.length(),
                        highlightPainter);
            } catch (BadLocationException ex) {
                Logger.getLogger(MultiHighlight.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        pos = area.getText().indexOf(chars, 0);
        while((pos!=-1) &&  (chars.trim().length() > 0)){
            try {
                h.addHighlight(pos ,
                        pos  + chars.length(),
                        highlightPainter);
            } catch (BadLocationException ex) {
                Logger.getLogger(MultiHighlight.class.getName()).log(Level.SEVERE, null, ex);
            }
            pos = area.getText().indexOf(chars, pos+1);
        }
    }
}

