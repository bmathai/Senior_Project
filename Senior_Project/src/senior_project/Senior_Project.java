/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

import java.awt.*;
import java.*;
import java.awt.datatransfer.Transferable;
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
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import jsyntaxpane.*;
/**
 *
 * @author Blaise Mathai
 */
/**
 *  This class is the main class for Lavender Text Editor. It
 *  initiates the GUI and responds to user's interaction with
 *  the software.
 */
public class Senior_Project extends JFrame {
    
    private JPopupMenu popup;
    private final Border emptyBorder = BorderFactory.createEmptyBorder();
    private boolean theme = true;
    
    /**
     *
     */
    public final UndoManager undo = new UndoManager();

    /**
     *
     */
    public JTextArea area = new JTextArea();
    private final JTextArea[] areaDrag = new JTextArea[4];
    private JPanel panel = new JPanel();
    
    private JPanel dragPanel = new JPanel();
    private Boolean dragPanelVisible = true;
    private Boolean dragPanelEdit = false;
    private JButton dragPanelEditButton;
    private JButton dragPanelBoolButton;
    
    private JPanel footer;
    private Boolean footerVisible;
    private JButton footerBoolButton;
    private JButton footerFindButton;
    private JButton footerReplaceButton;
    private JTextField find;
    private JTextField replace;
    private JLabel findLabel;
    private JLabel replaceLabel;
    
    
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));

    /**
     *
     */
    public String currentFile = "Untitled";
    private boolean changed = false;
    private boolean dirty = false;

    /**
     *
     */
    public String currentPath = null;
    private CSSParser cssParser = null;
    private HTMLParser htmlParser = null;
    private String fileType = "";
    private final Set<Integer> pressed = new HashSet<>();// Set of currently pressed keys
    private String[] clipboard = new String[3];
    Multipaste mp = new Multipaste();
    
    //constructor

    /**
     *
     */
        public Senior_Project() {
        clipboard[0] = "";
        clipboard[1] = "";
        clipboard[2] = "";
        areaDrag[0] = new JTextArea();
        areaDrag[1] = new JTextArea();
        areaDrag[2] = new JTextArea();
        areaDrag[3] = new JTextArea();
        area.setBorder(null);
        area.setBorder(emptyBorder);
        
        footerVisible = true;
    }

    /**
     *
     */
    public void Editor() {
        
         area.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
              undo.addEdit(evt.getEdit());
            }
          });
        
        area.getActionMap().put("Undo", new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent evt) {
              try {
                if (undo.canUndo()) {
                  undo.undo();
                }
              } catch (CannotUndoException e) {
              }
            }
          });
        
        area.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        
        area.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 20, 0, 0, Color.white), 
            BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white))
        );
        area.setFont(new Font("Verdana", Font.PLAIN, 12));
        area.setBackground(Color.white);
        area.setForeground(Color.DARK_GRAY.darker());
        area.setCaretColor(Color.magenta);
        //area.setTabSize(2);
        JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(emptyBorder);
        scroll.setBorder(null);
        LineNumber tln = new LineNumber(area);
        scroll.setRowHeaderView(tln);
        add(scroll, BorderLayout.CENTER);

        JMenuBar JMB = new JMenuBar();
        JMB.setBorder(emptyBorder);
        DefaultSyntaxKit.initKit();
        //custom look for menu bar
        //removeborders
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.DARK_GRAY.darker().darker()));
        UIManager.put("Menu.border", BorderFactory.createLineBorder(Color.white, 0));
        UIManager.put("MenuItem.border", BorderFactory.createLineBorder(Color.white, 0));
        JMB.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY.darker().darker(), 4));
        //set backgrounds
        JMB.setBackground(Color.DARK_GRAY.darker().darker());
        UIManager.put("MenuItem.background", Color.DARK_GRAY.darker().darker());
        UIManager.put("MenuItem.foreground", Color.white);
        //set selection backgrounds
        UIManager.put("Menu.selectionBackground", Color.white);
        UIManager.put("MenuItem.selectionBackground", Color.white);
        //set text color
        UIManager.put("MenuItem.selectionForeground", Color.DARK_GRAY.darker().darker());
        UIManager.put("Menu.foreground", Color.white);
        //set outlines
        UIManager.put("MenuItem.border", BorderFactory.createLineBorder(Color.DARK_GRAY.darker().darker(), 4));
        UIManager.put("Menu.selectionBorder", BorderFactory.createLineBorder(Color.white, 4));


        setJMenuBar(JMB);

        //adding options to menu bar
        JMenu file = new JMenu(" File ");
        JMenu edit = new JMenu(" Edit ");
        JMenu tools = new JMenu(" Tools ");//not finished
        JMenu comment = new JMenu(" Comment ");//not finished
        JMenu format = new JMenu(" Format ");//not finished
        JMenu preferences = new JMenu(" Preferences ");//not finished
        JMB.add(file);
        JMB.add(edit);
        JMB.add(tools);//not finished
        JMB.add(comment);//finished
        JMB.add(format);//not finished
        JMB.add(preferences);//not finished
        //JMB.add(dragPanelBool);
        
        dragPanelEditButton = new JButton(dragPanelEnableEdit);
        dragPanelEditButton.setBackground(Color.DARK_GRAY.darker().darker());
        dragPanelEditButton.setForeground(Color.white);
        dragPanelBoolButton = new JButton(dragPanelShowHide);
        dragPanelBoolButton.setBackground(Color.DARK_GRAY.darker().darker());
        dragPanelBoolButton.setForeground(Color.white);
        
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
        
        preferences.add(Change_Theme);

        Save.setEnabled(false);
        SaveAs.setEnabled(false);
        Insert.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        area.addKeyListener(k1);
        area.addMouseListener(m1);
        setTitle("Lavender Editor - " + currentFile);
        setVisible(true);
        
        for(int i = 0; i < areaDrag.length; i++){
            areaDrag[i].addMouseListener(mDrag);
            areaDrag[i].setTransferHandler(new TransferHandler("text"));
            areaDrag[i].getDropTarget().setActive(false);
            areaDrag[i].setBorder(emptyBorder);
            areaDrag[i].setFont(new Font("Verdana", Font.PLAIN, 12));
            areaDrag[i].setEditable(false);
        }
        
        areaDrag[0].setText(".html, body, p{\n"
                        + "  padding: 0;\n"
                        + "  margin: 0;\n"
                        + "  font-size: 100%;\n"
                        + "  font-weight: normal;\n"
                        + "  font-style: normal;\n"
                        + "}\n");
        areaDrag[1].setText(".container{\n"
                        + "  \n"
                        + "}\n");
        areaDrag[2].setText("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>title</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "  \n" +
                        "  </body>\n" +
                        "</html>");
        areaDrag[3].setText("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>title</title>\n" +
                        "  <meta name=\"author\" content=\"name\">\n" +
                        "  <meta name=\"description\" content=\"description here\">\n" +
                        "  <meta name=\"keywords\" content=\"keywords,here\">\n" +
                        "  <link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/vnd.microsoft.icon\">\n" +
                        "  <link rel=\"stylesheet\" href=\"stylesheet.css\" type=\"text/css\">\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "  \n" +
                        "  </body>\n" +
                        "</html>");
        
        
        JScrollPane scrollDrag = new JScrollPane(areaDrag[0], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag1 = new JScrollPane(areaDrag[1], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag2 = new JScrollPane(areaDrag[2], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane scrollDrag3 = new JScrollPane(areaDrag[3], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollDrag.setBorder(emptyBorder);
        scrollDrag1.setBorder(emptyBorder);
        scrollDrag2.setBorder(emptyBorder);
        scrollDrag3.setBorder(emptyBorder);
        
        getContentPane().setLayout(new BorderLayout());
        
        //footer
        footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
        footer.setPreferredSize(new Dimension(800, 30));
        footer.setBackground( Color.DARK_GRAY.darker().darker());
        //button
        footerBoolButton = new JButton(footerHide);
        footerFindButton = new JButton(footerFind);
        footerReplaceButton = new JButton(footerReplace);
        footerBoolButton.setBackground(Color.DARK_GRAY.darker().darker());
        footerBoolButton.setForeground(Color.white);
        footerFindButton.setBackground(Color.DARK_GRAY.darker().darker());
        footerFindButton.setForeground(Color.white);
        footerReplaceButton.setBackground(Color.DARK_GRAY.darker().darker());
        footerReplaceButton.setForeground(Color.white);
        //text fields find replace
        find = new JTextField("", 20);
        find.setBorder(BorderFactory.createMatteBorder(4, 10, 4, 10, Color.DARK_GRAY.darker().darker()));
        replace = new JTextField("", 20);
        replace.setBorder(BorderFactory.createMatteBorder(4, 10, 4, 10, Color.DARK_GRAY.darker().darker()));
        //add content to footer
        footer.add(footerBoolButton);
        footer.add(footerFindButton);
        footer.add(find);
        footer.add(footerReplaceButton);
        footer.add(replace);
        
        
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(emptyBorder);
        panel.add(scroll);
        
        dragPanel.setLayout(new BoxLayout(dragPanel, BoxLayout.Y_AXIS));
        dragPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.setBackground( Color.DARK_GRAY.darker().darker());
        dragPanel.setForeground(Color.white);
        
        JLabel dragLabel = new JLabel("CSS Blank Canvas");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag);
        dragLabel = new JLabel("CSS Element");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag1);
        dragLabel = new JLabel("HTML Basic Skeleton");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag2);
        dragLabel = new JLabel("HTML Full Skeleton");
        dragLabel.setForeground(Color.white);
        dragLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dragPanel.add(dragLabel);
        dragPanel.add(scrollDrag3);
        dragPanel.setPreferredSize(new Dimension(200, 600));
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(dragPanel, BorderLayout.EAST);
        getContentPane().add(footer, BorderLayout.SOUTH);
        
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

    private void multiPaste() throws BadLocationException{
        
        JPopupMenu multiPastePopup = new JPopupMenu();
        multiPastePopup.add(new JMenuItem("testing"));
        multiPastePopup.setVisible(true);
        popup.setComponentPopupMenu(multiPastePopup);
        
        String selectedValue = mp.multiPaste(clipboard);
        if(selectedValue != null){
            area.insert(selectedValue, area.getCaretPosition());
        }
    }
    
    private void changeTheme() throws BadLocationException{
        if(theme == true){
            theme = false;
            area.insert("_", 0);
            area.setFont(new Font("Verdana", Font.PLAIN, 12));
            area.setForeground(Color.WHITE);
            area.setBackground(Color.DARK_GRAY.darker());
            area.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 20, 0, 0, Color.DARK_GRAY.darker()), 
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.DARK_GRAY.darker()))
            );
            area.replaceRange(null, 0, 1);
            for(int i = 0; i < areaDrag.length; i++){
                areaDrag[i].setForeground(Color.WHITE);
                areaDrag[i].setBackground(Color.DARK_GRAY.darker());  
            }
        } else {
            theme = true;
            area.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 20, 0, 0, Color.white), 
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white))
            );
            area.setFont(new Font("Verdana", Font.PLAIN, 12));
            area.setBackground(Color.white);
            area.setForeground(Color.DARK_GRAY.darker());
            for(int i = 0; i < areaDrag.length; i++){
                areaDrag[i].setForeground(Color.DARK_GRAY.darker());
                areaDrag[i].setBackground(Color.white);  
            }
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
                //ctrl+f --> find/replace show/hide
                if (pressed.contains(17) && pressed.contains(70)){
                    pressed.clear();
                    hideFooter();
                }
                //ctrl+shift+c --> insert comment
                if (pressed.contains(17) && pressed.contains(16) && pressed.contains(67)){
                    pressed.clear();
                    if(fileType == "html"){
                        try {
                            insertComment(0);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else {
                        try {
                            insertComment(1);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                //find/replace function
                else if(pressed.contains(17) && pressed.contains(70)){
//                    MultiHighlight mh = new MultiHighlight(area, "aeiouAEIOU");
//                    mh.highlight();
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
                try {
                    insertComment(0);
                } catch (BadLocationException ex) {
                    Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    insertComment(1);
                } catch (BadLocationException ex) {
                    Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        public void actionPerformed(ActionEvent e) throws NullPointerException{
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

    /**
     *
     * @param n
     * @throws BadLocationException
     */
    public void insertComment(int n) throws BadLocationException {
        if (n == 0) {
            System.out.println(area.getCaretPosition());
            area.insert("<!--COMMENT-->", area.getCaretPosition());
        }
        if (n == 1) {
            System.out.println(area.getCaretPosition());
            area.insert("/*COMMENT*/", area.getCaretPosition());
        }
        if (n == 2) {
            area.insert("/*COMMENT*/", area.getCaretPosition());
        }
    }
    
    //hide/show button for dragPanel
    Action dragPanelShowHide = new AbstractAction("Hide") {
            public void actionPerformed(ActionEvent e){
                    dragPanelVisible = !dragPanelVisible;
                    dragPanel.setVisible(dragPanelVisible);
                    if(dragPanelVisible){
                        dragPanelShowHide.putValue(Action.NAME, "Hide");
                    } else {
                        dragPanelShowHide.putValue(Action.NAME, "Show");
                    }
            }
    };
    
    //hide/show footer
    Action footerHide = new AbstractAction("Hide") {
            public void actionPerformed(ActionEvent e){
                    hideFooter();
            }
    };
    private void hideFooter(){
        footerVisible = !footerVisible;
                    footer.setVisible(footerVisible);
    }
    
    //dragPanelEnableEdit
    Action dragPanelEnableEdit = new AbstractAction("Edit") {
            public void actionPerformed(ActionEvent e){
                    dragPanelEdit = !dragPanelEdit;
                    for(int i = 0; i < areaDrag.length; i++){
                        areaDrag[i].setEditable(dragPanelEdit);
                    }
                    //remove or renable mouse listener mDrag
                    if(dragPanelEdit){
                        //change button text
                        dragPanelEnableEdit.putValue(Action.NAME, "Done");
                        //remove mouse listener mDrag
                        for(int i = 0; i < areaDrag.length; i++){
                            areaDrag[i].removeMouseListener(mDrag);
                        }
                    } else {
                        //change button text
                        dragPanelEnableEdit.putValue(Action.NAME, "Edit");
                        //renable mouse listener mDrag
                        for(int i = 0; i < areaDrag.length; i++){
                            areaDrag[i].addMouseListener(mDrag);
                        }
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
            try {
                multiPaste();
            } catch (BadLocationException ex) {
                Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    Action Insert = new AbstractAction("Insert Link Element") {
        public void actionPerformed(ActionEvent e) {
            /*Do this later*/
        }
    };
    
    Action Change_Theme = new AbstractAction("Change Theme"){
        public void actionPerformed(ActionEvent e) {
            try {
                changeTheme();
            } catch (BadLocationException ex) {
                Logger.getLogger(Senior_Project.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    Action footerFind = new AbstractAction("Find:"){
        public void actionPerformed(ActionEvent e) {
            find(find.getText());
        }
    };
    
    Action footerReplace = new AbstractAction("Replace With:"){
        public void actionPerformed(ActionEvent e) {
            findAndReplace(find.getText(), replace.getText());
        }
    };
    
    private void find(String str){
       MultiHighlight mh = new MultiHighlight(area, str);
       mh.highlight();
    }
    
    private void findAndReplace(String find, String replace){
       FindReplace fp = new FindReplace(area, find, replace);
       area = fp.replaceString();
    }

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
        
        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            
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
           if(e.getButton() == 1){
           }
        }
        
        @Override
        public synchronized void mouseClicked(MouseEvent e) {
            
        }
        
        @Override
        public synchronized void mouseReleased(MouseEvent e) {
            if (area.getSelectedText() != null){ //see if they selected something 
                String s = area.getSelectedText();
                MultiHighlight mh = new MultiHighlight(area, s);
                    mh.highlight();
                    
            }
        }
        
        @Override
        public synchronized void mouseEntered(MouseEvent e) {
            
        }
        
        @Override
        public synchronized void mouseExited(MouseEvent e) {
            
        }
    };

    /**
     *
     */
    public void saveFileAs() {
        if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            saveFile(dialog.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     *
     */
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

    /**
     *
     * @param fileName
     */
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
    
    /**
     *
     */
    public void runParser() throws NullPointerException{
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

    /**
     *
     */
    public void markDirty() {
        if (!this.dirty) {
            String dirtyTitle = this.getTitle() + "*";
            this.setTitle(dirtyTitle);
            this.dirty = true;
        }
        //parse for color coding
        //doc = syntaxhighlight.highlightSyntax(area, doc);
        
    }

    /**
     *
     */
    public void markClean() {
        if (this.dirty) {
            this.dirty = false;
        }
    }
    
    /**
     *
     */
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

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Senior_Project sp = new Senior_Project();
        sp.Editor();
    }

}
