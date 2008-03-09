package PACA.agents;

import PACA.util.*;


public class InterfazSwing2 extends Interfaz{
	
	public boolean FinSetup = false;
	@Override
	protected void setup(){
		super.setup();
		System.out.println("Creada Interfaz SWING");
		FinSetup=true;
	}
	
	public boolean isFinSetup(){
		return FinSetup;
	}
	
	public void swingAutentica(String usu, String pass, Testigo tes){
		System.out.println("Esto se lanza???");
		addBehaviour(new EnviaAutenticaBehaviour(this, tes, usu, pass));
		System.out.println("Esto se lanzó");
	}
	
	public void swingPideCorrector(Testigo tes){
		System.out.println("En busqueda del corrector... ");
		addBehaviour(new CorrectorBehaviour(this, tes));
		System.out.println("Corrector encontrado... ");
	}
	
	public void swingPidePracticas(Testigo tes){
		System.out.println("En busqueda de las practicas... ");
		addBehaviour(new PidePracticasBehavior(this, tes));
		System.out.println("Practicas encontradas... ");
	}
	
	public void swingPideTests(Testigo tes, String practica){
		System.out.println("En busqueda de los tests... ");
		addBehaviour(new PideTestBeha(this, tes, practica));
		System.out.println("Tests encontrados... ");
	}
	
	public void swingPideFicheros(Testigo tes, String[] ficheros){
		System.out.println("En busqueda de los ficheros... ");
		addBehaviour(new PideFicherosBeha(this, tes, ficheros));
		System.out.println("Ficheros encontrados... ");
	}
	

	
	
}




