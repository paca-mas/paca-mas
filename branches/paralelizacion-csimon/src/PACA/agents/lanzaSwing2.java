package PACA.agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import PACA.util.Testigo;

public class lanzaSwing2 {
	
	private static final long serialVersionUID = 1L;
	
	//JFrame --- la ventana
	static JFrame ventana = new JFrame();
	
	//JComBox ---- Uno por cada accion
	static JComboBox cAutentica = new JComboBox();
	static JComboBox cPracticas = new JComboBox();
	static JComboBox cTests = new JComboBox();
	static JComboBox cFicheros = new JComboBox();
	
	
		
	//JPanel ---- Uno por cada accion
	static JPanel paraAutenticar = new JPanel();
	static JPanel paraPracticas = new JPanel();
	static JPanel paraTests = new JPanel();
	static JPanel paraFicheros = new JPanel();
	static JPanel paraCorregir = new JPanel();
	static JPanel paraResultados = new JPanel();
	
	//JButton ---- Uno por cada accion
	static JButton botonAutenticacion = new JButton("Autenticacion OK");
	static JButton botonPracticas = new JButton("Elige Practica");
	static JButton botonTests = new JButton("Elige Tests");
	static JButton botonFicheros = new JButton("Corrige");
	static JButton botonCorregir = new JButton("Corregir OK");
	
	//Autenticacion
	static JTextField textoUsuario = new JTextField(10);
	static JTextField textoPass = new JTextField(10);
	
	//Listas
	static List listaPract = new List();
	static List listaTests = new List();
	static List listaFichs = new List();
	
	//Salida correccion
	static JTextArea correccion = new JTextArea("",5,20);
	static JTextArea tiempoEmpleado = new JTextArea("",5,20);
	        
	
	//Contenido fichero
	static String contenido = "hola";
	
	static Runtime rt;
    static Profile p;
    static ContainerController cc;
    static AgentController agentInterfaz=null;
    static InterfazSwing2 agent=null;
            
	static void repintar(JFrame frame1, JPanel panel1){
		System.out.println("estamos repintando... ");
		frame1.getContentPane().add(panel1, BorderLayout.CENTER);
		frame1.repaint();
		
	}
	
	static class Aleatorio extends Random {
		public int nextInt(int inferior, int superior) {
			int i;
			i=nextInt();
			i=inferior+(Math.abs(i) % (superior-inferior+1));
			return(i);
		}
	}
	
