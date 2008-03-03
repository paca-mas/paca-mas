package PACA.agents;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class lanzaSwing2 {
	
	private static final long serialVersionUID = 1L;
	
	//JFrame --- la ventana
	static JFrame ventana = new JFrame();
	
	//JComBox ---- Uno por cada accion
	static JComboBox cAutentica = new JComboBox();
	static JComboBox cPracticas = null;
	static JComboBox cTests = null;
	static JComboBox cFicheros = null;
	
	//JPanel ---- Uno por cada accion
	static JPanel paraAutenticar = new JPanel();
	static JPanel paraPracticas = new JPanel();
	static JPanel paraTests = new JPanel();
	static JPanel paraFicheros = new JPanel();
	static JPanel paraCorregir = new JPanel();
	
	//JButton ---- Uno por cada accion
	static JButton botonAutenticacion = new JButton("Autenticacion OK");
	static JButton botonPracticas = new JButton("Practicas OK");
	static JButton botonTests = new JButton("Tests OK");
	static JButton botonFicheros = new JButton("Ficheros OK");
	static JButton botonCorregir = new JButton("Corregir OK");
	
	//Autenticacion
	static JTextField textoUsuario = new JTextField();
	static JPasswordField textoPass = new JPasswordField();
	
	//Practicas
	static String[] practicas = {"Practica1", "Practica2", "Practica3", "Practica4"};
	static String[] practica1 = {"Test1", "Test2", "Test3"};
	static String[] practica2 = {"TestA", "TestB", "TestC"};
	static String[] practica3 = {"Test00", "Test01", "Test10"};
	
	static String[] Test1 = {"Fichero1", "Fichero2", "Fichero3", "Fichero4"};
	static String[] Test2 = {"FicheroA", "FicheroB", "FicheroC", "FicheroD"};
	static String[] Test3 = {"Fichero00", "Fichero01", "Fichero10", "Fichero11"};
	
	//Inicializacion
	//----------- Paneles ------------
		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		cPracticas = new JComboBox(practicas);
		
		ventana.setTitle("P A C A - Interfaz");
		ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
		ventana.getContentPane().setLayout(new GridLayout(10,10));
		
		
		//---- Montar Paneles ----
		paraAutenticar.add(textoUsuario);
		paraAutenticar.add(textoPass);
		paraAutenticar.add(botonAutenticacion);
		
		paraPracticas.add(cPracticas);
		paraPracticas.add(botonPracticas);
		
		//paraTests.add(cTests);
		//paraTests.add(botonTests);
		
		//paraFicheros.add(cFicheros);
		//paraFicheros.add(botonFicheros);
		
		paraCorregir.add(botonCorregir);
		
		//--- Fin Montar Paneles ----
		
		//--- Montar FRAME ----
		ventana.getContentPane().add(paraAutenticar);
		ventana.getContentPane().add(paraPracticas);
		ventana.getContentPane().add(paraTests);
		ventana.getContentPane().add(paraFicheros);
		ventana.getContentPane().add(paraCorregir);
		ventana.pack();
		ventana.setVisible(true);
		ventana.setResizable(true);
		//--- Fin FRAME ----
		
		
		//Funcionalidad Botones
		
		// Selecciona practicas
		botonPracticas.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String salida = (String) cPracticas.getSelectedItem();
				System.out.println("salida: "+salida+"**");
								
				if (salida.equals("Practica1")){
					cTests = new JComboBox(practica1);
					
				}
				else{
					if (salida.equals("Practica2")){
						cTests = new JComboBox(practica2);
					}
					else {
						cTests = new JComboBox(practica3);
					}
				}
				
				paraTests.add(cTests);
				paraTests.add(botonTests);
			}
		} );
		
		
		//Selecciona Tests
		botonTests.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String salida = (String) cTests.getSelectedItem();
				System.out.println("salida: "+salida+"**");
								
				if (salida.equals("Practica1")){
					cFicheros = new JComboBox(Test1);
					
				}
				else{
					if (salida.equals("Practica2")){
						cFicheros = new JComboBox(Test2);
					}
					else {
						cFicheros = new JComboBox(Test3);
					}
				}
				
					paraFicheros.add(cFicheros);
					paraFicheros.add(botonFicheros);
			}
		} );
		
	}

}
