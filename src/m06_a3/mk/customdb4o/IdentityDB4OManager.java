/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m06_a3.mk.customdb4o;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import m06_a3.mk.customdb4o.exceptions.FieldNotUniqueException;
import m06_a3.mk.customdb4o.exceptions.UnknownReferenceException;
import m06_a3.mk.customdb4o.exceptions.UnmatchingObjectsWithSameIdException;

/**
 *
 * @author Brainight
 */
public class IdentityDB4OManager {

    private boolean debugMode = false;
    private boolean supressErrors = false;

    private volatile Map<Class<?>, AtomicLong> identities = new HashMap<>(0);
    private ObjectContainer dbSession;

    Set<Class> classes;
    String dbName;

    public IdentityDB4OManager(String db, Class... identityClasses) {
        this.dbName = db;
        this.classes = new HashSet<>(List.of(identityClasses));
        this.setUp();

        System.out.println("#######################################");
    }

    public String getDbName() {
        return dbName;
    }

    private ObjectContainer openDBSession() {
        try {
            return Db4o.openFile(Db4o.newConfiguration(), this.dbName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void openSession() {
        if (this.dbSession == null || this.dbSession.ext().isClosed()) {
            this.dbSession = Db4o.openFile(Db4o.newConfiguration(), this.dbName);
        }
    }

    public void closeSession() {
        if (this.dbSession != null || !this.dbSession.ext().isClosed()) {
            this.dbSession.close();
        }
    }

    public boolean isSysErrTraces() {
        return debugMode;
    }

    public void setSysErrTraces(boolean sysErrTraces) {
        this.debugMode = sysErrTraces;
    }

    public boolean isSupressErrors() {
        return supressErrors;
    }

    public void setSupressErrors(boolean supressErrors) {
        this.supressErrors = supressErrors;
    }

    private void setUp() {
        for (Class c : classes) {

            ObjectContainer dbSession = openDBSession();
            try {
                List<Db4oEntity> cs = this.<Db4oEntity>getAllObjects(c, dbSession);

                if (cs == null || cs.size() == 0) {
                    identities.put(c, new AtomicLong(0));
                    continue;
                }

                Optional<Db4oEntity> dbE = cs.stream().max(Comparator.comparing(e -> e.getId()));
                if (dbE.isPresent()) {
                    identities.put(c, new AtomicLong(dbE.get().getId() + 1));
                    continue;
                }

                identities.put(c, new AtomicLong(0));
            } finally {
                dbSession.close();
            }

        }
    }

    private synchronized <T extends Db4oEntity> long save(T o, ObjectContainer dbSession) throws Exception {
        long genId = -1;
        if (this.objectExists(o, dbSession)) {
            System.err.println("Error: Object -> " + o.toString() + " of type " + o.getClass().getName() + " exists in database.");
        }

        try {
            checkIntegrity(o, dbSession);
            AtomicLong l = this.identities.get(o.getClass());
            Field f = o.getClass().getSuperclass().getDeclaredField("id");
            f.setAccessible(true);
            f.set(o, l.incrementAndGet());
            dbSession.store(o);
            System.out.println("[TRANSACTION] Saved: " + o.toString());
            dbSession.commit();
            genId = ((Long)f.get(o)).longValue();
            f.setAccessible(false);
        } catch (Exception e) {
            dbSession.rollback();
            if (!this.supressErrors) {
                System.err.println(e.getMessage());
                System.err.println("ROLLBACK TRANSACTION!");
            }

            if (this.debugMode) {
                e.printStackTrace();
            }

            throw new Exception(e);
        }finally{
            return genId;
        }

    }

    public synchronized <T extends Db4oEntity> long[] save(Collection<T> t) throws Exception {
        this.openSession();
        long[] genIds = new long[t.size()];
        for(T o : t){
            this.save(o, this.dbSession);
        }
        this.closeSession();
        return genIds;
    }
    
    public synchronized <T extends Db4oEntity> long save(T o) throws Exception{
        this.openSession();
        long genId = this.save(o, this.dbSession);
        this.closeSession();
        return genId;
    }

    private <T extends Db4oEntity> void checkIntegrity(T o, ObjectContainer dbSession) throws Exception {
        Field[] fs = o.getClass().getDeclaredFields();
        for (Field f : fs) {
            checkUnique(f, o, dbSession);
            checkReferenceIntegrity(f, o, dbSession);
        }
    }
    

    private <T extends Db4oEntity> void checkUnique(Field f, T o, ObjectContainer dbSession) throws FieldNotUniqueException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, SecurityException {
        if (f.isAnnotationPresent(Db4oUniqueField.class)) {
            f.setAccessible(true);
            List<T> l = (List<T>) getAllObjects(o.getClass(), dbSession);
            for (T obj : l) {
                Field iField = obj.getClass().getDeclaredField(f.getName());
                iField.setAccessible(true);
                if (iField.get(obj).equals(f.get(o))) {
                    throw new FieldNotUniqueException("An object of type '"
                            + obj.getClass().getName() + "' with field '" + f.getName() + "' == '" + f.get(o).toString() + "' already exists!");
                }
                iField.setAccessible(false);
            }
            f.setAccessible(false);
        }
    }

    private <T extends Db4oEntity> void checkReferenceIntegrity(Field f, T o, ObjectContainer dbSession) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, UnknownReferenceException, UnmatchingObjectsWithSameIdException, Exception {
        for (Class<?> c : this.identities.keySet()) {
            if (c.equals(f.getType())) {
                f.setAccessible(true);
                List<T> l = (List<T>) getAllObjects(c, dbSession);
                boolean refFound = false;
                boolean alteredObj = false;
                T dbInstanceObject = null;
                for (T obj : l) {
                    if (obj.getId() == ((Db4oEntity) f.get(o)).getId()) {
                        refFound = true;

                        if (!obj.equals(f.get(o))) {
                            alteredObj = true;
                            dbInstanceObject = obj;
                        } else {
                            f.set(o, obj);
                        }
                        break;
                    }
                }

                try {
                    if (!refFound) {
                        throw new UnknownReferenceException("Error: Cannot find a reference for referenced object '"
                                + f.getName() + "' -> " + f.get(o).toString() + "in the database.");
                    }

                    if (alteredObj) {
                        throw new UnmatchingObjectsWithSameIdException("Error: Object of type '"
                                + f.getType() + "' with 'id' == '" + ((Db4oEntity) f.get(o)).getId() + "' are different! Object seems to have been updated out of session"
                                + "\n[Passed Instance] " + f.get(o).toString() + " vs. [Database Instance] " + dbInstanceObject.toString());
                    }
                } finally {
                    f.setAccessible(false);
                }

            }
        }
    }

    private <T extends Db4oEntity> void update(T o, ObjectContainer dbSession) throws Exception {
        try {
            T modO = (T) this.findById(o.getId(), o.getClass(), dbSession);
            modO.update(o);
            dbSession.store(modO);
            System.out.println("[TRANSACTION] Updated " + o.toString());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public <T extends Db4oEntity> void update(T o) throws Exception {
        this.openSession();
        this.update(o, this.dbSession);
        this.closeSession();
    }

    private <T extends Db4oEntity> void remove(T o, ObjectContainer dbSession) throws Exception {
        try {
            T modO = (T) this.findById(o.getId(), o.getClass(), dbSession);
            dbSession.delete(modO);
            System.out.println("[TRANSACTION] Deleted " + o.toString());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public <T extends Db4oEntity> void remove(T o) throws Exception {
        this.openSession();
        this.remove(o, this.dbSession);
        this.closeSession();
    }

    // ################## RETRIVING DATA METHODS ####################
    
    public <T extends Db4oEntity> List<T> findByPredicate(Predicate p){
         try {
            openSession();
            List<T> l = (List<T>)dbSession.query(p);
            return new ArrayList<T>(l);
        } finally {
            closeSession();
        }
    }
    private <T extends Db4oEntity> T findById(long id, Class<T> t, ObjectContainer dbSession) {

        ObjectSet os = dbSession.query(new Predicate() {
            public boolean match(T dbE) {
                return dbE.getClass().equals(t) && dbE.getId() == id;
            }
        });

        return os.hasNext() ? (T) os.next() : null;
    }

    public <T extends Db4oEntity> T findById(long id, Class<T> t) {
        try {
            openSession();
            ObjectSet os = dbSession.query(new Predicate() {
                public boolean match(T dbE) {
                    return dbE.getClass().equals(t) && dbE.getId() == id;
                }
            });
            return os.hasNext() ? (T) os.next() : null;
        } finally {
            closeSession();
        }
    }

    private <T extends Db4oEntity> boolean objectExists(T o, ObjectContainer dbSession) {
        ObjectSet os = dbSession.query(new Predicate() {
            public boolean match(T dbE) {
                return dbE.getId() == o.getId() && dbE.getClass().equals(o.getClass());
            }
        });

        return os.size() != 0;
    }

    private <T extends Db4oEntity> boolean objectExists(T o) {
        try {
            this.openSession();
            return this.objectExists(o, this.dbSession);
        } finally {
            this.closeSession();
        }
    }

    public <T> List<T> getAllObjects(Class<T> t, ObjectContainer dbSession) {
        List<T> l = (List<T>) dbSession.query(t);
        return new ArrayList<T>(l);
    }

    public <T> List<T> getAllObjects(T t, ObjectContainer dbSession) {
        List<T> l = (List<T>) dbSession.queryByExample(t);
        return new ArrayList<T>(l);
    }

    public <T> List<T> getAllObjects(Class<T> t) {
        try {
            openSession();
            List<T> l = (List<T>) dbSession.query(t);
            return new ArrayList<T>(l);
        } finally {
            closeSession();
        }
    }

    public <T> List<T> getAllObjects(T t) {
        try {
            openSession();
            List<T> l = (List<T>) dbSession.queryByExample(t);
            return new ArrayList<T>(l);
        } finally {
            closeSession();
        }
    }
}
