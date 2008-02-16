
package PACA.agents;



import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.tools.testagent.TestAgentFrame;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.wrapper.AgentController;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import PACA.parser.EvaluacionParser;
import auth.util.Testigo;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;


/** 
    Agente proxy entre la interfaz JSP y la plataforma JADE.
    Hereda de un agente con funciones para interactuar con la 
    plataforma JADE y extiende las funciones propias para interacturar
    con JSP.
    @author Sergio Saugar García.
    Modificado para pruebas Carlos Simón García
 */
public class InterfazJSP extends Interfaz {
	
	/**
       Lee la petición, de un formulario con "checkbox" que indican
       los test de los que se solicitan los ficheros. Almacenamos
       los test marcados y llamamos al "padre" con la función 
       doFicherosPractica.
       @param request Petición del navegador, con los datos de formulario
	 */
	public String[] doFicherosPracticaRequest(HttpServletRequest request)
	throws IOException, java.io.UnsupportedEncodingException {

		String[] NombreTest = new String[0];
		List ListaTest = new ArrayList();

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		MultipartParser parser = new MultipartParser(request,1000000);

		Part parte = parser.readNextPart();

		while (parte != null) {

			if (parte.isParam()) {
				ParamPart parametro = (ParamPart) parte;
				// Si el parámetro está marcado (CHEKED) lo guardamos
				if (parametro.getStringValue().equals("CHEKED")) {
					ListaTest.add(parte.getName());
				}
			}
			parte = parser.readNextPart();
		}

		// Pasamos al array
		NombreTest = new String[ListaTest.toArray().length];
		for (int i=0; i < ListaTest.toArray().length; i++) {
			NombreTest[i] = (String) ListaTest.toArray()[i];
		}
		
		return (doFicherosPractica(NombreTest));
		//return NombreTest;
	}



	/**
       Este método será invocado desde la página JSP para que el agente realice la corrección de una práctica.
       @param request Petición que le ha sido realizada al JSP y que contiene los ficheros para la corrección.
	*/
	public String doCorreccionRequest(HttpServletRequest request) throws IOException {

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		MultipartParser parser = new MultipartParser(request,1000000);

		// Almacenamos los ficheros en un array de String
		String[] contenidos = new String[ficherosUltimaPractica.length];
		int cont = 0;

		Part parte = parser.readNextPart();

		while (parte!=null){
			if (parte.isFile()){
				FilePart filepart = (FilePart) parte;
				InputStream is = filepart.getInputStream();
				StringWriter sw = new StringWriter();

				int tempo = is.read();
				while (tempo != -1 ) {
					sw.write(tempo);
					tempo = is.read();
				}

				contenidos[cont] = sw.toString();
				cont++;
			}
			parte = parser.readNextPart();
		}

		String salida="";

		try{

			EvaluacionParser XMLparser = new EvaluacionParser(doCorreccion(contenidos),this);
			salida= XMLparser.getEvaluacion();
		}
		catch(Exception e){
			salida="Hubo Errores "+e.getMessage();
		}
		return salida;
	}


	/** HECHO
       Método que obtiene los tests (que han sido seleccionados para ser corregidos) a partir de un objeto
       petición.
       @param request Petición que le ha sido realizada al JSP y que contiene los tests seleccionados.
       @return Los identificadores de los tests seleccionados.
	 */
	public String[] doTestPracticasRequest(HttpServletRequest request){

		ArrayList lista = new ArrayList();

		// Miramos de todos los posibles, los que estén seleccionados.
		for(int i=0;i<this.TestPosiblesPractica.length;i++){
			if(request.getParameter(this.TestPosiblesPractica[i])!=null && 
					((String)request.getParameter(this.TestPosiblesPractica[i])).equals("on")){
				lista.add(this.TestPosiblesPractica[i]);
			}
		};

		// Ahora los insertamos en el array de salida.
		String[] output = new String[lista.size()];

		if (!lista.isEmpty()){
			for (int i=0;i<lista.size();i++){
				output[i]=(String)lista.get(i);
			}
		}
		
		return output;
	}


