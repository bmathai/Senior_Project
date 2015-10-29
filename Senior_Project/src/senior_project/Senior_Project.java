/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

import java.awt.*;
import java.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

/**
 *
 * @author Blaise Mathai
 */
public class Senior_Project extends JFrame {
    
    private JPopupMenu popup;

    public JTextArea area = new JTextArea();
    public JTextArea areaDrag = new JTextArea();
    public JTextArea areaDrag1 = new JTextArea();
    public JTextArea areaDrag2 = new JTextArea();
    public JTextArea areaDrag3 = new JTextArea();
    public JPanel dragPanel = new JPanel();
    public Boolean dragPanelVisible = true;
    public Boolean dragPanelEdit = false;
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    public String currentFile = "Untitled";
    private boolean changed = false;
    private boolean dirty = false;
    public String currentPath = null;
    private CSSParser cssParser = null;
    private HTMLParser htmlParser = null;
    private String fileType = "";
    private final Set<Integer> pressed = new HashSet<>();// Set of currently pressed keys
    private String[] clipboard = new String[3];
    Multipaste mp = new Multipaste();
    
    //constructor
    public Senior_Project() {
        clipboard[0] = "";
        clipboard[1] = "";
        clipboard[2] = "";
    }

    public void Editor() {
        area.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 20, 0, 0, Color.white), 
            BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white))
        );
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(Color.white);
        area.setForeground(Color.getHSBColor(49, (float) 0.12, (float) .14));
        area.setCaretColor(Color.getHSBColor((float) .70, (float) .40, (float) .20));
        JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        TextLineNumber tln = new TextLineNumber(area);
        scroll.setRowHeaderView(tln);
        add(scroll, BorderLayout.CENTER);

        JMenuBar JMB = new JMenuBar();

        //custom look for menu bar
        //removeborders
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.getHSBColor((float) .70, (float) .40, (float) .20)));
        UIManager.put("Menu.border", BorderFactory.createLineBorder(Color.white, 0));
        UIManager.put("MenuItem.border", BorderFactory.createLineBorder(Color.white, 0));
        JMB.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float) .70, (float) .40, (float) .20), 4));
        //set backgrounds
        JMB.setBackground(Color.getHSBColor((float) .70, (float) .40, (float) .20));
        UIManager.put("MenuItem.background", Color.getHSBColor((float) .70, (float) .40, (float) .20));
        UIManager.put("MenuItem.foreground", Color.white);
        //set selection backgrounds
        UIManager.put("Menu.selectionBackground", Color.white);
        UIManager.put("MenuItem.selectionBackground", Color.white);
        //set text color
        UIManager.put("MenuItem.selectionForeground", Color.getHSBColor((float) .70, (float) .40, (float) .20));
        UIManager.put("Menu.foreground", Color.white);
        //set outlines
        UIManager.put("MenuItem.border", BorderFactory.createLineBorder(Color.getHSBColor((float) .70, (float) .40, (float) .20), 4));
        UIManager.put("Menu.selectionBorder", BorderFactory.createLineBorder(Color.white, 4));


        setJMenuBar(JMB);

        //adding options to menu bar
        JMenu file = new JMenu(" File ");
        JMenu edit = new JMenu(" Edit ");
        JMenu tools = new JMenu(" Tools ");//not finished
        JMenu comment = new JMenu(" Comment ");//not finished
        JMenu format = new JMenu(" Format ");//not finished
        JMB.add(file);
        JMB.add(edit);
        JMB.add(tools);//not finished
        JMB.add(comment);//finished
        JMB.add(format);//not finished
        //JMB.add(dragPanelBool);
        
        JButton dragPanelEditButton = new JButton(dragPanelEnableEdit);
        JButton dragPanelBoolButton = new JButton(dragPanelShowHide);
        JMB.add(Box.createHorizontalGlue());
        JMB.add(dragPanelBoolButton);
        JMB.add(dragPanelEditButton);
        
        

        file.add(New);
        file.add(Open);
        file.add(Save);
        file.add(Quit);
        file.add(SaveAs);

        for (int i = 0; i < 4; i++) {
            file.getItem(i).setIcon(null);
        }

        edit.add(Cut);
        edit.add(Copy);
        edit.add(Paste);
        edit.add(MultiPaste);
        edit.add(Insert);
        edit.getItem(0).setText("Cut");
        edit.getItem(1).setText("Copy");
        edit.getItem(2).setText("Paste");

        tools.add(colorPicker);

        comment.add(addComment);

        format.add(cssCBC);
        format.add(csshtmlFormat);

        Save.setEnabled(false);
        SaveAs.setEnabled(false);
        Insert.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        area.addKeyListener(k1);
        area.addMouseListener(m1);
        setTitle("Lavender Editor - " + currentFile);
        setVisible(true);
        
        areaDrag.addMouseListener(mDrag);
        areaDrag.setTransferHandler(new TransferHandler("text"));
        areaDrag1.addMouseListener(mDrag);
        areaDrag1.setTransferHandler(new TransferHandler("text"));
        areaDrag2.addMouseListener(mDrag);
        areaDrag2.setTransferHandler(new TransferHandler("text"));
        areaDrag3.addMouseListener(mDrag);
        areaDrag3.setTransferHandler(new TransferHandler("text"));
        
        areaDrag.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        areaDrag1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        areaDrag2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        areaDrag3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        areaDrag.setText(".html, body, p{\n"
                + "  padding: 0;\n"
                + "  margin: 0;\n"
                + "  font-size: 100%;\n"
                + "  font-weight: normal;\n"
                + "  font-style: normal;\n"
                + "}\n");
        areaDrag1.setText(".container{\n"
                + "  \n"
                + "}\n");
        areaDrag2.setText("<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "  <head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title>title</title>\n" +
                            "  <meta name=\"author\" content=\"name\">\n" +
                            "  <meta name=\"description\" content=\"description here\">\n" +
                            "  <meta name=\"keywords\" content=\"keywords,here\">\n" +
                            "  <link rel=\"stylesheet\" href=\"stylesheet.css\" type=\"text/css\">\n" +
                            "  </head>\n" +
                            "  <body>\n" +
                            "  \n" +
                            "  </body>\n" +
                            "</html>");
        areaDrag3.setText(".container{\n"
                + "  \n"
                + "}\n");
        
        areaDrag.setEditable(false);
        areaDrag1.setEditable(false);
        areaDrag2.setEditable(false);
        areaDrag3.setEditable(false);
        
        JScrollPane scrollDrag = new JScrollPane(areaDrag, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag1 = new JScrollPane(areaDrag1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag2 = new JScrollPane(areaDrag2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag3 = new JScrollPane(areaDrag3, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        getContentPane().setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        dragPanel.setLayout(new BoxLayout(dragPanel, BoxLayout.Y_AXIS));
        dragPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.setBackground( Color.getHSBColor((float) .70, (float) .40, (float) .20));
        dragPanel.setForeground(Color.white);
        panel.add(new JScrollPane(scroll));
        
        JLabel dragLabel = new JLabel("Blank Canvas");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag);
        dragLabel = new JLabel("CSS Element");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag1);
        dragLabel = new JLabel("HTML Skeleton");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag2);
        dragLabel = new JLabel("In Progress");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag3);
        dragPanel.setPreferredSize(new Dimension(200, 600));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(dragPanel, BorderLayout.EAST);
        
        pack();
    }

