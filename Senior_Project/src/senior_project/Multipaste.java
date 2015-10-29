/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import java.awt.MouseInfo;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author Blaise Mathai
 */
public class Multipaste {

    public String multiPaste(String[] clipboard){
        String[] shortClipboard = new String [3];
        for(int i = 0; i < clipboard.length; i++){
            if(clipboard[i].length() > 13){
                shortClipboard[i] = " " + clipboard[i].substring(0,10) + "...";//" " is a hack to get around joptionpane <html>
            } else {
                shortClipboard[i] = " " + clipboard[i];
                System.out.println(shortClipboard[i]);
            }
        }
        Object[] possibleValues = { shortClipboard[0], shortClipboard[1], shortClipboard[2] };
        JFrame test = new JFrame();
        test.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        test.setUndecorated(true);
        test.setVisible(true);
        Object selectedValue = JOptionPane.showInputDialog(test, null, "Paste", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
        test.setVisible(false);
        test.dispose();
        //convert back to full form
        
        if(selectedValue!=null){
            for(int i = 0; i < clipboard.length; i++){
                if(selectedValue.equals(shortClipboard[i])){
                    selectedValue = clipboard[i];
                }
            }
            return selectedValue.toString();
        } else{
            return null;
        }
        
    }
}