	static String generaContenido(){
		
		Aleatorio rand = new Aleatorio();
		int tamano = rand.nextInt(5, 10);
		char[] contAux = new char[tamano];
		for (int i = 0; i < contAux.length; i++) {
			contAux[i]='a';
		}
		String contenido2 = String.valueOf(contAux);
		return contenido2;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
				
		//---- Montar Panel Autenticacion ----
		//paraAutenticar.add(textoUsuario);
		//paraAutenticar.add(textoPass);
		//paraAutenticar.add(botonAutenticacion);
		//--- Fin Panel Autenticacion ----
		
		//paraPracticas.add(cPracticas);
		//paraPracticas.add(botonPracticas);
		
		//--- Fin FRAME ----
		
		
		//Funcionalidad Botones
		
		
		
//		--------- Arrancamos el agente ---------------------
		Date ahora = new Date();
		long lnMilisegundos = ahora.getTime();
		System.out.println("Ahora :"+ahora.toString());
		System.out.println("Milisegundos: "+lnMilisegundos);
		
		String texto = "csimon";
		String passw = "admin";
					       
		String nombre = texto + lnMilisegundos;
		
		try {
				rt = Runtime.instance();
				p = new ProfileImpl(false);
				cc = rt.createAgentContainer(p);
				agent = new InterfazSwing2();
				agentInterfaz = cc.acceptNewAgent(nombre, agent);
				if (agentInterfaz != null) {
					agentInterfaz.start();
					
					while (!agent.isFinSetup()) {
						System.out.println("Esperando al fin... ");
					}
				} 
				else {
					System.out.println("Agente no Arrancado" + agentInterfaz.getState().getName());
				}
			} 
		catch (Exception ex) {
				ex.printStackTrace();
		}
		
		boolean autenticado = false;
		
		
		
		Testigo testigo = new Testigo();
		agent.swingAutentica(texto, passw, testigo);
		System.out.println("--------------------");
						
		while(!testigo.isRelleno()){
		}
		
		autenticado = testigo.isResultadoB();
		System.out.println("autenticado: "+autenticado);
		
		if (autenticado){
			testigo = new Testigo();
			agent.swingPideCorrector(testigo);
			while(!testigo.isRelleno()){
			}
								
			testigo = new Testigo();
			
			agent.swingPidePracticas(testigo);
			while(!testigo.isRelleno()){
			}
			
			String [] pract = (String [])testigo.getResultado();
						
			for (int i = 0; i < pract.length; i++) {
				listaPract.add(pract[i]);						
			}		
			
			//cPracticas = new JComboBox(pract);
			paraPracticas.add(listaPract);
			paraPracticas.add(botonPracticas);
			//ventana.getContentPane().add(paraPracticas, BorderLayout.NORTH);
			//ventana.validate();
			
			paraTests.add(listaTests);
			paraTests.add(botonTests);
			paraFicheros.add(listaFichs);
			paraFicheros.add(botonFicheros);
			paraCorregir.add(correccion);
			
			
			
			
			
			//--- Montar FRAME ----
			ventana.setSize(new Dimension(800, 400));		
			ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
			ventana.setLayout(new BorderLayout());
			//ventana.getContentPane().add(paraAutenticar, BorderLayout.NORTH);
			
			ventana.getContentPane().add(paraPracticas, BorderLayout.NORTH);
			ventana.getContentPane().add(paraTests, BorderLayout.WEST);
			ventana.getContentPane().add(paraFicheros, BorderLayout.CENTER);
			ventana.getContentPane().add(paraCorregir, BorderLayout.EAST);
			
			ventana.setTitle("P A C A - Interfaz");
			ventana.pack();
			ventana.setVisible(true);
			ventana.validate();
			
			
			
		}
		
		//----- FIN ARRANCAR AGENTE -----
		
		
		
		// Selecciona practicas
		botonPracticas.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String practica = (String) listaPract.getSelectedItem();
				System.out.println("salida: "+practica+"**");
				
				Testigo testigo = new Testigo();
				agent.swingPideTests(testigo, practica);
				while(!testigo.isRelleno()){
				}
				
				String [] tests = (String [])testigo.getResultado();
				
				int tamano = tests.length / 2;
				
				String[] tests2 = new String[tamano];
				for (int i = 0; i < tests2.length; i++) {
						tests2[i]=tests[i*2];
				}
				
						
				for (int i = 0; i < tests2.length; i++) {
					listaTests.add(tests2[i]);						
				}
				
				listaTests.setMultipleMode(true);
				paraTests.add(listaTests);
				paraTests.add(botonTests);
				ventana.getContentPane().add(paraTests, BorderLayout.WEST);
				ventana.validate();
			}
		} );
		
		
		//Selecciona Tests
		botonTests.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String [] testss  =  listaTests.getSelectedItems();
											
				Testigo testigo = new Testigo();
				agent.swingPideFicheros(testigo, testss);
				
				while(!testigo.isRelleno()){
				}
				
				String [] fichs = (String [])testigo.getResultado();
				
				for (int i = 0; i < fichs.length; i++) {
					listaFichs.add(fichs[i]);
				}
				listaFichs.setMultipleMode(true);
				paraFicheros.add(listaFichs);
				paraFicheros.add(botonFicheros);
				ventana.getContentPane().add(paraFicheros, BorderLayout.CENTER);
				ventana.validate();
			}
		} );
		
		//Rellenar fichero
		botonFicheros.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				String [] ficheros = listaFichs.getSelectedItems();
				Testigo testigo = new Testigo();
				String [] cont = new String[ficheros.length];
				String contenido3 = generaContenido();
				for(int i = 0; i < cont.length; i++) {
					cont[i]=contenido;
				}
				
				
				agent.swingPideCorrector(testigo);
				while(!testigo.isRelleno()){
				}
				
				System.out.println("CORRECTOR ELEGIDO: "+testigo.getResultado());
				
				testigo = new Testigo();
				agent.swingPideCorreccion(testigo, cont);
				while(!testigo.isRelleno()){
				}
				
				String salida = (String) testigo.getResultado();
								
				int posicion = salida.indexOf("terminacion_incorrecta");
				
				String textoEva = null;
				if (posicion!=-1){
					textoEva = "Practica Erronea";
				}
				else{
					textoEva = "Practica Aceptada";
				}
				
				
				correccion.setText(textoEva);
				paraCorregir.add(correccion);
				//paraCorregir.add(botonCorregir);
				ventana.getContentPane().add(paraCorregir, BorderLayout.EAST);
				ventana.validate();
				
			}
		});
		
				
	}

}
