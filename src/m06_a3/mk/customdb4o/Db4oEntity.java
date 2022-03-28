/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m06_a3.mk.customdb4o;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * @author Brainight
 */
public class Db4oEntity implements Serializable{
    
    public static final String ID_FIELD_NAME = "id";
    private Long id = Long.valueOf(0);
    
    public Db4oEntity(){}
    
    public long getId(){
        return this.id == null ? 0 : this.id;
    }
    
    public <T extends Db4oEntity> void update(T o) throws Exception{
        if(o.getClass() != this.getClass()){
            throw new IllegalArgumentException("Error: Cannot pass object type '" + o.getClass() + "' to update object of type '" + this.getClass() + "'");
        }
        
        Field[] fs = this.getClass().getDeclaredFields();
        Field[] newFs = o.getClass().getDeclaredFields();
        
        for(int i = 0; i < fs.length; i++){
            fs[i].setAccessible(true);
            newFs[i].setAccessible(true);
            
            if(fs[i].getName().equals(ID_FIELD_NAME)){ // Id is not updateable!
                continue;
            }
            
            if(!fs[i].get(this).equals(newFs[i].get(o))){
                fs[i].set(this, newFs[i].get(o));
            }
            fs[i].setAccessible(false);
            newFs[i].setAccessible(false);
        }
            
    }
}
