package PACA.agents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

import PACA.agents.lanzaSwing2.Aleatorio;
import PACA.util.Resultado;
import PACA.util.Testigo;

//public class SimulaAgentes extends Thread{
public class SimulaAgentes implements Runnable{
	
	private class Semaphore {
		private int count;

		public Semaphore(int n) {
			this.count=n;
		}

		public synchronized void WAIT(){
			while(count==0){
				try{wait();
			}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			count--;
		}
		
		public synchronized void SIGNAL(){
			count++;
			notify();
		}
	} 
//public class SimulaAgentes{
	private class Aleatorio extends Random {
		public int nextInt(int inferior, int superior) {
			int i;
			i=nextInt();
			i=inferior+(Math.abs(i) % (superior-inferior+1));
			return(i);
		}
	}
	
	//Metodo para generar contenido aleatorio entre 5K y 20K
	private String generaContenido(){
		
		Aleatorio rand = new Aleatorio();
		int tamano = rand.nextInt(5000, 20000);
		char[] contAux = new char[tamano];
		for (int i = 0; i < contAux.length; i++) {
			contAux[i]='a';
		}
		String contenido2 = String.valueOf(contAux);
		return contenido2;
	}
	
	private String generaContenido2(int tam){
		
		char[] contAux = new char[tam];
		for (int i = 0; i < contAux.length; i++) {
			contAux[i]='a';
		}
		String contenido2 = String.valueOf(contAux);
		//System.out.println(contenido2);
		return contenido2;
	}
	
