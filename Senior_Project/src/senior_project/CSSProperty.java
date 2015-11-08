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
public class CSSProperty {
    private String name = "";
    private String value = "";
    
    /**
     *
     * @param n
     * @param v
     */
    public CSSProperty(String n, String v){
        this.name = n;
        this.value = v;
    }
    
    /**
     *
     * @return
     */
    public String getName(){
        return this.name;
    }
    
    /**
     *
     * @param n
     */
    public void setName(String n){
        this.name = n;
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
     * @param v
     */
    public void setValue(String v){
        this.value = v;
    }

}
