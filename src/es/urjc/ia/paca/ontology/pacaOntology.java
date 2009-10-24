package es.urjc.ia.paca.ontology;

//import jade.onto.Frame;
//import jade.onto.Ontology;
//import jade.onto.DefaultOntology;
//import jade.onto.SlotDescriptor;
//import jade.onto.OntologyException;
//import jade.onto.basic.*;
import es.urjc.ia.paca.ontology.Fichero.Fichero;
import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;
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

/**
Clase que representa las relaciones entre los diversos objetos de la ontolog�a.
Es necesaria en JADE para la conversi�n de objetosOntolog�a-Java y viceversa.

Modificado Carlos Sim�n Garc�a

 */
public class pacaOntology extends Ontology {

    /**
    Nombre de la ontolog�a.
     */
    public static final String NAME = "paca-ontology";
    // VOCABULARIO
    // Conceptos
    //Practica
    public static final String PRACTICA = "practica";
    //A�adido Carlos
    public static final String PRACTICA_ID = "Id";
    public static final String PRACTICA_DESCRIPCION = "Descripcion";
    //Añadido Alvaro
    public static final String PRACTICA_FECHA_ENTREGA = "FechaEntrega";
    //Test
    public static final String TEST = "test";
    //A�adido Carlos
    public static final String TEST_ID = "Id";
    public static final String TEST_DESCRIPCION = "Descripcion";
    //Corrector
    public static final String CORRECTOR = "corrector";
    //A�adido Carlos
    public static final String CORRECTOR_ID = "Id";
    //Alumno
    public static final String ALUMNO = "alumno";
    //A�adido Carlos
    public static final String ALUMNO_ID = "identificador";
    public static final String ALUMNO_PASSWD = "password";
    //Fichero
    public static final String FICHERO = "fichero";
    public static final String FICHERO_NOMBRE = "Nombre";
    public static final String FICHERO_CONTENIDO = "Contenido";
    //FuentesPrograma
    public static final String FUENTESPROGRAMA = "fuentesPrograma";
    //Modificado Alvaro
    public static final String FUENTESPROGRAMA_CODIGO = "Codigo";
    //EvaluacionPractica
    public static final String EVALUACIONPRACTICA = "evaluacionPractica";
    //A�adido Carlos
    public static final String EVALUACIONPRACTICA_TEXTO = "textoEvaluacion";
    public static final String INTERFAZ = "interfaz";
    public static final String RESULTADOEVALUACION = "resultadoEvaluacion";
    public static final String RESULTADOEVALUACION_TEXTO = "resultadoEvaluacionTexto";
    //Añadido Alvaro
    //FicheroIN
    public static final String FICHEROIN = "ficheroIN";
    //FicheroOUT
    public static final String FICHEROOUT = "ficheroOUT";
    //FicheroPropio
    public static final String FICHEROPROPIO = "ficheroPropio";
    //Caso
    public static final String CASO = "caso";
    public static final String CASO_ID = "Id";
    //Fichero Alumno
    public static final String FICHEROALUMNO = "ficheroAlumno";
    // Predicados
    public static final String CORRIGE = "Corrige";
    public static final String TESTS = "Tests";
    public static final String FICHEROFUENTES = "FicheroFuentes";
    public static final String EVALUAPRACTICA = "EvaluaPractica";
    public static final String FORMAGRUPOCON = "FormaGrupoCon";
    public static final String INTERACTUA = "Interactua";
    //Añadido Alvaro
    public static final String FICHEROSPROPIOS = "FicherosPropios";
    public static final String CASOS = "Casos";
    public static final String FICHEROSIN = "FicherosIN";
    public static final String FICHEROSOUT = "FicherosOUT";
    public static final String FICHEROSALUMNO = "FicherosAlumno";
    // Acciones
    public static final String ENTREGARPRACTICA = "EntregarPractica";
    public static final String ENTREGA = "Entrega";
    //AgentAction
    public static final String ACTIONENTREGAR = "ActionEntregar";
    public static final String MODIFICAPRACTICA = "ModificaPractica";
    public static final String MODIFICATEST = "ModificaTest";
    public static final String MODIFICAFICHEROPROPIO = "ModificaFicheroPropio";
    public static final String MODIFICAFICHEROIN = "ModificaFicheroIN";
    public static final String MODIFICAFICHEROOUT = "ModificaFicheroOUT";
    /**
    Instancia de la ontolog�a. Sigue un patr�n de dise�o SINGLETON.
     */
    private static Ontology theInstance = new pacaOntology();

