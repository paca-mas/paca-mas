package es.urjc.ia.paca.ontology;

//import jade.onto.Frame;
//import jade.onto.Ontology;
//import jade.onto.DefaultOntology;
//import jade.onto.SlotDescriptor;
//import jade.onto.OntologyException;
//import jade.onto.basic.*;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import es.urjc.ia.paca.ontology.Fichero.Fichero;
import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;

/**
Clase que representa las relaciones entre los diversos objetos de la ontolog�a.
Es necesaria en JADE para la conversi�n de objetosOntolog�a-Java y viceversa.

Modificado Carlos Sim�n Garc�a

 */
public class pacaOntology extends Ontology {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
    public static final String TEST_EJECUTABLE = "Ejecutable";
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
    
    //*********************************************************
    //  Modificado Sandra
    // Estadisticas - EvaluacionCaso
    public static final String ESTADISTICAEVALUACIONCASO = "EstadisticaEvaluacionCaso";
    public static final String ESTADISTICAEVALUACIONCASO_ALUMNO = "alumno";
    public static final String ESTADISTICAEVALUACIONCASO_PRACTICA = "practica";
    public static final String ESTADISTICAEVALUACIONCASO_TEST = "test";
    public static final String ESTADISTICAEVALUACIONCASO_CASO = "caso";
    public static final String ESTADISTICAEVALUACIONCASO_EVALUACION = "evaluacion";

    // Registrar Estadistica Practica
    public static final String REGISTRARESTADISTICAEVALUACION = "RegistrarEstadisticaEvaluacion";
    public static final String EVALUACIONCASOS = "evaluacionesCasos";
    
    // Estadisticas - EntregaPractica 
    public static final String REGISTRARESTADISTICAENTREGA = "RegistrarEstadisticaEntrega";
    public static final String REGISTRARESTADISTICAENTREGA_ALUMNO1 = "alumno1";
    public static final String REGISTRARESTADISTICAENTREGA_ALUMNO2 = "alumno2";
    public static final String REGISTRARESTADISTICAENTREGA_PRACTICA = "practica";
    
    // Estadistica - Ordenes BBDD
    public static final String MODIFICARDATOSBBDD = "EliminarDatosBBDD";
    public static final String ELIMINARDATOSBBDDTIPO = "tipo";
    //*********************************************************
    
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
    public static final String CREAPRACTICA = "CreaPractica";
    public static final String CREATEST = "CreaTest";
    public static final String CREAFICHEROPROPIO = "CreaFicheroPropio";
    public static final String CREACASO = "CreaCaso";
    public static final String CREAFICHEROALUMNO = "CreaFicheroAlumno";
    public static final String CREAFICHEROIN = "CreaFicheroIN";
    public static final String CREAFICHEROOUT = "CreaFicheroOUT";
    public static final String ELIMINAPRACTICA = "EliminaPractica";
    public static final String ELIMINATEST = "EliminaTest";
    public static final String ELIMINAFICHEROPROPIO = "EliminaFicheroPropio";
    public static final String ELIMINACASO = "EliminaCaso";
    public static final String ELIMINAFICHEROALUMNO = "EliminaFicheroAlumno";
    public static final String ELIMINAFICHEROIN = "EliminaFicheroIN";
    public static final String ELIMINAFICHEROOUT = "EliminaFicheroOUT";
    public static final String COPIATEST = "CopiaTest";
    public static final String COPYPRACTICA = "CopyPractica";
    public static final String COPYTEST = "CopyTest";
    public static final String COPIAFICHEROPROPIO = "CopiaFicheroPropio";
    public static final String COPYFICHEROPROPIO = "CopyFicheroPropio";
    public static final String COPIAFICHEROALUMNO = "CopiaFicheroAlumno";
    public static final String COPYFICHEROALUMNO = "CopyFicheroAlumno";
    public static final String COPIACASO = "CopiaCaso";
    public static final String COPYCASO = "CopyCaso";
    public static final String COPYFICHEROIN = "CopyFicheroIN";
    public static final String COPIAFICHEROIN = "CopiaFicheroIN";
    public static final String COPYFICHEROOUT = "CopyFicheroOUT";
    public static final String COPIAFICHEROOUT = "CopiaFicheroOUT";
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
            cs1.add(TEST_EJECUTABLE, (PrimitiveSchema) getSchema(BasicOntology.STRING));

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

