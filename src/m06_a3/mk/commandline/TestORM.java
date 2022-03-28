package m06_a3.mk.commandline;


import m06_a3.mk.backend.config.DB4OConfig;
import m06_a3.mk.backend.model.Empleado;
import m06_a3.mk.backend.model.Historial;
import m06_a3.mk.backend.model.Incidencia;
import m06_a3.mk.backend.orm.HistorialORM;
import m06_a3.mk.backend.orm.IncidenciasORM;
import m06_a3.mk.backend.repos.DB4OManager;



public class TestORM {

	public static String id = null;
	private static final String DB4OFile = "m06act3_db40";
        public final static DB4OManager dbMngr = new DB4OManager(DB4OFile, Empleado.class, Incidencia.class, Historial.class);
        
	public static void main(String[] args) {
            DB4OConfig conf = new DB4OConfig(dbMngr);
            conf.addTestData();
            dbMngr.setSupressErrors(false);
            dbMngr.setSysErrTraces(true);
            
            int op = 0;
            do {
                System.out.println("############# ACTIVIDAD 3 DB4O ############  (Logged as: " + id + ")");
                System.out.println("1. Acceder IncidenciasORM\n2. Acceder HistorialORM\n0. Salir");
                op = KB.askIntMinMax(0, 2, "Opcion: ", "Opcion inexistente!");
                if (op == 1) {
                    IncidenciasORM.execMenu();
                }
                if (op == 2) {
                    HistorialORM.execMenu();
                }
            } while (op != 0);
            System.out.println("Hasta la proxima");

       
	}
	
}
