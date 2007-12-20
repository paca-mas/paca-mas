package auth.ontology;




import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;



/**
   Clase que modela todas las relaciones y objetos que existen en la ontolog�a.
   Esta clase es utilizada por JADE para poder realizar las transiciones objetosOntologia-Java
   y viceversa.
   @author Sergio Saugar Garc�a.
   @modificado Carlos Sim�n Garc�a
 */
public class AuthOntology extends Ontology{
	//Carlos 


	/**
       Nombre de la ontologia
	 */
	public static final String ONTOLOGY_NAME = "AuthOntology-ontology";


	// Vocabulario


	/**
       Concepto Usuario.
	 */
	public static final String USUARIO = "usuario";
	public static final String USUARIO_NOMBRE = "user_id";
	public static final String USUARIO_PASS ="password";

	/**
       Predicado autenticado.
	 */
	public static final String AUTENTICADO = "autenticado";

	/**
       Instancia est�tica de esta ontolog�a. Responde a un patron de dise�o SINGLETON.
	 */
	private static Ontology theInstance = new AuthOntology();



	/**
       �ste m�todo devuelve la �nica instancia de la ontolog�a.
       @return Un objeto que representa a �sta ontolog�a.
	 */
	public static Ontology getInstance() {
		return theInstance;
	}

	/**
	 * Constructor privado
	 *
	 */
	private AuthOntology(){
		//Comienzo modificaci�n Carlos
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try { 
			add(new ConceptSchema(USUARIO), Usuario.class);
			add(new PredicateSchema(AUTENTICADO), Autenticado.class);

			// Estructura del esquema para el concepto Usuario (Carlos)
			ConceptSchema cs = (ConceptSchema) getSchema(USUARIO);
			cs.add(USUARIO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(USUARIO_PASS, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			// Estructura del esquema para el predicado Autenticado (Carlos)
			PredicateSchema ps = (PredicateSchema) getSchema(AUTENTICADO);
			ps.add(USUARIO, (ConceptSchema) getSchema(USUARIO));


		}
		// Fin modificacion Carlos

		catch(OntologyException oe) {
			oe.printStackTrace();
		}
	}
}
