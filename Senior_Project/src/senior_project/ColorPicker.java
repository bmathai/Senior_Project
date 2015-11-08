/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

import java.awt.Color;
import javax.swing.*;
import javax.swing.colorchooser.*;

/**
 *
 * @author Blaise Mathai
 */
public class ColorPicker {

    private JFrame frame = new JFrame("Lavender Editor - Hex Color Picker");

    /**
     *
     */
    public ColorPicker() {
        //color picker stuff
        JPanel colorPan = new JPanel();
        JColorChooser cp = new JColorChooser();
        AbstractColorChooserPanel panels[] = cp.getChooserPanels();
        //remove unwanted panels
        for (AbstractColorChooserPanel accp : panels) {
            if (!accp.getDisplayName().equals("RGB")) {
                cp.removeChooserPanel(accp);
            } else {
                accp.setOpaque(true);
                accp.setBackground(Color.white);
            }
        }
        colorPan.add(cp);
        frame.add(colorPan);
        frame.setVisible(true);
        frame.setSize(650, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //change color scheme
        colorPan.setOpaque(true);
        colorPan.setBackground(Color.white);

    }

}
