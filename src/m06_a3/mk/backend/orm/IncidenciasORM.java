package m06_a3.mk.backend.orm;

import com.db4o.query.Predicate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import m06_a3.mk.backend.model.Empleado;
import m06_a3.mk.backend.model.Historial;
import m06_a3.mk.backend.model.Incidencia;
import m06_a3.mk.commandline.KB;
import m06_a3.mk.commandline.TestORM;

public class IncidenciasORM {

    static final String MENU = "\n1. Insertar nuevo empleado en BD"
            + "\n2. Login entrada empleado"
            + "\n3. Modificar perfil empleado "
            + "\n4. Cambiar contrase�a empleado"
            + "\n5. Borrar empleado"
            + "\n6. Obtener incidencia por 'id'"
            + "\n7. Obtener lista incidencias"
            + "\n8. Insertar nueva incidencia"
            + "\n9. Obtener incidencia para empleado"
            + "\n10. Obtener incidencias de empleado"
            + "\n-------------------------\n0. Salir\n";
    // ######### METODOS DE AYUDA

    public static List<Empleado> loadEmpleados() {
        return TestORM.dbMngr.getAllObjects(Empleado.class);
    }

    public static List<String> getEmpleadosKeys() {
        return loadEmpleados().stream().map(e -> e.getNombreusuario()).collect(Collectors.toList());
    }

    public static void execMenu() {
        int op = 0;
        Empleado e;
        do {
            System.out.println("\n\n######### MENU INCIDENCIAS-ORM #########");
            System.out.println(MENU);
            op = KB.askIntMinMax(0, 10, "Opcion: ", "La opcion introducida no existe");
            switch (op) {
                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;
                case 1:
                    e = createEmpleado();
                    insertEmpleado(e);
                    break;
                case 2:
                    String[] data = getUserCredentials();
                    e = validateEmpleado(data[0], data[1]);
                    if (e != null) {
                        System.out.println("Usuario validado correctamente!");
                        TestORM.id = e.getNombreusuario();
                        TestORM.dbMngr.save(new Historial(e, "I", new Date().toString()));

                    } else {
                        System.out.println("ERROR: Usuario o password incorrectos!");
                    }

                    break;
                case 3:
                    updateEmpleadoHelp(selectEmpleadoHelp());
                    break;
                case 4:
                    System.out.println("Esto lo puede conseguir desde la opci�n 3. :)");
                    break;
                case 5:
                    deleteEmpleado(selectEmpleadoHelp());
                    break;
                case 6:
                    int id = KB.askInt("Id de Incidencia: ", "Formato incorrecto. El ID es numerico.");
                    Incidencia inc = IncidenciasORM.getIncidencia(id);
                    if (inc == null) {
                        System.out.println("No se encuentra dicha encidencia");
                    } else {
                        System.out.println(inc.simpleToString());
                    }
                    break;
                case 7:
                    List<Incidencia> lI = IncidenciasORM.loadIncidencias();
                    for (Incidencia i : lI) {
                        System.out.println(i.toString());
                    }
                    break;
                case 8:
                    Incidencia i = IncidenciasORM.createIncidencia();
                    long idInc = IncidenciasORM.insertIncidencia(i);
                    if(i.getTipo().equals("Urgente") && idInc != -1) {
			TestORM.dbMngr.save(new Historial(i.getEmpleadoByOrigen(), "U", new Date().toString()));	
                    }
                    

                    break;
                case 9:
                    e = IncidenciasORM.selectEmpleadoHelp();
                    lI = IncidenciasORM.getIncidencias4Empleado(e);

                    if (lI == null || lI.size() == 0) {
                        System.out.println("No se han encontrado incidencia con destino empleado seleccionado!");
                        break;
                    }
                    for (Incidencia x : lI) {
                        System.out.println(x.simpleToString());
                    }
                    
                    if(TestORM.id != null){
                        List<Empleado> executer = TestORM.dbMngr.findByPredicate(new Predicate(){
                            public boolean match(Empleado e){
                                return e.getNombreusuario().equals(TestORM.id);
                            }
                        });
                        
                        if(executer != null && executer.size() > 0){
                            TestORM.dbMngr.save(new Historial(executer.get(0), "C", new Date().toString()));
                        }else{
                            System.err.println("No se ha podido generar entrada en el historial para esta consulta de incidencia...");
                            System.err.println("No estas autenticado o el usuario actual ya no existe en la base de datos...");
                        }
                    }

                    break;
                case 10:
                    e = IncidenciasORM.selectEmpleadoHelp();
                    lI = IncidenciasORM.getIncidenciasFromEmpleado(e);
                    
                    if (lI == null || lI.size() == 0) {
                        System.out.println("No se han encontrado incidencia con origen empleado seleccionado!");
                        break;
                    }
                    for (Incidencia x : lI) {
                        System.out.println(x.simpleToString());
                    }
                    break;
            }
        } while (op != 0);
    }

    // AYUDAS 2.a)
    private static Empleado createEmpleado() {
        List<String> empleados = IncidenciasORM.getEmpleadosKeys();
        String nombreUsuario = "";
        char[] passwd;
        String nombreCompleto;
        String telefono;
        boolean match = true;

        // Get username
        do {
            final String nu = KB.askString("Nombre Usuario: ", false);
            if (!empleados.stream().anyMatch(e -> e.equals(nu))) {
                match = false;
                nombreUsuario = nu;
            }
        } while (match);

        // Get password
        do {
            passwd = KB.askPassword("Password: ", false);
            if (passwd == null || passwd.length <= 0) {
                System.out.println("No se admite un password vacio!");
            }
        } while (passwd == null || passwd.length <= 0);

        // Get nombre completo
        nombreCompleto = KB.askString("Nombre compleot: ", false);
        telefono = KB.askString("Telefono: ", false);

        return new Empleado(nombreUsuario, new String(passwd), nombreCompleto, telefono);
    }

