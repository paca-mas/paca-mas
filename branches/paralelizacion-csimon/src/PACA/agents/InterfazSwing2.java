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
		addBehaviour(new EnviaAutenticaBehaviour(this, tes, usu, pass));
	}
	
	public void swingPideCorrector(Testigo tes){
		addBehaviour(new CorrectorBehaviour(this, tes));
	}
	
	public void swingPidePracticas(Testigo tes){
		addBehaviour(new PidePracticasBehavior(this, tes));
	}
	
	public void swingPideTests(Testigo tes, String practica){
		addBehaviour(new PideTestBeha(this, tes, practica));
	}
	
	public void swingPideFicheros(Testigo tes, String[] ficheros){
		addBehaviour(new PideFicherosBeha(this, tes, ficheros));
	}
	
	public void swingPideCorreccion(Testigo tes, String[] contenido){
		addBehaviour(new PideCorreccionBeha(this, tes, contenido));
	}
	

	
	
}




