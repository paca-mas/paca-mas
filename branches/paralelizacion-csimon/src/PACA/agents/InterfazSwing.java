package PACA.agents;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.java.swing.*; 

public class InterfazSwing extends JFrame {
  
  public static void main( String argv[] ) {
    new InterfazSwing();
  }
  
  InterfazSwing() {
    JLabel hola = new JLabel( "Es una prueba de Interfaz Swing!" );

    getContentPane().add( hola,"Center" );
    setSize( 200,100);
    setVisible( true );
  }
}