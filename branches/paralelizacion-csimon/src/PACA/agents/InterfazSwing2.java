package PACA.agents;

import PACA.util.*;


public class InterfazSwing2 extends Interfaz{
	
	public boolean FinSetup = false;
	@Override
	protected void setup(){
		super.setup();
		//System.out.println("Creada Interfaz SWING");
		FinSetup=true;
	}
	
	public boolean isFinSetup(){
		return FinSetup;
	}
	
	public void swingAutentica(String usu, String pass, Resultado tes){
		addBehaviour(new EnviaAutenticaBehaviour(this, tes, usu, pass));
	}
	
	public void swingPideCorrector(Resultado tes, String politica, int porcentaje){
		addBehaviour(new CorrectorBehaviour(this, tes, politica, porcentaje));
	}
	
	public void swingPidePracticas(Resultado tes){
		addBehaviour(new PidePracticasBehavior(this, tes));
	}
	
	public void swingPideTests(Resultado tes, String practica){
		addBehaviour(new PideTestBeha(this, tes, practica));
	}
	
	public void swingPideFicheros(Resultado tes, String[] ficheros){
		addBehaviour(new PideFicherosBeha(this, tes, ficheros));
	}
	
	public void swingPideCorreccion(Resultado testigo6, String[] contenido){
		addBehaviour(new PideCorreccionBeha(this, testigo6, contenido));
	}
	

	
	
}




