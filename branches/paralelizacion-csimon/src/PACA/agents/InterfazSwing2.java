package PACA.agents;

import PACA.agents.Interfaz.CorrectorBehaviour;
import PACA.agents.Interfaz.EnviaAutenticaBehaviour;
import PACA.agents.InterfazJSP.AutenticaRequestBeha;
import PACA.util.Testigo;


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
	
	public void prueba(String usu, String pass, Testigo tes){
				
		System.out.println("Esto se lanza???");
		addBehaviour(new EnviaAutenticaBehaviour(this, tes, usu, pass));
		System.out.println("Esto se lanzó");
	}

	
	
}




