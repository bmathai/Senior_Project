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
    
    public CSSProperty(String n, String v){
        this.name = n;
        this.value = v;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String n){
        this.name = n;
    }

    public String getValue(){
        return this.value;
    }
    
    public void setValue(String v){
        this.value = v;
    }

}
