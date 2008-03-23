package PACA.agents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.Date;
import java.util.Random;

import PACA.agents.lanzaSwing2.Aleatorio;
import PACA.util.Testigo;

public class SimulaAgentes {
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
	
	//	Variables para crear el Agente Interfaz
	static Runtime rt;
	static Profile p;
	static ContainerController cc;
	static AgentController agentInterfaz=null;
	static InterfazSwing2 agent=null;
    //Fin Variables Agente Interfaz
	
	public static void main(String[] args) {
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
		
		testigo = new Testigo();
		agent.swingPideCorrector(testigo);
		while(!testigo.isRelleno()){
		}
			
		
		//---------------------- PRACTICAS -----------------------------
		testigo = new Testigo();
			
		agent.swingPidePracticas(testigo);
		while(!testigo.isRelleno()){
		}
			
		String [] pract = (String [])testigo.getResultado();
			
		int numPract = pract.length;
		
		System.out.println("Numero de practicas: "+numPract);
			
		Aleatorio rand = new Aleatorio();
		int eleccion = rand.nextInt(0, numPract-1);
		System.out.println("Resultado aleatorio para practicas: "+eleccion);
		
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//---------------------- FIN PRACTICAS -----------------------------
		
		
		//-------------------------- TESTS ---------------------------------
		testigo = new Testigo();
		System.out.println("Practica elegida: "+pract[eleccion].toString());
		agent.swingPideTests(testigo, pract[eleccion]);
		while(!testigo.isRelleno()){
		}
		
		String [] tests = (String [])testigo.getResultado();
		
		int tamano = tests.length / 2;
		
		String[] tests2 = new String[tamano];
		for (int i = 0; i < tests2.length; i++) {
				tests2[i]=tests[i*2];
		}
		
		int numTests = tests2.length;
		System.out.println("Numero de Tests: "+numTests);
		
		//Numero de tests a seleccionar
		rand = new Aleatorio();
		eleccion = rand.nextInt(0, numTests-1);
		System.out.println("Resultado aleatorio para tests: "+eleccion);
		String [] listaTests = new String[eleccion+1];
		for (int i = 0; i < listaTests.length; i++) {
			Aleatorio rand2 = new Aleatorio();
			int eleccion2 = rand2.nextInt(0, numTests-1);
			listaTests[i] = tests2[eleccion2];
		}
		
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//-------------------------- FIN TESTS ---------------------------------
		
		
		
		
		//-------------------------- FICHEROS ------------------------------
		testigo = new Testigo();
		agent.swingPideFicheros(testigo, listaTests);
		
		while(!testigo.isRelleno()){
		}
		String [] fichs = (String [])testigo.getResultado();
		
		String []cont = new String[fichs.length];
		
		for(int i = 0; i < cont.length; i++) {
			String contenido3 = generaContenido();
			cont[i]=contenido3;
			System.out.println("Tamano del fichero: "+cont[i].length());
		}
		
			
		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Guardamos la hora de la peticion de correccion 
		Date comienzo = new Date();
		Long enMilisegundos = comienzo.getTime();
		
		testigo = new Testigo();
		
		agent.swingPideCorrector(testigo);
		while(!testigo.isRelleno()){
		}
		
		AID nombreC = (AID) testigo.getResultado();
		String nombreCorto = nombreC.getName();
		System.out.println("Corrector: "+nombreCorto);
		
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
		
		System.out.println("Resultado de la correccion: "+textoEva);
		
		Date finalizado = new Date();
		Long enMilisegundos2 = finalizado.getTime();
		
		Long duracion = enMilisegundos2 - enMilisegundos;
		String duraAux = duracion.toString();
		System.out.println("Tiempo necesitado: "+duraAux);
		System.out.println("------------------------------------------------------");
		
		try {
			Thread.sleep(30000);
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
