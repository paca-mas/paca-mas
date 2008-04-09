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
	
	private synchronized void EscribeFichero(String sFicher, String duraAux){
		File fichero = new File(sFicher);
		try {
			FileWriter ficheroA = new FileWriter(fichero,true);
			PrintWriter pw = new PrintWriter(ficheroA);
			pw.print(duraAux);
			pw.print(";");
			pw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//	Variables para crear el Agente Interfaz
	ContainerController cc;
	AgentController agentInterfaz=null;
	InterfazSwing2 agent=null;
	lanzador todosJuntos;
    //Fin Variables Agente Interfaz
	
/*	static void GeneraRetardo(int ret){
		try {
			Thread.sleep(ret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	int numeroThread=0;
	
	public SimulaAgentes(ContainerController cc, int num){
		super();
		this.cc = cc;
		numeroThread = num;
		//this.todosJuntos=lanz;
	}
		
	
	//public static void main(String[] args) {
	public void run(){
	
		
		Date ahora = new Date();
		long lnMilisegundos = ahora.getTime();
		//System.out.println("Ahora :"+ahora.toString());
		//System.out.println("Milisegundos: "+lnMilisegundos);
		
		String texto = "csimon";
		String passw = "admin";
					       
		String nombre = texto + lnMilisegundos+"-"+numeroThread;
		
		
		try {
			//rt = Runtime.instance();
			//p = new ProfileImpl(false);
			//cc = rt.createAgentContainer(p);
			agent = new InterfazSwing2();
			agentInterfaz = cc.acceptNewAgent(nombre, agent);
			if (agentInterfaz != null) {
				agentInterfaz.start();
				
				while (!agent.isFinSetup()) {
					//System.out.println("Esperando al fin... ");
				}
			} 
			else {
				System.out.println("Agente no Arrancado" + agentInterfaz.getState().getName());
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//while(!todosJuntos.getBol()){
		//}
		
		boolean autenticado = false;
		
		Resultado testigo = new Resultado();
		agent.swingAutentica(texto, passw, testigo);
		//System.out.println("--------------------");
						
		while(!testigo.isRelleno()){
		}
		
		autenticado = testigo.isResultadoB();
		//System.out.println("autenticado: "+autenticado);
		
		
		Resultado testigo1 = new Resultado();


		agent.swingPideCorrector(testigo1);
		while(!testigo1.isRelleno()){
		}
		
		
		//GeneraRetardo(1000);


		//---------------------- PRACTICAS -----------------------------
		Resultado testigo2 = new Resultado();


		agent.swingPidePracticas(testigo2);
		while(!testigo2.isRelleno()){
		}

		String [] pract = (String [])testigo2.getResultado();
		//pract = (String [])testigo.getResultado();

		int numPract = pract.length;

		//System.out.println("Numero de practicas: "+numPract);

		Aleatorio rand = new Aleatorio();
		int eleccion = rand.nextInt(0, numPract-1);
		//System.out.println("Resultado aleatorio para practicas: "+eleccion);

		//try {
		//Thread.sleep(1200);
		//} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		//}
		
		//GeneraRetardo(2000);

		//---------------------- FIN PRACTICAS -----------------------------


		//-------------------------- TESTS ---------------------------------
		Resultado testigo3 = new Resultado();

		//System.out.println("------------------------------------------------------");

		//System.out.println("Practica elegida: "+pract[eleccion].toString());
		agent.swingPideTests(testigo3, pract[eleccion]);
		while(!testigo3.isRelleno()){
		}

		String [] tests = (String [])testigo3.getResultado();
		
		int tamano = tests.length / 2;

		String[] tests2 = new String[tamano];
		for (int i = 0; i < tests2.length; i++) {
			tests2[i]=tests[i*2];
		}

		int numTests = tests2.length;
		//System.out.println("Numero de Tests: "+numTests);

		//Numero de tests a seleccionar
		rand = new Aleatorio();
		eleccion = rand.nextInt(0, numTests-1);
		//System.out.println("Resultado aleatorio para tests: "+eleccion);
		String [] listaTests = new String[eleccion+1];

		for (int i = 0; i < listaTests.length; i++) {
			Aleatorio rand2 = new Aleatorio();
			int eleccion2 = rand2.nextInt(0, numTests-1);
			listaTests[i] = tests2[eleccion2];
		}


		//try {
		//Thread.sleep(800);
		//} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		//}
		
		//GeneraRetardo(1000);


		//-------------------------- FIN TESTS ---------------------------------




		//-------------------------- FICHEROS ------------------------------
		Resultado testigo4 = new Resultado();



		agent.swingPideFicheros(testigo4, listaTests);

		while(!testigo4.isRelleno()){
		}
		String [] fichs = (String [])testigo4.getResultado();

		String []cont = new String[fichs.length];

		//System.out.println("------------------------------------------------------");

		for(int i = 0; i < cont.length; i++) {
			String contenido3 = generaContenido();
			cont[i]=contenido3;
			//System.out.println("Tamano del fichero: "+cont[i].length());
		}


		//try {
		//Thread.sleep(1800);
		//} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		//}
		
		//GeneraRetardo(4000);



		//Guardamos la hora de la peticion de correccion 
		Date comienzo = new Date();
		Long enMilisegundos = comienzo.getTime();

		
		Resultado testigo5 = new Resultado();


		agent.swingPideCorrector(testigo5);
		while(!testigo5.isRelleno()){
		}

		AID nombreC = (AID) testigo5.getResultado();
		String nombreCorto = nombreC.getName();
		//nombreCorto = nombreC.getName();
		//System.out.println("Corrector: "+nombreCorto);

		//GeneraRetardo(500);


		Resultado testigo6 = new Resultado();



		agent.swingPideCorreccion(testigo6, cont);
		while(!testigo6.isRelleno()){
		}

		String salida = (String) testigo6.getResultado();

		int posicion = salida.indexOf("terminacion_incorrecta");

		String textoEva = null;
		if (posicion!=-1){
			textoEva = "Practica Erronea";
		}
		else{
			textoEva = "Practica Aceptada";
		} 

		//System.out.println("Resultado de la correccion: "+textoEva);

		Date finalizado = new Date();
		Long enMilisegundos2 = finalizado.getTime();

		Long duracion = enMilisegundos2 - enMilisegundos;
		String duraAux = duracion.toString();
		//System.out.println("Tiempo necesitado: "+duraAux);
		//System.out.println("Interfaz: "+nombre); 
		//System.out.println("------------------------------------------------------");

		//String sFichero = "C:\\Documents and Settings\\Carlos\\Escritorio\\Resultados\\"+nombre+".txt";
		String sFichero = "C:\\Documents and Settings\\Carlos\\Escritorio\\Resultados\\Resultados.csv";
			
		//File fichero = new File(sFichero);
		
		EscribeFichero(sFichero, duraAux);
		
		
		/*try {
			//BufferedWriter bw = new BufferedWriter(new FileWriter(fichero,true));
			FileWriter ficheroA = new FileWriter(fichero,true);
			PrintWriter pw = new PrintWriter(ficheroA);
			//bw.write("Interfaz: "+nombre);
			//bw.newLine();
			//bw.write("Corrector: "+nombreCorto);
			//bw.newLine();
			//bw.write("Tiempo necesitado: "+duraAux);
			pw.print(duraAux);
			pw.print(";");
			bw.newLine();
			bw.write("Hora de comienzo: "+ahora);
			bw.newLine();
			bw.write("Hora de finalizacion: "+finalizado);
			//bw.close();
			pw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//try {
		//Thread.sleep(30000);
		//System.exit(0);
		//} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		//}
		
		
		
	}

}
