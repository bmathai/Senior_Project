/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package senior_project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Blaise Mathai
 */
public class CSSElement {
    private List<CSSProperty> properties = new ArrayList<CSSProperty>(5);
    private String name = "";
    
    public CSSElement(String n){
        this.name = n;
    }
    
    public void addProperty(CSSProperty prop){
        this.properties.add(prop);
    }
    
    public CSSProperty getProperty(String propName){
        for(int i = 0; i < this.properties.size(); i++){
            CSSProperty prop = this.properties.get(i);
            if(prop.getName().trim().equals((propName).trim())){
                return prop;
            }
        }
        return null;
    }
    
    public String getName(){
        return this.name;
    }
    
    public List<CSSProperty> getProperties(){
        return this.properties;
    }
    
    public boolean hasProperty(String prop){
        for(int i = 0; i < properties.size(); i++){
            if(properties.get(i).getName().trim().equals(prop.trim())){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasMoreThanOneProperty(String prop){
        int num = 0;
        for(int i = 0; i < properties.size(); i++){
            if(properties.get(i).getName().trim().equals(prop.trim())){
                num++;
            }
        }
        if(num > 1){
            return true;
        }
        return false;
    }
    
    public void removeFirstProperty(String prop){
        for(int i = 0; i < properties.size(); i++){
            if(properties.get(i).getName().trim().equals(prop.trim())){
                properties.remove(i);
                i = properties.size();
            }
        }
    }
    
    public void removeProperty(String prop){
        for(int i = 0; i < properties.size(); i++){
            if(properties.get(i).getName().trim().equals(prop.trim())){
                properties.remove(i);
            }
        }
    }
    
}
