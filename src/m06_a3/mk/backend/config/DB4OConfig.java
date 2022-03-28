/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m06_a3.mk.backend.config;

import java.util.Date;
import java.util.List;
import m06_a3.mk.backend.model.Empleado;
import m06_a3.mk.backend.model.Historial;
import m06_a3.mk.backend.model.Incidencia;
import m06_a3.mk.backend.repos.DB4OManager;

/**
 *
 * @author Brainight
 */
public class DB4OConfig {

    DB4OManager dbMngr;

    public DB4OConfig(DB4OManager dbMngr) {
        this.dbMngr = dbMngr;
    }

    public boolean addTestData() {
        Empleado e0 = new Empleado("BigBro", "101", "Big Brother", "4815162342");
        Empleado e1 = new Empleado("Goldstein", "1984", "Emmanuel Goldstein", "101101101");
        Empleado e2 = new Empleado("WSmith", "Love", "Winston Smith", "11235813");
        Empleado e3 = new Empleado("Manolo", "Manolo", "Manolo Kabezabolo", "666666666");

        this.dbMngr.save(List.of(e0, e1, e2, e3));

        Incidencia i0 = new Incidencia(e2, e1, new Date().toString(), "La que nos ha caido...", "Urgente");
        Incidencia i1 = new Incidencia(e3, e0, new Date().toString(), "Otro dia la misma m****", "Normal");
        Incidencia i2 = new Incidencia(e1, e3, new Date().toString(), "Incidencia urgente", "Urgente");
        Incidencia i3 = new Incidencia(e1, e2, new Date().toString(), "Otra vez el mismo tio..", "Normal");

        this.dbMngr.save(List.of(i0, i1, i2, i3));

        Historial h0 = new Historial(e0, "I", new Date().toString());
        Historial h1 = new Historial(e1, "I", new Date().toString());
        Historial h2 = new Historial(e2, "I", new Date().toString());
        Historial h3 = new Historial(e3, "I", new Date().toString());
        Historial h4 = new Historial(e0, "I", new Date(System.currentTimeMillis() + 100000).toString());
        Historial h5 = new Historial(e1, "I", new Date(System.currentTimeMillis() + 200000).toString());
        Historial h6 = new Historial(e2, "I", new Date(System.currentTimeMillis() + 300000).toString());
        Historial h7 = new Historial(e3, "I", new Date(System.currentTimeMillis() + 400000).toString());
        Historial h8 = new Historial(e2, "U", new Date().toString());
        Historial h9 = new Historial(e0, "U", new Date().toString());
        Historial h10 = new Historial(e2, "U", new Date().toString());
        Historial h11 = new Historial(e3, "U", new Date().toString());
        Historial h12 = new Historial(e2, "U", new Date().toString());
        Historial h13 = new Historial(e1, "U", new Date().toString());
        Historial h14 = new Historial(e0, "U", new Date().toString());
        Historial h15 = new Historial(e0, "U", new Date().toString());
        Historial h16 = new Historial(e0, "U", new Date().toString());
        Historial h17 = new Historial(e0, "C", new Date().toString());

        this.dbMngr.save(List.of(h0, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, h15, h16, h17));
        return true;
    }

    public void deleteAll() {

    }
}