            //EstadisticasEvaluacionCaso
            add(new ConceptSchema(ESTADISTICAEVALUACIONCASO), EstadisticaEvaluacionCaso.class);
            ConceptSchema cs12 = (ConceptSchema) getSchema(ESTADISTICAEVALUACIONCASO);
            cs12.add(ESTADISTICAEVALUACIONCASO_ALUMNO, (ConceptSchema) getSchema(ALUMNO));
            cs12.add(ESTADISTICAEVALUACIONCASO_PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            cs12.add(ESTADISTICAEVALUACIONCASO_TEST, (ConceptSchema) getSchema(TEST));
            cs12.add(ESTADISTICAEVALUACIONCASO_CASO, (ConceptSchema) getSchema(CASO));
            cs12.add(ESTADISTICAEVALUACIONCASO_EVALUACION, (PrimitiveSchema) getSchema(BasicOntology.STRING));
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

            //EvaluaPractica
            add(new PredicateSchema(EVALUAPRACTICA), EvaluacionPractica.class);
            PredicateSchema ps6 = (PredicateSchema) getSchema(EVALUAPRACTICA);
            ps6.add(EVALUACIONPRACTICA_TEXTO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            ps6.add(ALUMNO, (ConceptSchema) getSchema(ALUMNO));

            //FicherosAlumno
            add(new PredicateSchema(FICHEROSALUMNO), FicherosAlumno.class);
            PredicateSchema ps7 = (PredicateSchema) getSchema(FICHEROSALUMNO);
            ps7.add(TEST, (ConceptSchema) getSchema(TEST));
            ps7.add(FICHEROALUMNO, (ConceptSchema) getSchema(FICHEROALUMNO));
            
            //FICHEROS OUT
            add(new PredicateSchema(FICHEROSOUT), FicherosOUT.class);
            PredicateSchema ps8 = (PredicateSchema) getSchema(FICHEROSOUT);
            ps8.add(FICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));
            ps8.add(CASO, (ConceptSchema) getSchema(CASO));

            //FICHEROS IN
            add(new PredicateSchema(FICHEROSIN), FicherosIN.class);
            PredicateSchema ps9 = (PredicateSchema) getSchema(FICHEROSIN);
            ps9.add(FICHEROIN, (ConceptSchema) getSchema(FICHEROIN));
            ps9.add(CASO, (ConceptSchema) getSchema(CASO));

            //Casos
            add(new PredicateSchema(CASOS), Casos.class);
            PredicateSchema ps10 = (PredicateSchema) getSchema(CASOS);
            ps10.add(TEST, (ConceptSchema) getSchema(TEST));
            ps10.add(CASO, (ConceptSchema) getSchema(CASO));

            //FicherosPropios
            add(new PredicateSchema(FICHEROSPROPIOS), FicherosPropios.class);
            PredicateSchema ps11 = (PredicateSchema) getSchema(FICHEROSPROPIOS);
            ps11.add(TEST, (ConceptSchema) getSchema(TEST));
            ps11.add(FICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));
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

