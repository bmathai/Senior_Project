/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senior_project;

import java.util.ArrayList;

/**
 *
 * @author Blaise Mathai
 */
public class HTMLNode {

    private ArrayList<HTMLNode> children;
    private HTMLNode parent;
    private String value;

    /**
     *
     */
    public HTMLNode() {
    }
    
    /**
     *
     * @return
     */
    public ArrayList<HTMLNode> getChildren(){
        return this.children;
    }
    
    /**
     *
     * @return
     */
    public HTMLNode getParent(){
        return this.parent;
    }
    
    /**
     *
     * @return
     */
    public String getValue(){
        return this.value;
    }

    /**
     *
     * @param val
     */
    public void setValue(String val){
        this.value = val;
    }
}
