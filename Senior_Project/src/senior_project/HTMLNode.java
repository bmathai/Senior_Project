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

    public HTMLNode() {
    }
    
    public ArrayList<HTMLNode> getChildren(){
        return this.children;
    }
    
    public HTMLNode getParent(){
        return this.parent;
    }
    
    public String getValue(){
        return this.value;
    }
    public void setValue(String val){
        this.value = val;
    }
}
