package m06_a3.mk.backend.orm;

import com.db4o.query.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import m06_a3.mk.backend.model.Empleado;
import m06_a3.mk.backend.model.Historial;
import m06_a3.mk.commandline.KB;
import m06_a3.mk.commandline.TestORM;

public class HistorialORM {

    static final String LOGIN = "l";
    static final String CHECK = "C";
    static final String URGENT = "U";

    static final String MENU = "\n1. Insertar eventos"
            + "\n2. Obtener fecha ultimo inicio sesion de empleado"
            + "\n3. Obtener ranking de empleados por incidencias urgentes generadas"
            + "\n4. Obtener la posición dentro del ranking de incidencia urgentes para un empleado concreto"
            + "\n-------------------------\n0. Salir\n";

    public static List<Historial> loadHistoriales() {
        return TestORM.dbMngr.getAllObjects(Historial.class);
    }

    public static void execMenu() {
        int op = 0;
        Map<Empleado, Integer> m;
        Empleado e;
        do {
            System.out.println("\n\n######### MENU HISTORIAL-ORM #########");
            System.out.println(MENU);
            op = KB.askIntMinMax(0, 4, "Opcion: ", "La opcion introducida no existe");

            switch (op) {
                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;
                case 1:
                    System.out.println("La unica forma de generar una entrada de evento es generando un evento :P");
                    System.out.println("Login -> IncidenciasORM, Op 2\nConsulta incidencia -> IncidenciasORM, Op 9\nCreacion Incidencia Urgente -> IncidenciasORM, Op 8");
                    break;
                case 2:
                    Empleado em = IncidenciasORM.selectEmpleadoHelp();
                    String date = getLastLogInDateTimeForEmpleado(em);
                    if (date != null) {
                        System.out.println("Ultimo log in: " + date);
                    } else {
                        System.out.println("Un error inesperado a ocurrido. No se encuentra la ultima fecha.");
                    }
                    break;
                // El case 3 y case 4 no son del todo fieles el uno al otro si los empleados tienen la misma puntuacion de incidencias urgentes creadas. Aquellos con la
                // misma puntuacion puede intercambiarse la posicion del ranking de "case 4" comparado con el ranking visible desde "case 3".
                case 3:
                    m = HistorialORM.getEmpleadosByCreatedIncidencias();
                    m.entrySet().stream().sorted(Map.Entry.<Empleado, Integer>comparingByValue().reversed()).forEach((emp) -> {
                        System.out.println("Numero: " + emp.getValue() + " | Emplado: " + ((Empleado) emp.getKey()).getNombreusuario());
                    });
                    break;
                case 4:
                    m = getEmpleadosByCreatedIncidencias();
                    List<Entry<Empleado, Integer>> l = m.entrySet().stream().sorted(Map.Entry.<Empleado, Integer>comparingByValue().reversed()).collect(Collectors.toList());
                    e = IncidenciasORM.selectEmpleadoHelp();
                    int i = 0;
                    while (i < l.size() && !l.get(i).getKey().getNombreusuario().equals(e.getNombreusuario())) {
                        i++;
                    }
                    System.out.println(String.format("Empleado: %s\nIncidencias creadas: %d\nRanking: %d", e.getNombreusuario(), l.get(i).getValue(), i == 0 ? 1 : i));
                    break;
            }
        } while (op != 0);
    }

    // a)
    public static Long insertHistorial(String tipo, String user) {
        //TestORM.dbMngr.save();
        return Long.valueOf(0);
    }

    public static Empleado getEmpleadoById(String username) {
        List<Empleado> l = TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Empleado e) {
                return e.getNombreusuario().equals(username);
            }
        });

        return l != null && l.size() > 0 ? l.get(0) : null;
    }
    // b)

    public static String getLastLogInDateTimeForEmpleado(Empleado e) {
        List<Historial> lH = TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Historial h) {
                return h.getEmpleado().equals(e);
            }
        });

        String date = null;
        if (lH != null && lH.size() > 0) {
            lH.stream().sorted();
            date = lH.get(0).getFechahora();
        }

        return date;
    }

    // c)
    //SELECT count(h.idevento) as "num", e.* FROM historial as h join empleado as e on e.nombreusuario = h.empleado WHERE h.tipo = 'U' GROUP BY e.nombreusuario ORDER BY num DESC; 
    public static Map<Empleado, Integer> getEmpleadosByCreatedIncidencias() {
        List<Historial> lH = TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Historial h) {
                return h.getTipo().equals("U");
            }
        });
        Map<Empleado, Integer> m = new HashMap<Empleado, Integer>();

        for (Historial h : lH) {
            if (m.containsKey(h.getEmpleado())) {
                int i = m.get(h.getEmpleado());
                m.replace(h.getEmpleado(), Integer.valueOf(++i));
                continue;
            }

            m.put(h.getEmpleado(), 1);
        }

        return m;
    }
    // d)

}
