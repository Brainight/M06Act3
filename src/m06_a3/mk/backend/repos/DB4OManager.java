/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m06_a3.mk.backend.repos;

import m06_a3.mk.customdb4o.Db4oEntity;
import m06_a3.mk.customdb4o.IdentityDB4OManager;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brainight
 */
public class DB4OManager extends IdentityDB4OManager {

    public DB4OManager(String db, Class... identityClasses) {
        super(db, identityClasses);
    }

    public ObjectContainer getSession() {
        this.openSession();
        return this.getSession();
    }

    @Override
    public synchronized <T extends Db4oEntity> void update(T o) {
        try {
            super.update(o);
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public synchronized <T extends Db4oEntity> long[] save(Collection<T> o) {
        long[] ids = new long[o.size()];
        try {
            super.save(o);
           
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }finally{
            return ids;
        }
    }

    @Override
    public synchronized <T extends Db4oEntity> long save(T o){
        long id = -1;
        try {
            id = super.save(o);
           
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }finally{
            return id;
        }
    }

    @Override
    public synchronized <T extends Db4oEntity> void remove(T o) {
        try {
            super.remove(o);
            System.out.println("[Transaction] " + o.getClass().getTypeName() + " borrado con exito!");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