    // AYUDAS 2.b)
    public static String[] getUserCredentials() {
        String username = null;
        char[] passwd;

        do {
            username = KB.askString("Usuario: ", false);
        } while (username == null);

        do {
            passwd = KB.askPassword("Password: ", false);
        } while (passwd == null || passwd.length <= 0);

        return new String[]{username, new String(passwd)};

    }

    // AYUDAS 2.c)
    /**
     * 0 = Guardado | -1 = Cancelado | 1 = Error
     *
     * @return
     */
    public static Empleado selectEmpleadoHelp() {
        List<Empleado> emps = loadEmpleados();
        int choice = -1;
        do {
            System.out.println("----- EMPLEADOS -----");
            int i = 0;
            for (Empleado e : emps) {
                System.out.println(i + ". " + e.toString());
                i++;
            }
            choice = KB.askIntMinMax(0, emps.size() - 1, "Empleado: ", "No se encuentra el empleado indicado");
        } while (choice < 0 || choice > emps.size() - 1);

        Empleado e = emps.get(choice);
        return e;
    }

    private static void updateEmpleadoHelp(Empleado e) {
        String options = "1. Password\n2. Nombre Completo\n3. Telefono\n------------\n0. Guardar\n-1. Salir";
        int op = 0;
        String data;
        do {
            System.out.println("--- MODIFICAR EMPLEADO ---");
            System.out.println(e.toString());
            System.out.println(options);
            op = KB.askIntMinMax(-1, 3, "Modificar: ", "Opci�n no valida");
            switch (op) {
                case 0:
                    break;
                case 1:
                    data = new String(KB.askPassword("Nuevo password: ", false));
                    e.setPassword(data);
                    break;
                case 2:
                    data = KB.askString("Nuevo nombre completo: ", false);
                    e.setNombrecompleto(data);
                    break;
                case 3:
                    data = KB.askString("Nuevo telefono: ", false);
                    e.setTelefono(data);
                    break;
            }
        } while (op != 0 && op != -1);

        if (op == -1) {
            System.out.println("Cambios no guardados");
            return;
        }
        System.out.println("Guardando cambios...");
        updateEmpleado(e);
    }

    // AYUDA 3.c)
    private static Incidencia createIncidencia() {

        Empleado eOrigen;
        do {
            System.out.println("Autenticate para generar la incidencia: ");
            String[] credentials = IncidenciasORM.getUserCredentials();
            eOrigen = IncidenciasORM.validateEmpleado(credentials[0], credentials[1]);
            System.out.println(eOrigen == null ? "Usuario o contraseña incorrectos." : "Validado correctamente");
        } while (eOrigen == null);

        System.out.println("Selecciona empleado DESTINO: ");
        Empleado eDestino = IncidenciasORM.selectEmpleadoHelp();

        String date = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String descr = KB.askString("Añade una descripcion: ", true);
        int tipoN = 0;
        do {
            tipoN = KB.askIntMinMax(1, 2, "Tipo de incidencia(1. Normal | 2. Urgente): ", "Escoja 1 o 2");
        } while (tipoN != 1 && tipoN != 2);
        String tipo = (tipoN == 1) ? "Normal" : "Urgente";
        return new Incidencia(eOrigen, eDestino, date, descr, tipo);
    }

    // ######### METODOS PARA APARTADO 2 
    // a)
    public static void insertEmpleado(Empleado emp) {
        TestORM.dbMngr.save(emp);
    }

    // b)
    public static Empleado validateEmpleado(String user, String password) {
        List<Empleado> l = TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Empleado e) {
                return e.getNombreusuario().equals(user) && e.getPassword().equals(password);
            }
        });

        return l != null && l.size() > 0 ? l.get(0) : null;
    }

    // c) d)
    public static void updateEmpleado(Empleado emp) {
        TestORM.dbMngr.update(emp);
    }

    // e)
    public static void deleteEmpleado(Empleado emp) {
        TestORM.dbMngr.remove(emp);
    }

    // FIN METODOS APARTADO 2
    // ########## METODOS APARTADO 3
    // a)
    public static Incidencia getIncidencia(int id) {
        return TestORM.dbMngr.findById(id, Incidencia.class);
    }

    // b)
    public static List<Incidencia> loadIncidencias() {
        return TestORM.dbMngr.getAllObjects(Incidencia.class);
    }

    // c)
    public static long insertIncidencia(Incidencia inc) {
        return TestORM.dbMngr.save(inc);
    }

    // d)
    public static List<Incidencia> getIncidencias4Empleado(Empleado emp) {
        return TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Incidencia i) {
                return i.getEmpleadoByDestino().equals(emp);
            }
        });
    }

    // e)
    public static List<Incidencia> getIncidenciasFromEmpleado(Empleado emp) {
        return TestORM.dbMngr.findByPredicate(new Predicate() {
            public boolean match(Incidencia d) {
                return d.getEmpleadoByOrigen().equals(emp);
            }
        });
    }

}
