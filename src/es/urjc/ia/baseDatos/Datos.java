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
public class Datos {
    private Statement stat;
    private Connection conn;

    public Statement getStat(){
        return stat;
    }

    public Connection getConnection(){
        return conn;
    }
    public Datos() throws ClassNotFoundException, SQLException{

        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:datos.db"); //Esto seria el fichero donde guardar los datos
        stat = conn.createStatement();

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

        

    }

   
}
