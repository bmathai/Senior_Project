/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Blaise Mathai
 */
public class CBC {

    //txt stuff
    BufferedReader brCSS;
    String lineCSS = "";

    //Our main stuff
    public String cbc(CSSParser cssP, String currentP) {

        //final JTextArea textA = textArea;
        JOptionPane.showMessageDialog(null, "Your file will now be made compatible with popular browsers");
        //edit it
        try {
            brCSS = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("CSS_CBC.txt")));
            String cssLine = brCSS.readLine();//read in first line
            do {
                String[] cssSplit = cssLine.split(" ");

                String name = "";//this is the name of a property in file
                String value = "";//this is the value of a property in file

                //Goes through the entire array of lines and fixes them
                for (int i = 0; i < cssP.getCSSElements().size(); i++) {//for CSSElements List
                    for (int j = 0; j < cssP.getCSSElements().get(i).getProperties().size(); j++) {//for CSSProperties List
                        name = cssP.getCSSElements().get(i).getProperties().get(j).getName();
                        value = cssP.getCSSElements().get(i).getProperties().get(j).getValue();
                        if (cssSplit[0].startsWith(name.trim())) {//if line needs to be altered
                            for (int k = 1; k < cssSplit.length; k++) {
                                cssP.getCSSElements().get(i).getProperties().add(new CSSProperty(("\t" + cssSplit[k]), value));
                                j++;//to prevent endless loop
                            }//for loop through css to add to List
                        }//if they match
                    }//for loop through CSSProperties List
                }//for CSSElements List
                cssLine = brCSS.readLine();
            } while (cssLine != null);

            //write updated List to string
            String finalOut = "";
            for (int i = 0; i < cssP.getCSSElements().size(); i++) {//for CSSElements List
                finalOut += cssP.getCSSElements().get(i).getName() + "\n";
                for (int j = 0; j < cssP.getCSSElements().get(i).getProperties().size(); j++) {//for CSSProperties List
                    finalOut += cssP.getCSSElements().get(i).getProperties().get(j).getName();
                    finalOut += cssP.getCSSElements().get(i).getProperties().get(j).getValue() + "\n";
                }
                finalOut += "}\n\n";
            }

            return finalOut;

            //end off all this. Don't mess with it.
        } catch (FileNotFoundException ex1) {
            Logger.getLogger(CBC.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (IOException ex) {
            Logger.getLogger(CBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