            add(new AgentActionSchema(CREAPRACTICA), CreaPractica.class);
            AgentActionSchema ps17 = (AgentActionSchema) getSchema(CREAPRACTICA);
            ps17.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(CREATEST), CreaTest.class);
            AgentActionSchema ps18 = (AgentActionSchema) getSchema(CREATEST);
            ps18.add(TEST, (ConceptSchema) getSchema(TEST));
            ps18.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(CREAFICHEROPROPIO), CreaFicheroPropio.class);
            AgentActionSchema ps19 = (AgentActionSchema) getSchema(CREAFICHEROPROPIO);
            ps19.add(TEST, (ConceptSchema) getSchema(TEST));
            ps19.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps19.add(FICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));

            add(new AgentActionSchema(CREAFICHEROIN), CreaFicheroIN.class);
            AgentActionSchema ps20 = (AgentActionSchema) getSchema(CREAFICHEROIN);
            ps20.add(TEST, (ConceptSchema) getSchema(TEST));
            ps20.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps20.add(CASO, (ConceptSchema) getSchema(CASO));
            ps20.add(FICHEROIN, (ConceptSchema) getSchema(FICHEROIN));

            add(new AgentActionSchema(CREAFICHEROOUT), CreaFicheroOUT.class);
            AgentActionSchema ps21 = (AgentActionSchema) getSchema(CREAFICHEROOUT);
            ps21.add(TEST, (ConceptSchema) getSchema(TEST));
            ps21.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps21.add(CASO, (ConceptSchema) getSchema(CASO));
            ps21.add(FICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));

            add(new AgentActionSchema(CREAFICHEROALUMNO), CreaFicheroAlumno.class);
            AgentActionSchema ps22 = (AgentActionSchema) getSchema(CREAFICHEROALUMNO);
            ps22.add(TEST, (ConceptSchema) getSchema(TEST));
            ps22.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps22.add(FICHEROALUMNO, (ConceptSchema) getSchema(FICHEROALUMNO));

            add(new AgentActionSchema(CREACASO), CreaCaso.class);
            AgentActionSchema ps23 = (AgentActionSchema) getSchema(CREACASO);
            ps23.add(TEST, (ConceptSchema) getSchema(TEST));
            ps23.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps23.add(CASO, (ConceptSchema) getSchema(CASO));

            add(new AgentActionSchema(ELIMINAPRACTICA), EliminaPractica.class);
            AgentActionSchema ps24 = (AgentActionSchema) getSchema(ELIMINAPRACTICA);
            ps24.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(ELIMINATEST), EliminaTest.class);
            AgentActionSchema ps25 = (AgentActionSchema) getSchema(ELIMINATEST);
            ps25.add(TEST, (ConceptSchema) getSchema(TEST));
            ps25.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));

            add(new AgentActionSchema(ELIMINAFICHEROPROPIO), EliminaFicheroPropio.class);
            AgentActionSchema ps26 = (AgentActionSchema) getSchema(ELIMINAFICHEROPROPIO);
            ps26.add(TEST, (ConceptSchema) getSchema(TEST));
            ps26.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps26.add(FICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));

            add(new AgentActionSchema(ELIMINAFICHEROIN), EliminaFicheroIN.class);
            AgentActionSchema ps27 = (AgentActionSchema) getSchema(ELIMINAFICHEROIN);
            ps27.add(TEST, (ConceptSchema) getSchema(TEST));
            ps27.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps27.add(CASO, (ConceptSchema) getSchema(CASO));
            ps27.add(FICHEROIN, (ConceptSchema) getSchema(FICHEROIN));

            add(new AgentActionSchema(ELIMINAFICHEROOUT), EliminaFicheroOUT.class);
            AgentActionSchema ps28 = (AgentActionSchema) getSchema(ELIMINAFICHEROOUT);
            ps28.add(TEST, (ConceptSchema) getSchema(TEST));
            ps28.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps28.add(CASO, (ConceptSchema) getSchema(CASO));
            ps28.add(FICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));

            add(new AgentActionSchema(ELIMINAFICHEROALUMNO), EliminaFicheroAlumno.class);
            AgentActionSchema ps29 = (AgentActionSchema) getSchema(ELIMINAFICHEROALUMNO);
            ps29.add(TEST, (ConceptSchema) getSchema(TEST));
            ps29.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps29.add(FICHEROALUMNO, (ConceptSchema) getSchema(FICHEROALUMNO));

            add(new AgentActionSchema(ELIMINACASO), EliminaCaso.class);
            AgentActionSchema ps30 = (AgentActionSchema) getSchema(ELIMINACASO);
            ps30.add(TEST, (ConceptSchema) getSchema(TEST));
            ps30.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps30.add(CASO, (ConceptSchema) getSchema(CASO));

            add(new AgentActionSchema(COPIATEST), CopiaTest.class);
            AgentActionSchema ps31 = (AgentActionSchema) getSchema(COPIATEST);
            ps31.add(TEST, (ConceptSchema) getSchema(TEST));
            ps31.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps31.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps31.add(COPYTEST, (ConceptSchema) getSchema(TEST));

            add(new AgentActionSchema(COPIAFICHEROPROPIO), CopiaFicheroPropio.class);
            AgentActionSchema ps32 = (AgentActionSchema) getSchema(COPIAFICHEROPROPIO);
            ps32.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps32.add(COPYTEST, (ConceptSchema) getSchema(TEST));
            ps32.add(COPYFICHEROPROPIO, (ConceptSchema) getSchema(FICHEROPROPIO));

            add(new AgentActionSchema(COPIAFICHEROALUMNO), CopiaFicheroAlumno.class);
            AgentActionSchema ps33 = (AgentActionSchema) getSchema(COPIAFICHEROALUMNO);
            ps33.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps33.add(COPYTEST, (ConceptSchema) getSchema(TEST));
            ps33.add(COPYFICHEROALUMNO, (ConceptSchema) getSchema(FICHEROALUMNO));

            add(new AgentActionSchema(COPIACASO), CopiaCaso.class);
            AgentActionSchema ps34 = (AgentActionSchema) getSchema(COPIACASO);
            ps34.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps34.add(COPYTEST, (ConceptSchema) getSchema(TEST));
            ps34.add(COPYCASO, (ConceptSchema) getSchema(CASO));
            ps34.add(PRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps34.add(TEST, (ConceptSchema) getSchema(TEST));
            ps34.add(CASO, (ConceptSchema) getSchema(CASO));

            add(new AgentActionSchema(COPIAFICHEROIN), CopiaFicheroIN.class);
            AgentActionSchema ps35 = (AgentActionSchema) getSchema(COPIAFICHEROIN);
            ps35.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps35.add(COPYTEST, (ConceptSchema) getSchema(TEST));
            ps35.add(COPYCASO, (ConceptSchema) getSchema(CASO));
            ps35.add(COPYFICHEROIN, (ConceptSchema) getSchema(FICHEROIN));

            add(new AgentActionSchema(COPIAFICHEROOUT), CopiaFicheroOUT.class);
            AgentActionSchema ps36 = (AgentActionSchema) getSchema(COPIAFICHEROOUT);
            ps36.add(COPYPRACTICA, (ConceptSchema) getSchema(PRACTICA));
            ps36.add(COPYTEST, (ConceptSchema) getSchema(TEST));
            ps36.add(COPYCASO, (ConceptSchema) getSchema(CASO));
            ps36.add(COPYFICHEROOUT, (ConceptSchema) getSchema(FICHEROOUT));

            // *********************************************************************************************
            add(new AgentActionSchema(MODIFICARDATOSBBDD), ModificarDatosBBDD.class);
            AgentActionSchema ps37 = (AgentActionSchema) getSchema(MODIFICARDATOSBBDD);
            ps37.add(ELIMINARDATOSBBDDTIPO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
                
            add(new AgentActionSchema(REGISTRARESTADISTICAEVALUACION), RegistrarEstadisticaEvaluacion.class);
            AgentActionSchema ps38 = (AgentActionSchema) getSchema(REGISTRARESTADISTICAEVALUACION);
            //ps38.add(REGISTRARESTADISTICAEVALUACION_sandra, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            ps38.add(EVALUACIONCASOS, 
            		(ConceptSchema) getSchema(ESTADISTICAEVALUACIONCASO), 0, ObjectSchema.UNLIMITED);

            //EstadisticasEvaluacionCaso
            add(new AgentActionSchema(REGISTRARESTADISTICAENTREGA), RegistrarEstadisticaEntrega.class);
            AgentActionSchema ps39 = (AgentActionSchema) getSchema(REGISTRARESTADISTICAENTREGA);
            ps39.add(REGISTRARESTADISTICAENTREGA_ALUMNO1, (ConceptSchema) getSchema(ALUMNO));
            ps39.add(REGISTRARESTADISTICAENTREGA_ALUMNO2, (ConceptSchema) getSchema(ALUMNO));            
            ps39.add(REGISTRARESTADISTICAENTREGA_PRACTICA, (ConceptSchema) getSchema(PRACTICA));            

        
        
        } catch (OntologyException oe) {
            oe.printStackTrace();
        } catch (Exception oe) {
            oe.printStackTrace();
        }
    }
}





