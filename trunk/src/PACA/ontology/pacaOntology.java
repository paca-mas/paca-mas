


package PACA.ontology;



//import jade.onto.Frame;
//import jade.onto.Ontology;
//import jade.onto.DefaultOntology;
//import jade.onto.SlotDescriptor;
//import jade.onto.OntologyException;

//import jade.onto.basic.*;

import jade.content.AgentAction;
import jade.content.lang.sl.SL0Vocabulary;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import PACA.ontology.Fichero.Fichero;
import PACA.ontology.Fichero.FuentesPrograma;




/**
   Clase que representa las relaciones entre los diversos objetos de la ontología.
   Es necesaria en JADE para la conversión de objetosOntología-Java y viceversa.

   Modificado Carlos Simón García

 */
public class pacaOntology extends Ontology{

	/**
       Nombre de la ontología.
	 */
	public static final String NAME = "paca-ontology";

	// VOCABULARIO
	// Conceptos

	//Practica
	public static final String PRACTICA = "practica";
	//Añadido Carlos
	public static final String PRACTICA_ID="Id";
	public static final String PRACTICA_DESCRIPCION="Descripcion"; 

	//Test 
	public static final String TEST = "test";
	//Añadido Carlos
	public static final String TEST_ID="Id";
	public static final String TEST_DESCRIPCION="Descripcion";

	//Corrector
	public static final String CORRECTOR = "corrector";
	//Añadido Carlos
	public static final String CORRECTOR_ID ="Id";

	//Alumno
	public static final String ALUMNO = "alumno";
	//Añadido Carlos
	public static final String ALUMNO_ID = "identificador";
	public static final String ALUMNO_PASSWD = "password";

	//Fichero
	public static final String FICHERO = "fichero";
	public static final String FICHERO_NOMBRE = "Nombre";
	public static final String FICHERO_CONTENIDO = "Contenido"; 

	//FuentesPrograma
	public static final String FUENTESPROGRAMA = "fuentesPrograma";

	//EvaluacionPractica
	public static final String EVALUACIONPRACTICA = "evaluacionPractica";
	//Añadido Carlos
	public static final String EVALUACIONPRACTICA_TEXTO ="textoEvaluacion";
	
	public static final String INTERFAZ = "interfaz";
	
	
	public static final String RESULTADOEVALUACION = "resultadoEvaluacion";
	public static final String RESULTADOEVALUACION_TEXTO = "resultadoEvaluacionTexto";


	// Predicados

	public static final String CORRIGE = "Corrige";
	public static final String TESTS = "Tests";
	public static final String FICHEROFUENTES = "FicheroFuentes";
	public static final String EVALUAPRACTICA = "EvaluaPractica";
	public static final String FORMAGRUPOCON = "FormaGrupoCon";
	public static final String INTERACTUA = "Interactua";

	// Acciones

	public static final String ENTREGARPRACTICA = "EntregarPractica";
	public static final String ENTREGA = "Entrega";
	
	
	//AgentAction
	public static final String ACTIONENTREGAR = "ActionEntregar";

	/**
       Instancia de la ontología. Sigue un patrón de diseño SINGLETON.
	 */
	private static Ontology theInstance = new pacaOntology();


	/**
       Este método devuelve la única instancia de ésta ontología.
	 */
	public static Ontology getInstance() {
		return theInstance;
	}


