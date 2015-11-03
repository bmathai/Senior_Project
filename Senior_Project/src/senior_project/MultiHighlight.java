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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class MultiHighlight {

    private JTextComponent comp;

    private String charsToHighlight;
    private DefaultHighlighter.DefaultHighlightPainter highlightPainter
            = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);

    public MultiHighlight(JTextComponent c, String chars) {
        comp = c;
        charsToHighlight = chars;
    }

    public void highlight() {
        // highlight all characters that appear in charsToHighlight
        Highlighter h = comp.getHighlighter();
        String text = comp.getText();
        
        h.removeAllHighlights();

        int offset = text.indexOf(charsToHighlight);
        int length = charsToHighlight.length();
        System.out.println("offset: " + offset);

        while (offset != -1) {
            try {
                System.out.println("help!");
                h.addHighlight(offset, offset + length, highlightPainter);
                offset = text.indexOf(charsToHighlight, offset + 1);
            } catch (BadLocationException ble) {
                System.out.println(ble);
            }
        }
    }
}