	/**
       Resuelve los tests que son necesarios para la entrega de una práctica.
	*/ 
	public String[] doTestEntregaFinal(HttpServletRequest req){
		// En realidad no tratamos la petición, sino que siempre
		// querremos todos los ficheros posibles.

		return this.TestPosiblesPractica;
	}


	/**
       Atiende a la petición de entrega de una práctica.
	*/
	public String doEntregaFinalRequest(HttpServletRequest request) throws IOException {

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		MultipartParser parser = new MultipartParser(request,1000000);

		// Almacenamos los ficheros en un array de String
		String[] contenidos = new String[ficherosUltimaPractica.length];
		int cont = 0;

		// Login y password del compañero.

		String login="";
		String pass="";


		// Empezamos a leer.

		Part parte = parser.readNextPart();

		while (parte!=null){
			if (parte.isFile()){//Es un fichero.
				FilePart filepart = (FilePart) parte;
				InputStream is = filepart.getInputStream();
				StringWriter sw = new StringWriter();

				int tempo = is.read();
				while (tempo != -1 ) {
					sw.write(tempo);
					tempo = is.read();
				}

				//Modificado por ssaugar@platon.escet.urjc.es
				//Codificamos el contenido a base64.
				//contenidos[cont] = sw.toString();

				contenidos[cont] = sw.toString();
				cont++;
			}
			if (parte.isParam()){//Entonces es un parámetro normal.
				if(parte.getName().equals("login")){
					ParamPart parampart = (ParamPart) parte;
					login = parampart.getStringValue();
				}
				else{
					ParamPart parampart = (ParamPart) parte;
					pass = parampart.getStringValue();
				}
			}

			parte = parser.readNextPart();
		}

		String salida="";

		salida = doEntregaPractica(contenidos,login,pass);

		return salida;
	}



	/**
       Autentica a un usuario. HECHO
	 */
	public boolean doAutenticacionRequest(HttpServletRequest request){

		return this.doAutenticacion(request.getParameter("user_id"), request.getParameter("password"));
	}


	/**
       Obtiene los tests relativos a una práctica. HECHO
	*/
	public String[] doPeticionTestPracticaRequest(HttpServletRequest request){

		return this.doPeticionTestPractica(request.getParameter("practica"));
	}
	
	
	//================================================================================
	public String[] doObtieneContenidoRequest(HttpServletRequest request) throws IOException {

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		MultipartParser parser = new MultipartParser(request,1000000);

		// Almacenamos los ficheros en un array de String
		String[] contenidos = new String[ficherosUltimaPractica.length];
		int cont = 0;

		Part parte = parser.readNextPart();

		while (parte!=null){
			if (parte.isFile()){
				FilePart filepart = (FilePart) parte;
				InputStream is = filepart.getInputStream();
				StringWriter sw = new StringWriter();

				int tempo = is.read();
				while (tempo != -1 ) {
					sw.write(tempo);
					tempo = is.read();
				}

				contenidos[cont] = sw.toString();
				cont++;
			}
			parte = parser.readNextPart();
		}

		return contenidos;
	}
	