    /**
    Este m�todo devuelve la �nica instancia de �sta ontolog�a.
     */
    public static Ontology getInstance() {
        return theInstance;
    }

    /**
     * Constructor privado
     *
     */
    private pacaOntology() {
        //Comienzo modificaci�n Carlos
        super(NAME, BasicOntology.getInstance());


        try {

            //---------- CONCEPTOS -----------
            //Concepto Practica
            add(new ConceptSchema(PRACTICA), Practica.class);
            ConceptSchema cs = (ConceptSchema) getSchema(PRACTICA);
            cs.add(PRACTICA_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(PRACTICA_DESCRIPCION, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(PRACTICA_FECHA_ENTREGA, (PrimitiveSchema) getSchema(BasicOntology.STRING));

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
            add(new ConceptSchema(FUENTESPROGRAMA), FuentesPrograma.class);
            ConceptSchema cs5 = (ConceptSchema) getSchema(FUENTESPROGRAMA);
            cs5.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs5.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs5.add(FUENTESPROGRAMA_CODIGO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            //Resultado Evaluacion
            add(new ConceptSchema(RESULTADOEVALUACION), ResultadoEvaluacion.class);
            ConceptSchema cs6 = (ConceptSchema) getSchema(RESULTADOEVALUACION);
            cs6.add(RESULTADOEVALUACION_TEXTO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            //Añadido por Alvaro
            //Concepto Caso
            add(new ConceptSchema(CASO), Caso.class);
            ConceptSchema cs10 = (ConceptSchema) getSchema(CASO);
            cs10.add(CASO_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));


            //Concepto Fichero Propio
            add(new ConceptSchema(FICHEROPROPIO), FicheroPropio.class);
            ConceptSchema cs11 = (ConceptSchema) getSchema(FICHEROPROPIO);
            cs11.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs11.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs11.add(FUENTESPROGRAMA_CODIGO, (PrimitiveSchema) getSchema(BasicOntology.STRING));


            //Concepto FicheroIN
            add(new ConceptSchema(FICHEROIN), FicheroIN.class);
            ConceptSchema cs7 = (ConceptSchema) getSchema(FICHEROIN);
            cs7.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs7.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            //Concepto FicheroOUT
            add(new ConceptSchema(FICHEROOUT), FicheroOUT.class);
            ConceptSchema cs8 = (ConceptSchema) getSchema(FICHEROOUT);
            cs8.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs8.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            //Concepto FicheroAlumno
            add(new ConceptSchema(FICHEROALUMNO), FicheroAlumno.class);
            ConceptSchema cs9 = (ConceptSchema) getSchema(FICHEROALUMNO);
            cs9.add(FICHERO_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs9.add(FICHERO_CONTENIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs9.add(FUENTESPROGRAMA_CODIGO, (PrimitiveSchema) getSchema(BasicOntology.STRING));


            //Pendiente de crear Concepto Interfaz!!!!!
            //---------- FIN CONCEPTOS -----------


            //---------- PREDICADOS -----------

            //Corrige
            add(new PredicateSchema(CORRIGE), Corrige.class);
            PredicateSchema ps = (PredicateSchema) getSchema(CORRIGE);
            ps.add(CORRECTOR, (ConceptSchema) getSchema(CORRECTOR));
            ps.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            //Tests
            add(new PredicateSchema(TESTS), Tests.class);
            PredicateSchema ps1 = (PredicateSchema) getSchema(TESTS);
            ps1.add(TEST, (ConceptSchema) getSchema(TEST));
            ps1.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            //FicherosFuentes
            add(new PredicateSchema(FICHEROFUENTES), FicheroFuentes.class);
            PredicateSchema ps2 = (PredicateSchema) getSchema(FICHEROFUENTES);
            ps2.add(TEST, (ConceptSchema) getSchema(TEST));
            ps2.add(FUENTESPROGRAMA, (ConceptSchema) getSchema(FUENTESPROGRAMA));

            //EvaluaPractica
            add(new PredicateSchema(EVALUAPRACTICA), EvaluacionPractica.class);
            PredicateSchema ps6 = (PredicateSchema) getSchema(EVALUAPRACTICA);
            ps6.add(EVALUACIONPRACTICA_TEXTO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            ps6.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));


            //FormaGrupoCon
            add(new PredicateSchema(FORMAGRUPOCON), FormaGrupoCon.class);
            PredicateSchema ps3 = (PredicateSchema) getSchema(FORMAGRUPOCON);
            ps3.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));
            ps3.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));

            //Interactua
            add(new PredicateSchema(INTERACTUA), Interactua.class);
            PredicateSchema ps4 = (PredicateSchema) getSchema(INTERACTUA);
            ps4.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));
            ps4.add(INTERFAZ, (ConceptSchema) getSchema(BasicOntology.AID));

