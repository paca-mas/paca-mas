package PACA.agents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
	
	//JPanel ---- Uno por cada accion
	static JPanel paraPracticas = new JPanel();
	static JPanel paraTests = new JPanel();
	static JPanel paraFicheros = new JPanel();
	static JPanel paraCorregir = new JPanel();
	
	//JButton ---- Uno por cada accion
	static JButton botonPracticas = new JButton("Elige Practica");
	static JButton botonTests = new JButton("Elige Tests");
	static JButton botonFicheros = new JButton("Corrige");
	
	//Autenticacion
	static JTextField textoUsuario = new JTextField(10);
	static JTextField textoPass = new JTextField(10);
	
	//Listas
	static List listaPract = new List();
	static List listaTests = new List();
	static List listaFichs = new List();
	static List listaCorrectores = new List();
	
	//Salida correccion
	static JTextArea correccion = new JTextArea();
	static JTextArea tiempoEmpleado = new JTextArea("",1,10);
	static JTextArea correctorElegido = new JTextArea("",1,25);
	
	static boolean primeraCorreccion = true;
	static String [] cont = new String[0];
	static String [] cont2 = new String[0];
	        
	//Variables para crear el Agente Interfaz
	static Runtime rt;
    static Profile p;
    static ContainerController cc;
    static AgentController agentInterfaz=null;
    static InterfazSwing2 agent=null;
    
    static String politica = "minimos";
    //Fin Variables Agente Interfaz
            
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
	
	
	//Metodo para generar contenido aleatorio entre 5K y 20K
	static String generaContenido(){
		
		Aleatorio rand = new Aleatorio();
		int tamano = rand.nextInt(5000, 20000);
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
		
		//Rellenamos los paneles
		paraPracticas.add(listaPract);
		paraPracticas.add(botonPracticas);
		
		listaTests.add("SELECCIONA LOS TESTS");
		paraTests.add(listaTests);
		paraTests.add(botonTests);
		listaFichs.add("SELECCIONA LOS FICHEROS");
		paraFicheros.add(listaFichs);
		paraFicheros.add(botonFicheros);
		listaCorrectores.add("      CORRECTOR Y TIEMPO NECESITADO     ");
		paraCorregir.add(listaCorrectores);
		
	
		Date ahora = new Date();
		long lnMilisegundos = ahora.getTime();
		System.out.println("Ahora :"+ahora.toString());
		System.out.println("Milisegundos: "+lnMilisegundos);
		
		String texto = "csimon";
		String passw = "admin";
					       
		String nombre = texto + lnMilisegundos;
		
		//Montamos el JFrame
		ventana.setSize(new Dimension(1200, 500));		
		ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
		
					
		ventana.setLayout(new BorderLayout());
		ventana.getContentPane().add(paraPracticas, BorderLayout.NORTH);
		ventana.getContentPane().add(paraTests, BorderLayout.WEST);
		ventana.getContentPane().add(paraFicheros, BorderLayout.CENTER);
		ventana.getContentPane().add(paraCorregir, BorderLayout.SOUTH);
		
		ventana.setTitle("P A C A - Interfaz -- " + nombre);
		ventana.pack();
		ventana.setVisible(true);
		ventana.validate();
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//--------- Arrancamos el agente ---------------------
		
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
			agent.swingPideCorrector(testigo, politica);
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
			
			paraPracticas.add(listaPract);
			paraPracticas.add(botonPracticas);
			ventana.getContentPane().add(paraPracticas, BorderLayout.NORTH);
			ventana.validate();
					
			
		}
		
		//----- FIN ARRANCAR AGENTE -----
		
		
		
		// Selecciona practicas
		botonPracticas.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				String practica = (String) listaPract.getSelectedItem();
				if (practica!=null){
					//System.out.println("salida: "+practica+"**");

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
			}
		} );
		
		
		//Selecciona Tests
		botonTests.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				String [] testss = (String []) listaTests.getSelectedItems();

				if (testss!=null){
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
			}
		} );
		
		//Rellenar fichero
		botonFicheros.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				String [] ficheros = listaFichs.getSelectedItems();
				
				Date comienzo = new Date();
				Long enMilisegundos = comienzo.getTime();
				
				Testigo testigo = new Testigo();
				
				cont = new String[ficheros.length];
				
				if (primeraCorreccion){
					cont2 = new String[ficheros.length];
					
					for(int i = 0; i < cont.length; i++) {
						String contenido3 = generaContenido();
						cont[i]=contenido3;
						//System.out.println("Tamano del fichero: "+cont[i].length());
						cont2[i]=contenido3;
						//System.out.println("Tamano del fichero de backup: "+cont2[i].length());
						primeraCorreccion = false;
					}
				}
				else{
					cont = cont2;
					//for (int i = 0; i < cont.length; i++) {
						//System.out.println("Tamano del fichero sin modificar: "+cont[i].length());
					//}
				}
				
					
				agent.swingPideCorrector(testigo, politica);
				while(!testigo.isRelleno()){
				}
				
				AID nombre = (AID) testigo.getResultado();
				String nombreCorto = nombre.getName();
				
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
				
				Date finalizado = new Date();
				Long enMilisegundos2 = finalizado.getTime();
				
				Long duracion = enMilisegundos2 - enMilisegundos;
				String duraAux = duracion.toString();
				
				String resultado = nombreCorto + "   " +duraAux;
				agent.almacenCorrec.put(nombre, duracion);
				
				listaCorrectores.add(resultado);
				paraCorregir.add(listaCorrectores);
				ventana.getContentPane().add(paraCorregir, BorderLayout.SOUTH);
				ventana.validate();
				
			}
		});
		
				
	}

}