	public String ParseaSalida (String salida1){
		String salida="";
		try{

			EvaluacionParser XMLparser = new EvaluacionParser(salida1,this);
			salida= XMLparser.getEvaluacion();
		}
		catch(Exception e){
			salida="Hubo Errores "+e.getMessage();
		}
		return salida;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//========================================================================
	public class AutenticaRequestBeha extends OneShotBehaviour{
		
		private Testigo test2;
		
		public AutenticaRequestBeha (Agent _a, Testigo tes1){
			super(_a);
			this.test2 = tes1;
		}
		
		public void action(){
			HttpServletRequest param1 = (HttpServletRequest) test2.getParametro();
			String auxUsuario = param1.getParameter("user_id");
			String auxPass = param1.getParameter("password");
			//addBehaviour(new AutenticaBehaviour(this.myAgent, test2, auxUsuario, auxPass));
			addBehaviour(new EnviaAutenticaBehaviour(this.myAgent, test2, auxUsuario, auxPass));
		}
	}
	
	public class PideTestRequestBeha extends OneShotBehaviour{
		private Testigo tes2;
		
		public PideTestRequestBeha (Agent _a, Testigo tes1){
			super(_a);
			this.tes2 = tes1;
		}
		
		public void action(){
			HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
			String practica = param1.getParameter("practica");
			//addBehaviour(new TestsBehaviour(this.myAgent,tes2,practica));
			addBehaviour(new PideTestBeha(this.myAgent, tes2, practica));
		}
	}
	
			
	public class InicializaObjeto extends CyclicBehaviour{
		
		private boolean done=false;
		
		private int chivato=0;
		
		
		public void action() {
			while (this.myAgent==null){
				System.out.println("AGENT ES NULL");
			}
			InterfazJSP agent = (InterfazJSP)this.myAgent;
			while (!agent.FinSetup){
				System.out.println("FINSETUP");
			}
			
			
			Object o = this.myAgent.getO2AObject();
			
			
			
			if (o!=null){
				//((Niapa)o).setAgent((InterfazJSP)this.myAgent);
																	
				Testigo tes = (Testigo)o;
				System.out.println("Que operacion es? "+tes.getOperacion());
				switch (tes.getOperacion()){
				
				case buscarCorrector:
					//System.out.println("Operacion BuscarCorrector");
					//tes.setResultado(agent.doBuscarCorrector());
					addBehaviour(new CorrectorBehaviour(agent, tes));
					break;
				
				case autenticar:
					//System.out.println("Operacion autenticar");
					//tes.setResultadoB(agent.doAutenticacionRequest((HttpServletRequest) tes.getParametro()));
					addBehaviour(new AutenticaRequestBeha(agent, tes));
					break;
					
				case pedirPracticas:
					//System.out.println("Operacion pedirPracticas");
					//tes.setResultado(agent.doPeticion());
					//addBehaviour(new PracticasBehaviour(agent, tes));
					addBehaviour(new PidePracticasBehavior(agent, tes));
					break;
					
				case pedirTests:
					//System.out.println("Operacion pedirTests");
					//tes.setResultado(agent.doPeticionTestPracticaRequest((HttpServletRequest) tes.getParametro()));
					addBehaviour(new PideTestRequestBeha(agent, tes));
					break;
					
				case pedirFicheros:
					System.out.println("Operacion pedirFicheros");
					tes.setResultado(agent.doTestPracticasRequest((HttpServletRequest) tes.getParametro()));
					break;
					
				case insertarFicheros:
					System.out.println("Operacion InsertarFicheros");
					//tes.setResultado(agent.doFicherosPractica((String[]) tes.getParametro()));
					addBehaviour(new PideFicherosBeha(agent, tes, (String[]) tes.getParametro()));
					break;
					
				case corregir:
					System.out.println("Operacion corregir algo... ");
					//try {
						//tes.setResultado(agent.doCorreccionRequest((HttpServletRequest) tes.getParametro()));
						//}
					//catch (IOException e) {
						//TODO Auto-generated catch block
						//e.printStackTrace();
					//}
					
					//============= Comportamiento ====================
					try {
							String[] contenido = doObtieneContenidoRequest((HttpServletRequest)tes.getParametro());
							addBehaviour(new PideCorreccionBeha(agent, tes, contenido));
						} 
					catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}		
					break;
					
				case parsear:
					System.out.println("Operacion parseo XML ");
					tes.setResultado(ParseaSalida((String) tes.getParametro()));
					break;
					
				default: 
					tes.setResultado(chivato+"-");
					chivato++;
					break;
				}	
				
				
				System.out.println("Objetoooooooo"+o);
				done=true;
				
							
			}
			
			
		}
		
		
	}
	
	
	
	public boolean FinSetup=false;
	
	
	
	protected void setup(){
		super.setup();
		System.out.print("Ni idea");
		this.setEnabledO2ACommunication(true, 0);
		
		System.out.println("añadir comportamineto");
		addBehaviour(new InicializaObjeto());
		System.out.println("añadido comportamineto");
		
		FinSetup=true;
	}
	
	public boolean isFinSetup(){
		return FinSetup;
	}
}



	
	
	

      