            //Añadido por Alvaro

            //FicherosPropios
            add(new PredicateSchema(FICHEROSPROPIOS), FicherosPropios.class);
            PredicateSchema ps11 = (PredicateSchema) getSchema(FICHEROSPROPIOS);
            ps11.add(TEST, (ConceptSchema) getSchema(TEST));
            ps11.add(FICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));

            //Casos
            add(new PredicateSchema(CASOS), Casos.class);
            PredicateSchema ps10 = (PredicateSchema) getSchema(CASOS);
            ps10.add(TEST, (ConceptSchema) getSchema(TEST));
            ps10.add(CASO, (ConceptSchema) getSchema(CASO));


            //FICHEROS IN
            add(new PredicateSchema(FICHEROSIN), FicherosIN.class);
            PredicateSchema ps9 = (PredicateSchema) getSchema(FICHEROSIN);
            ps9.add(FICHEROIN, (ConceptSchema) getSchema(FICHEROIN));
            ps9.add(CASO, (ConceptSchema) getSchema(CASO));

            //FICHEROS OUT
            add(new PredicateSchema(FICHEROSOUT), FicherosOUT.class);
            PredicateSchema ps8 = (PredicateSchema) getSchema(FICHEROSOUT);
            ps8.add(FICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));
            ps8.add(CASO, (ConceptSchema) getSchema(CASO));


            //FicherosAlumno
            add(new PredicateSchema(FICHEROSALUMNO), FicherosAlumno.class);
            PredicateSchema ps7 = (PredicateSchema) getSchema(FICHEROSALUMNO);
            ps7.add(TEST, (ConceptSchema) getSchema(TEST));
            ps7.add(FICHEROALUMNO, (ConceptSchema) getSchema(FICHEROALUMNO));



            //---------- FIN PREDICADOS ----------------



            //---------- AGENT ACTIONS ----------------
            //EntregarPractica
            add(new AgentActionSchema(ENTREGARPRACTICA), EntregarPractica.class);
            AgentActionSchema ps5 = (AgentActionSchema) getSchema(ENTREGARPRACTICA);
            ps5.add(CORRECTOR, (ConceptSchema) getSchema(CORRECTOR));
            ps5.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps5.add(ENTREGA, (PredicateSchema) getSchema(SL1Vocabulary.AND));

            add(new AgentActionSchema(MODIFICAPRACTICA), ModificaPractica.class);
            AgentActionSchema ps12 = (AgentActionSchema) getSchema(MODIFICAPRACTICA);
            ps12.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(MODIFICATEST), ModificaTest.class);
            AgentActionSchema ps13 = (AgentActionSchema) getSchema(MODIFICATEST);
            ps13.add(TEST, (ConceptSchema) getSchema(TEST));
            ps13.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(MODIFICAFICHEROPROPIO), ModificaFicheroPropio.class);
            AgentActionSchema ps14 = (AgentActionSchema) getSchema(MODIFICAFICHEROPROPIO);
            ps14.add(TEST, (ConceptSchema) getSchema(TEST));
            ps14.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps14.add(FICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));

            add(new AgentActionSchema(MODIFICAFICHEROIN), ModificaFicheroIN.class);
            AgentActionSchema ps15 = (AgentActionSchema) getSchema(MODIFICAFICHEROIN);
            ps15.add(TEST, (ConceptSchema) getSchema(TEST));
            ps15.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps15.add(CASO, (ConceptSchema) getSchema(CASO));
            ps15.add(FICHEROIN, (ConceptSchema) getSchema(FICHEROIN));


                        add(new AgentActionSchema(MODIFICAFICHEROOUT), ModificaFicheroOUT.class);
            AgentActionSchema ps16 = (AgentActionSchema) getSchema(MODIFICAFICHEROOUT);
            ps16.add(TEST, (ConceptSchema) getSchema(TEST));
            ps16.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps16.add(CASO, (ConceptSchema) getSchema(CASO));
            ps16.add(FICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));



        } catch (OntologyException oe) {
            oe.printStackTrace();
        } catch (Exception oe) {
            oe.printStackTrace();
        }
    }
}