	/**
	 * Constructor privado
	 *
	 */
	private pacaOntology(){
		//Comienzo modificación Carlos
		super(NAME,BasicOntology.getInstance());


		try {

			//---------- CONCEPTOS -----------
			//Concepto Practica
			add(new ConceptSchema(PRACTICA), Practica.class);
			ConceptSchema cs = (ConceptSchema) getSchema(PRACTICA);
			cs.add(PRACTICA_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(PRACTICA_DESCRIPCION, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Concepto Test
			add(new ConceptSchema(TEST), Test.class);
			ConceptSchema cs1 = (ConceptSchema) getSchema(TEST);
			cs1.add(TEST_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs1.add(TEST_DESCRIPCION, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Concepto Corrector
			add(new ConceptSchema(CORRECTOR), Corrector.class);
			ConceptSchema cs2 = (ConceptSchema) getSchema(CORRECTOR);
			cs2.add(CORRECTOR_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Concepto Alumno
			add(new ConceptSchema(ALUMNO), Alumno.class);
			ConceptSchema cs3 = (ConceptSchema) getSchema(ALUMNO);
			cs3.add(ALUMNO_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs3.add(ALUMNO_PASSWD, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Concepto Fichero
			add(new ConceptSchema(FICHERO), Fichero.class);
			ConceptSchema cs4 = (ConceptSchema) getSchema(FICHERO);
			cs4.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs4.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Fuentes Programa
			add(new ConceptSchema(FUENTESPROGRAMA),FuentesPrograma.class);
			ConceptSchema cs5 = (ConceptSchema) getSchema(FUENTESPROGRAMA);
			cs5.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs5.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			//Resultado Evaluacion		
			add(new ConceptSchema(RESULTADOEVALUACION), ResultadoEvaluacion.class);
			ConceptSchema cs6 = (ConceptSchema) getSchema(RESULTADOEVALUACION);
			cs6.add(RESULTADOEVALUACION_TEXTO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			
			//Pendiente de crear Concepto Interfaz!!!!!
			//---------- FIN CONCEPTOS -----------


			//---------- PREDICADOS -----------

			//Corrige
			add(new PredicateSchema(CORRIGE), Corrige.class);
			PredicateSchema ps = (PredicateSchema) getSchema(CORRIGE);
			ps.add(CORRECTOR, (ConceptSchema) getSchema(CORRECTOR));
			ps.add(PRACTICA,(ConceptSchema)getSchema(PRACTICA));

			//Tests
			add(new PredicateSchema(TESTS),Tests.class);
			PredicateSchema ps1 = (PredicateSchema) getSchema(TESTS);
			ps1.add(TEST, (ConceptSchema) getSchema(TEST));
			ps1.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

			//FicherosFuentes
			add(new PredicateSchema(FICHEROFUENTES),FicheroFuentes.class);
			PredicateSchema ps2 = (PredicateSchema) getSchema(FICHEROFUENTES);
			ps2.add(TEST, (ConceptSchema) getSchema(TEST));
			ps2.add(FUENTESPROGRAMA, (ConceptSchema) getSchema(FUENTESPROGRAMA));
			
			//EvaluaPractica
			add(new PredicateSchema(EVALUAPRACTICA), EvaluacionPractica.class);
			PredicateSchema ps6 = (PredicateSchema) getSchema(EVALUAPRACTICA);
			ps6.add(EVALUACIONPRACTICA_TEXTO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			ps6.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));
			

			//FormaGrupoCon
			add(new PredicateSchema(FORMAGRUPOCON),FormaGrupoCon.class);
			PredicateSchema ps3 = (PredicateSchema) getSchema(FORMAGRUPOCON);
			ps3.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));
			ps3.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));

			//Interactua
			add(new PredicateSchema(INTERACTUA),Interactua.class);
			PredicateSchema ps4 = (PredicateSchema) getSchema(INTERACTUA);
			ps4.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));
			ps4.add(INTERFAZ, (ConceptSchema) getSchema(BasicOntology.AID));
			
			
			//---------- FIN PREDICADOS ----------------
			
			
			
			//---------- AGENT ACTIONS ----------------
			//EntregarPractica
			add(new AgentActionSchema(ENTREGARPRACTICA),EntregarPractica.class);
			AgentActionSchema ps5 = (AgentActionSchema) getSchema(ENTREGARPRACTICA);
			ps5.add(CORRECTOR, (ConceptSchema) getSchema(CORRECTOR));
			ps5.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
			ps5.add(ENTREGA, (PredicateSchema) getSchema(SL1Vocabulary.AND));
						

		}
		catch(OntologyException oe) {
			oe.printStackTrace();
		}
		
		catch(Exception oe){
			oe.printStackTrace();
		}
	}
}





