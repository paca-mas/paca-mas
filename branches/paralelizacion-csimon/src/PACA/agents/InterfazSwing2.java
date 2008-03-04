package PACA.agents;


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

	
	
}




