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
public class HTMLTree {
    private HTMLNode root;
    
    /**
     *
     * @param newValue
     * @param path
     */
    public void placeNode(String newValue, int[] path){
        HTMLNode current = root;
        for(int i = 0; i < path.length; i++){
            if(current.getChildren().get(i) == null){
                current.getChildren().add(i, new HTMLNode());
            }
            current = current.getChildren().get(i);
        }
        current.setValue(newValue);
    }
}
