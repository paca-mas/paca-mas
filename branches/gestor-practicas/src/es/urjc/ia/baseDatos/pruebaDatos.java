/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.baseDatos;

import java.sql.*;

/**
 *
 * @author alvaro
 */
public class pruebaDatos {

    public static void main(String[] args) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:datos.db"); //Esto seria el fichero donde guardar los datos
        Statement stat = conn.createStatement();

        /*stat.executeUpdate("drop table Practica;");
        stat.executeUpdate("drop table Test;");
        stat.executeUpdate("drop table Caso;");
        stat.executeUpdate("drop table FicherosPropios;");
        stat.executeUpdate("drop table FicherosAlumno;");
        stat.executeUpdate("drop table FicherosIN;");
        stat.executeUpdate("drop table FicherosOUT;");*/

        stat.executeUpdate("create table Practica (id VARCHAR2(20) not null," +
                " descripcion VARCHAR2(25) not null," +
                " fechaEntrega VARCHAR2(15) not null, " +
                " primary key (id));");

        stat.executeUpdate(
                "create table Test (id VARCHAR2(20) not null," +
                "id_practica VARCHAR2(20) not null, " +
                "descripcion VARCHAR2(25) not null, " +
                "primary key (id, id_practica)," +
                "foreign key (id_practica) references Practica);");

        stat.executeUpdate(
                "create table Caso (id VARCHAR2(20) not null," +
                "id_test VARCHAR2(20) not null, " +
                "id_practica VARCHAR2(20) not null," +
                "primary key (id, id_test, id_practica)," +
                "foreign key (id_test, id_practica) references Test) ;");

        stat.executeUpdate(
                "create table FicherosPropios (id VARCHAR2(20) not null," +
                "id_test VARCHAR2(20) not null, " +
                "id_practica VARCHAR2(20) not null," +
                "contenido VARCHAR2," +
                "codigo VARCHAR2 not null," +
                "primary key (id, id_test, id_practica)," +
                "foreign key (id_test, id_practica) references Test) ;");

        stat.executeUpdate(
                "create table FicherosAlumno (id VARCHAR2(20) not null," +
                "id_test VARCHAR2(20) not null, " +
                "id_practica VARCHAR2(20) not null," +
                "contenido VARCHAR2," +
                "codigo VARCHAR2," +
                "primary key (id, id_test, id_practica)," +
                "foreign key (id_test, id_practica) references Test) ;");

        stat.executeUpdate(
                "create table FicherosOUT (id VARCHAR2(20) not null," +
                "id_caso VARCHAR2(20) not null,   " +
                "id_test VARCHAR2(20) not null, " +
                "id_practica VARCHAR2(20) not null," +
                "contenido VARCHAR2," +
                "primary key (id, id_caso, id_test, id_practica)," +
                "foreign key (id_caso, id_test, id_practica) references Caso) ;");

        stat.executeUpdate(
                "create table FicherosIN (id VARCHAR2(20) not null," +
                "id_caso VARCHAR2(20) not null,   " +
                "id_test VARCHAR2(20) not null, " +
                "id_practica VARCHAR2(20) not null," +
                "contenido VARCHAR2," +
                "primary key (id, id_caso, id_test, id_practica)," +
                "foreign key (id_caso, id_test, id_practica) references Caso) ;");

        stat.executeUpdate(
                "insert into Practica values ('Practica1', 'Practica1A: Arboles', '06/08/09');");

        stat.executeUpdate(
                "insert into Test values('Test01','Practica1', 'Test Arbol');");
        stat.executeUpdate(
                "insert into Test values('Test02','Practica1', 'Test ArbolIO');");

        stat.executeUpdate(
                "insert into Caso values('Caso01', 'Test01', 'Practica1');");
        stat.executeUpdate(
                "insert into Caso values('Caso02', 'Test01', 'Practica1');");
        stat.executeUpdate(
                "insert into Caso values('Caso01', 'Test02', 'Practica1');");
        stat.executeUpdate(
                "insert into Caso values('Caso02', 'Test02', 'Practica1');");

        stat.executeUpdate(
                "insert into FicherosPropios values('test_arbol.adb', 'Test01', 'Practica1', '', 'SeriaCodigo');");
        stat.executeUpdate(
                "insert into FicherosPropios values('arbol.ads', 'Test01', 'Practica2', '', 'SeriaCodigo');");
        stat.executeUpdate(
                "insert into FicherosPropios values('test_arbolB.adb', 'Test02', 'Practica1', '', 'SeriaCodigo');");

        stat.executeUpdate(
                "insert into FicherosAlumno values('arbol.adb', 'Test01', 'Practica1', '', '');");
        stat.executeUpdate(
                "insert into FicherosAlumno values('arbol.ads', 'Test01', 'Practica1', '', '');");
        stat.executeUpdate(
                "insert into FicherosAlumno values('arbol-io.adb', 'Test02', 'Practica1', '', '');");

        stat.executeUpdate(
                "insert into FicherosIN values('entrada1.in','Caso01','Test01', 'Practica1', '()');");
        stat.executeUpdate(
                "insert into FicherosIN values('entrada1.in', 'Caso02','Test01','Practica1','(())');");
        stat.executeUpdate(
                "insert into FicherosIN values('entrada2.in','Caso01','Test01', 'Practica1', '()');");
        stat.executeUpdate(
                "insert into FicherosIN values('entrada2.in', 'Caso01','Test02','Practica1','((a))');");
        stat.executeUpdate(
                "insert into FicherosIN values('entrada1.in','Caso02','Test02', 'Practica1', '(b)');");
        stat.executeUpdate(
                "insert into FicherosIN values('entrada2.in', 'Caso02','Test02','Practica1','(())');");

        stat.executeUpdate(
                "insert into FicherosOUT values('salida.out','Caso01','Test01', 'Practica1', '(a)');");
        stat.executeUpdate(
                "insert into FicherosOUT values('salida.out', 'Caso02','Test01','Practica1','(a(b))');");
        stat.executeUpdate(
                "insert into FicherosOUT values('salida.out','Caso01','Test02', 'Practica1', '(c)');");
        stat.executeUpdate(
                "insert into FicherosOUT values('salida.out','Caso02','Test02', 'Practica1', '(bc)');");



        conn.close();
    }
}