	private synchronized void EscribeFichero(String sFicher, String duraAux,
			String nombre, String nombreCorto, String practica, String politica,
			Integer numTests,String inicio, String terminacion, int tamanoF) {
		
		String dir1 = System.getProperty("user.dir");
		String separador = System.getProperty("file.separator");
		String rutaCompleta = dir1 + separador + sFicher;
		
		
		if (debug){
			System.out.println("DIR: "+dir1);
			System.out.println("SEPARADOR: "+separador);
			System.out.println("RUTA COMPLETA: "+rutaCompleta);
		}
		
		
		FileOutputStream fichero;
		try {
			mutex.WAIT();
			fichero = new FileOutputStream(System.getProperty("user.dir")+System.getProperty("file.separator")
					+sFicher,true);
			PrintWriter pw = new PrintWriter(fichero);
			pw.print(nombre);
			pw.print(";");
			pw.print(nombreCorto);
			pw.print(";");
			pw.print(duraAux);
			pw.print(";");
			pw.print(practica);
			pw.print(";");
			pw.print(numTests);
			pw.print(";");
			pw.print(politica);
			pw.print(";");
			pw.print(inicio);
			pw.print(";");
			pw.print(terminacion);
			pw.print(";");
			pw.print(tamanoF);
			pw.print(";");
			pw.println();
			pw.close();
			mutex.SIGNAL();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			mutex.WAIT();
			//FileWriter ficheroA = new FileWriter(fichero,true);
			PrintWriter pw = new PrintWriter(fichero);
			pw.print(nombre);
			pw.print(";");
			pw.print(nombreCorto);
			pw.print(";");
			pw.print(duraAux);
			pw.print(";");
			pw.print(practica);
			pw.print(";");
			pw.print(numTests);
			pw.print(";");
			pw.print(politica);
			pw.print(";");
			pw.print(inicio);
			pw.print(";");
			pw.print(terminacion);
			pw.print(";");
			pw.print(tamanoF);
			pw.print(";");
			pw.println();
			pw.close();
			mutex.SIGNAL();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	private void GeneraRetardo(int ret){
		try {
			Thread.sleep(ret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//	Variables para crear el Agente Interfaz
	ContainerController cc;
	AgentController agentInterfaz=null;
	InterfazSwing2 agent = null;
	//lanzador todosJuntos;
	private Semaphore mutex = new Semaphore(1);
    //Fin Variables Agente Interfaz
	
	private boolean debug = false;
	
	
	
	
	int numeroThread;
	String politica;
	int numCorrecionesPedidas;
	int practica_;
	int tests_;
	int tamano_;
	
		
	public SimulaAgentes(ContainerController cc, int num, String politica,
						 int correcionesPedidas, int practica_1, int tests_1, int tamano_1){
		super();
		this.cc = cc;
		this.numeroThread = num;
		this.politica = politica;
		this.numCorrecionesPedidas = correcionesPedidas;
		this.practica_ = practica_1;
		this.tests_ = tests_1;
		this.tamano_ = tamano_1;
		
	}
		
	
	//public static void main(String[] args) {
	public void run(){
		
		Date ahora = new Date();
		long lnMilisegundos = ahora.getTime();
				
		String texto = "csimon";
		String passw = "admin";
					       
		String nombre = texto + lnMilisegundos+"-"+numeroThread;
		
		
		try {

			agent = new InterfazSwing2();
			agentInterfaz = cc.acceptNewAgent(nombre, agent);
			if (agentInterfaz != null) {
				agentInterfaz.start();
				agent.setup();
				if (debug){
					System.out.println("Agente Arrancado: " + agentInterfaz.getState().getName());
				}
				
				while (!agent.isFinSetup()) {
				}
				
				if (debug){
					System.out.println("Agente Arrancado Nuevo estado: " + agentInterfaz.getState().getName());
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
		
		Resultado testigo = new Resultado();
		agent.swingAutentica(texto, passw, testigo);
		
		if (debug){
			System.out.println("Intentamos autenticarnos");
			System.out.println("Testigo: "+testigo.isResultadoB());
		}
						
		/*while(!testigo.isRelleno()){
			
		}*/
		
		autenticado = testigo.isResultadoB();
		if (debug){
			System.out.println("AUTENTICADO: "+autenticado);
		}
				
		Resultado testigo1 = new Resultado();
		agent.swingPideCorrector(testigo1, politica);
		if (debug){
			System.out.println("Intentamos buscar un corrector");
		}
		
		/*while(!testigo1.isRelleno()){
		}*/

		//---------------------- PRACTICAS -----------------------------
		Resultado testigo2 = new Resultado();
		agent.swingPidePracticas(testigo2);
		if (debug){
			System.out.println("Intentamos pedir las practicas");
		}
		
		/*while(!testigo2.isRelleno()){
		}*/

		String [] pract = (String [])testigo2.getResultado();
		int numPract = pract.length;

		Aleatorio rand = new Aleatorio();
		//int eleccion = rand.nextInt(0, numPract-1);
		int eleccion = practica_ - 1;
		
		String practica = pract[eleccion];

		//---------------------- FIN PRACTICAS -----------------------------


		//-------------------------- TESTS ---------------------------------
		
		Resultado testigo3 = new Resultado();

		agent.swingPideTests(testigo3, practica);
		if (debug){
			System.out.println("Intentamos pedir los tests");
		}
				
		/*while(!testigo3.isRelleno()){
		}*/

		String [] tests = (String [])testigo3.getResultado();
		
		int tamano = tests.length / 2;

		String[] tests2 = new String[tamano];
		for (int i = 0; i < tests2.length; i++) {
			tests2[i]=tests[i*2];
		}

		int numTests = tests2.length;

		//Numero de tests a seleccionar
		rand = new Aleatorio();
		eleccion = rand.nextInt(1, numTests);
		
		int ntests = tests_ - 1;
		
		//String [] listaTests = new String[eleccion];
		String [] listaTests = new String[tests_];

		/*for (int i = 0; i < listaTests.length; i++) {
			Aleatorio rand2 = new Aleatorio();
			int eleccion2 = rand2.nextInt(0, numTests-1);
			listaTests[i] = tests2[eleccion2];
		}*/
		
		for (int i = 0; i < listaTests.length; i++){
			listaTests[i] = tests2[ntests];
		}

		//-------------------------- FIN TESTS ---------------------------------




		//-------------------------- FICHEROS ------------------------------
		Resultado testigo4 = new Resultado();
		agent.swingPideFicheros(testigo4, listaTests);
		if (debug){
			System.out.println("Intentamos mandar los ficheros");
		}

		/*while(!testigo4.isRelleno()){
		}*/
		String [] fichs = (String [])testigo4.getResultado();

		String []cont = new String[fichs.length];

		for(int i = 0; i < cont.length; i++) {
			//String contenido3 = generaContenido();
			String contenido3 = generaContenido2(tamano_);
			cont[i]=contenido3;
		}

		Aleatorio randC = new Aleatorio();
		int numCorrec = randC.nextInt(1, 20);
		
		for (int i = 0; i < numCorrecionesPedidas; i++) {
			//Guardamos la hora de la peticion de correccion 
			Date comienzo = new Date();
			Long enMilisegundos = comienzo.getTime();


			Resultado testigo5 = new Resultado();

			agent.swingPideCorrector(testigo5, politica);
			if (debug){
				System.out.println("Intentamos buscar un corrector");
			}
			
			/*while(!testigo5.isRelleno()){
			}*/

			AID nombreC = (AID) testigo5.getResultado();
			String nombreCorto = nombreC.getName();

			Resultado testigo6 = new Resultado();

			agent.swingPideCorreccion(testigo6, cont);
			if (debug){
				System.out.println("Intentamos corregir algo..");
			}
			
			/*while(!testigo6.isRelleno()){
			}*/

			String salida = (String) testigo6.getResultado();

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
			
			agent.almacenCorrec.put(nombreC,duracion);
			
			
			
			//String sFichero = "C:\\Documents and Settings\\Carlos\\Escritorio\\Resultados\\"+nombre+".txt";
			//String sFichero = "C:\\Documents and Settings\\Carlos\\Escritorio\\Resultados\\Resultados.csv";
			String sFichero = "Resultados.csv";
			
			String inicio = enMilisegundos.toString();
			String terminacion = enMilisegundos2.toString();
			
			if (debug){
				System.out.println("Intentamos escribir algo... ");
			}
			
			EscribeFichero(sFichero, duraAux, nombre, nombreCorto, practica, politica, 
					ntests, inicio, terminacion, tamano_);
		}
		
		agent.doDelete();
	}

}