//    @Override
//    protected void processWindowEvent(WindowEvent e){
//        super.processWindowEvent(e);
//        System.out.println(e);
//        
//    }
    @Override
    protected void processContainerEvent(ContainerEvent e) {
        super.processContainerEvent(e);
        System.out.println(e);

    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //To change body of generated methods, choose Tools | Templates.
        super.addWindowListener( new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                JFrame frame = (JFrame)e.getSource();

                int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit the application?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION){
                    if ("Untitled".equals(currentFile)) {
                        if (JOptionPane.showConfirmDialog(null, "Wait! Would you like to save " + currentFile + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            saveFileAs();
                        }
                    } else {
                        saveOld();
                    }
                    System.exit(0);
                }
            }
        });
    }

    private void multiPaste(){
        
        JPopupMenu multiPastePopup = new JPopupMenu();
        multiPastePopup.add(new JMenuItem("testing"));
        multiPastePopup.setVisible(true);
        popup.setComponentPopupMenu(multiPastePopup);
        
        String selectedValue = mp.multiPaste(clipboard);
        if(selectedValue != null){
            area.insert(selectedValue, area.getCaretPosition());
        }
    }

    private KeyListener k1 = new KeyAdapter() {
        @Override
        public synchronized void keyPressed(KeyEvent e) {
            changed = true;
            markDirty();
            Save.setEnabled(true);
            SaveAs.setEnabled(true);
            //for hotkeys CTRL + C etc.
            pressed.add(e.getKeyCode());
            if (pressed.size() > 1) {
                // More than one key is currently pressed.
                // Iterate over pressed to get the keys.
                Iterator iter = pressed.iterator();
                while (iter.hasNext()) {
                    System.out.println(iter.next());
                }
                //ctrl+shift+c --> insert comment
                if (pressed.contains(17) && pressed.contains(16) && pressed.contains(67)){
                    pressed.clear();
                    if(fileType == "html"){
                        insertComment(0);
                    }
                    else {
                        insertComment(1);
                    }
                }
                //ctrl+s --> save
                else if (pressed.contains(17) && pressed.contains(83)){
                    pressed.clear();
                    if ("Untitled".equals(currentFile)) {
                        saveFileAs();
                    } else if (changed){
                        saveFile(currentFile);
                    }
                }
                //ctrl+c --> copy (multi)
                else if (pressed.contains(17) && pressed.contains(67)){
                    pressed.clear();
                    clipboard[2] = clipboard[1];
                    clipboard[1] = clipboard[0];
                    clipboard[0] = area.getSelectedText();
                }
                //ctrl+x --> paste (multi)
                else if (pressed.contains(17) && pressed.contains(88)){
                    pressed.clear();
                    String selectedValue = mp.multiPaste(clipboard);
                    if(selectedValue != null){
                        area.insert(selectedValue, area.getCaretPosition());
                    }
                }
            }
        }
        @Override
        public synchronized void keyReleased(KeyEvent e) {
            pressed.remove(e.getKeyCode());
        }
    };

    Action colorPicker = new AbstractAction("Color Picker") {
        public void actionPerformed(ActionEvent e) {
            new ColorPicker();
        }
    };

    Action addComment = new AbstractAction("Insert Comment") {
        public void actionPerformed(ActionEvent e) {
            if("html".equals(fileType)){
                insertComment(0);
            }
            else {
                insertComment(1);
            }
            changed = true;
            markDirty();
            Save.setEnabled(true);
            SaveAs.setEnabled(true);
        }
    };

    Action cssCBC = new AbstractAction("CSS Cross Browser Format") {
        public void actionPerformed(ActionEvent e) {
            runParser();
            if (cssParser != null) {
                CBC newCBC = new CBC();
                String replacementText = newCBC.cbc(cssParser, currentPath);
                area.setText(replacementText);
                changed = true;
                markDirty();
                Save.setEnabled(true);
                SaveAs.setEnabled(true);
            }
        }
    };

    Action csshtmlFormat = new AbstractAction("Format") {
        public void actionPerformed(ActionEvent e) {
            boolean saved = false;
            runParser();
                if ("css".equals(fileType)){
                    if (saved || (cssParser != null)) {
                        CSSFormat cssformat = new CSSFormat(cssParser);
                        cssformat.removeDuplicates();
                        String replacementText = cssformat.formatCSS();
                        area.setText(replacementText);
                        changed = true;
                        markDirty();
                        Save.setEnabled(true);
                        SaveAs.setEnabled(true);
                    }
                }
                if("html".equals(fileType)){
                    runParser();
                    if (htmlParser != null) {
                        String replacementText = htmlParser.cleanHTML();
                        area.setText(replacementText);
                        changed = true;
                        markDirty();
                        Save.setEnabled(true);
                        SaveAs.setEnabled(true);
                    }
                }
            //}
        }
    };

    public void insertComment(int n) {
        if (n == 0) {
            area.insert("<!--COMMENT-->", area.getCaretPosition());
        }
        if (n == 1) {
            area.insert("/*COMMENT*/", area.getCaretPosition());
        }
        if (n == 2) {
            area.insert("/*COMMENT*/", area.getCaretPosition());
        }
    }
    
    //hide/show button for dragPanel
    Action dragPanelShowHide = new AbstractAction("dragPanelShowHide") {
            public void actionPerformed(ActionEvent e){
                    dragPanelVisible = !dragPanelVisible;
                    dragPanel.setVisible(dragPanelVisible);
            }
    };
    
    //dragPanelEnableEdit
    Action dragPanelEnableEdit = new AbstractAction("Edit") {
            public void actionPerformed(ActionEvent e){
                    dragPanelEdit = !dragPanelEdit;
                    areaDrag.setEditable(dragPanelEdit);
                    areaDrag1.setEditable(dragPanelEdit);
                    areaDrag2.setEditable(dragPanelEdit);
                    areaDrag3.setEditable(dragPanelEdit);
                    //remove or renable mouse listener mDrag
                    if(dragPanelEdit){
                        //change button text
                        dragPanelEnableEdit.putValue(Action.NAME, "Done");
                        //remove mouse listener mDrag
                        areaDrag.removeMouseListener(mDrag);
                        areaDrag1.removeMouseListener(mDrag);
                        areaDrag2.removeMouseListener(mDrag);
                        areaDrag3.removeMouseListener(mDrag);
                    } else {
                        //change button text
                        dragPanelEnableEdit.putValue(Action.NAME, "Edit");
                        //renable mouse listener mDrag
                        areaDrag.addMouseListener(mDrag);
                        areaDrag1.addMouseListener(mDrag);
                        areaDrag2.addMouseListener(mDrag);
                        areaDrag3.addMouseListener(mDrag);
                    }
            }
    };

    Action New = new AbstractAction("New") {
        public void actionPerformed(ActionEvent e) {
            if ("Untitled".equals(currentFile)) {
                if (JOptionPane.showConfirmDialog(null, "Wait! Would you like to save " + currentFile + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    saveFileAs();
                }
            } else {
                saveOld();
            }
            area.setText(null);
            currentFile = "Untitled";
            setTitle("Lavender Editor - " + currentFile);
            SaveAs.setEnabled(true);
        }
    };

    //set fileType accordingly
    Action Open = new AbstractAction("Open") {
        public void actionPerformed(ActionEvent e) {
            saveOld();
            markClean();
            if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentPath = dialog.getSelectedFile().getAbsolutePath();
                //System.out.println(currentPath);
                readInFile(dialog.getSelectedFile().getAbsolutePath());
                setTitle("Lavender Editor - " + currentFile);
                //run parser
                runParser();
            }
            SaveAs.setEnabled(true);
        }
    };

    Action Save = new AbstractAction("Save") {
        public void actionPerformed(ActionEvent e) {
            if (!currentFile.equals("Untitled")) {
                saveFile(currentFile);
                setTitle("Lavender Editor - " + currentFile);
            } else {
                saveFileAs();
                setTitle("Lavender Editor - " + currentFile);
            }
        }
    };

    Action SaveAs = new AbstractAction("Save As...") {
        public void actionPerformed(ActionEvent e) {
            saveFileAs();
            setTitle("Lavender Editor - " + currentFile);
        }
    };

    Action Quit = new AbstractAction("Quit") {
        public void actionPerformed(ActionEvent e) {
            saveOld();
            System.exit(0);
        }
    };

    ActionMap m = area.getActionMap();
    Action Cut = m.get(DefaultEditorKit.cutAction);
    Action Copy = m.get(DefaultEditorKit.copyAction);
    Action Paste = m.get(DefaultEditorKit.pasteAction);
    
    
    Action MultiPaste = new AbstractAction("Multi-paste"){
        public void actionPerformed(ActionEvent e) {
            multiPaste();
        }
    };
    
    Action Insert = new AbstractAction("Insert Link Element") {
        public void actionPerformed(ActionEvent e) {
            /*Do this later*/
        }
    };
    
    private MouseListener mDrag = new MouseAdapter(){
        @Override
        public synchronized void mousePressed(MouseEvent e) {
           //drag and drop text
            System.out.println("Mouse pressed " + e.getButton());
           JComponent jc = (JComponent)e.getSource();
           TransferHandler th = jc.getTransferHandler();
           th.exportAsDrag(jc, e, TransferHandler.COPY);
        }
        @Override
        public synchronized void mouseClicked(MouseEvent e) {
            
        }
    };
    
    private MouseListener m1 = new MouseAdapter() {
        @Override
        public synchronized void mousePressed(MouseEvent e) {
           //right click
           System.out.println("Mouse pressed " + e.getButton());
           if(e.getButton() == 3){
               rightClickBox();
           }
        }
        
        @Override
        public synchronized void mouseClicked(MouseEvent e) {
            
        }
        
        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            
        }
        
        @Override
        public synchronized void mouseEntered(MouseEvent e) {
            
        }
        
        @Override
        public synchronized void mouseExited(MouseEvent e) {
            
        }
    };

    public void saveFileAs() {
        if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            saveFile(dialog.getSelectedFile().getAbsolutePath());
        }
    }

    public void saveOld() {
        if (changed) {
            if (JOptionPane.showConfirmDialog(this, "Wait! Would you like to save " + currentFile + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saveFile(currentFile);
            }
        }
    }

    private void readInFile(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            area.read(r, null);
            r.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
        } catch (IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "File " + fileName + " not found!");
        }
    }

    public void saveFile(String fileName) {
        try {
            FileWriter w = new FileWriter(fileName);
            area.write(w);
            w.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
            Save.setEnabled(false);
            this.markClean();
            runParser();
        } catch (IOException e) {
        }
    }
    
    public void runParser(){
        String currentType = currentPath.substring(currentPath.length() - 3);            
            if(currentType.equals("css")){
                fileType = "css";
                cssParser = new CSSParser(area.getText());
            } else if (currentType.equals("tml")){
                fileType = "html";
                Insert.setEnabled(true);
                htmlParser = new HTMLParser(area.getText());
            } else{
                fileType = "unknown";
            }
    }

    public void markDirty() {
        if (!this.dirty) {
            String dirtyTitle = this.getTitle() + "*";
            this.setTitle(dirtyTitle);
            this.dirty = true;
        }
    }

    public void markClean() {
        if (this.dirty) {
            this.dirty = false;
        }
    }
    
    public void rightClickBox(){
        popup = new JPopupMenu();
        popup.setVisible(true);
        Cut.putValue(Action.NAME, "Cut");
        Copy.putValue(Action.NAME, "Copy");
        Paste.putValue(Action.NAME, "Paste");
        popup.add(Cut);
        popup.add(Copy);
        popup.add(Paste);
        popup.add(addComment);
        //popup.add(MultiPaste);
        popup.add(Insert);
        popup.setBackground(Color.getHSBColor((float) .70, (float) .40, (float) .20));
        
        //multipaste mess
        String[] shortClipboard = new String [3];
        for(int i = 0; i < clipboard.length; i++){
            if(clipboard[i].length() > 13){
                shortClipboard[i] = " " + clipboard[i].substring(0,10) + "...";//" " is a hack to get around joptionpane <html>
            } else {
                shortClipboard[i] = " " + clipboard[i];
                System.out.println(shortClipboard[i]);
            }
        }
        JMenu mpMenu = new JMenu("Multipaste");
        
        Action mpOption0 = new AbstractAction(shortClipboard[0]){
            public void actionPerformed(ActionEvent e) {
                area.insert(clipboard[0], area.getCaretPosition());
            }
        };
        Action mpOption1 = new AbstractAction(shortClipboard[1]){
            public void actionPerformed(ActionEvent e) {
                area.insert(clipboard[1], area.getCaretPosition());
            }
        };
        Action mpOption2 = new AbstractAction(shortClipboard[2]){
            public void actionPerformed(ActionEvent e) {
                area.insert(clipboard[2], area.getCaretPosition());
            }
        };
        
        
        if(!clipboard[0].equals("")){
            mpMenu.add(mpOption0);
        }
        if(!clipboard[1].equals("")){
            mpMenu.add(mpOption1);
        }
        if(!clipboard[2].equals("")){
            mpMenu.add(mpOption2);
        }
        popup.add(mpMenu);
        area.setComponentPopupMenu(popup);
        
        
        
    }

    public static void main(String[] args) {
        Senior_Project sp = new Senior_Project();
        sp.Editor();
    }

}
