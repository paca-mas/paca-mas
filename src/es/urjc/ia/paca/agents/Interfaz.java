package es.urjc.ia.paca.agents;

//Paquetes JAVA.
import jade.content.abs.AbsAgentAction;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.image.codec.jpeg.TruncatedFileException;

import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Sort;

import es.urjc.ia.paca.ontology.Alumno;
import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.EvaluacionPractica;
import es.urjc.ia.paca.ontology.FicheroFuentes;
import es.urjc.ia.paca.ontology.Interactua;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.ResultadoEvaluacion;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.Tests;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.EstadoCorrector;
import es.urjc.ia.paca.util.Resultado;
import es.urjc.ia.paca.auth.ontology.Autenticado;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.auth.ontology.Usuario;
import es.urjc.ia.paca.ontology.EntregarPractica;
import es.urjc.ia.paca.ontology.FicheroAlumno;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.FicherosAlumno;
import es.urjc.ia.paca.ontology.ModificaPractica;
import es.urjc.ia.paca.ontology.FicherosPropios;
import es.urjc.ia.paca.ontology.ModificaFicheroPropio;
import es.urjc.ia.paca.ontology.ModificaTest;

/**
Este agente contiene la comunicaci�n necesaria que debe tener un agente usuario
humano para interaccionar con el resto de agentes de �sta plataforma. Es decir,
este agente encapsula los protocolos definidos para el sistema.
@author Sergio Saugar Garc�a

 */
public class Interfaz extends Agent {

    private boolean debug = false;
    private Codec codec = new SLCodec();
    //	Nombre de la ontologia
    private Ontology ontologia = AuthOntology.getInstance();
    private Ontology PACAOntology = pacaOntology.getInstance();
    /**
    ID del alumno
     */
    private String alumnoID;
    /**
    Password del Alumno;
     */
    private String alumnoPass;
    /**
    AID de esta instancia del agente Interfaz.
     */
    private AID miAID;
    /**
    AID del agente corrector de pr�cticas.
     */
    private AID correctorAID;
    /**
    Variable booleana cerrojo para evitar que ninguna funci�n se ejecute
    antes de que termine de ejecutarse el m�todo "setup"
     */
    private boolean terminadoSetup = false;
    /**
    Nombre del agente corrector
     */
    //private String AgenteCorrector = "corrector";
    public AID AgenteCorrector;
    /**
    Nombre del agente autenticador
     */
    private String AgenteAutenticador = "autenticador";
    /**
    El identificador de la �ltima pr�ctica solicitada
     */
    public String ultimaPractica;
    /**
    Array con los nombres de los ficheros de la �ltima
    pr�ctica solicitada
     */
    public String[] ficherosUltimaPractica;
    /**
    Array con los identificadores de los test de la �ltima
    pr�ctica solicitada
     */
    public String[] TestUltimaPractica;
    /**
    Array con los identificadores de los test posibles para una
    pr�ctica solicitada.
     */
    public String[] TestPosiblesPractica;
    /**Identifica al agente gestor de practicas**/
    public String gestorPracticas = "gestor";
    public Practica[] auxPracticas;
    public Test[] auxTest;
    public FicheroPropio[] FicherosPropiosDisponibles;

    /**
    Constructor.
     */
    public Interfaz() {

        miAID = getAID();
    }
    public String usuario;
    public String passUsu;

    public void setUsuarioAux(String usu) {
        this.usuario = usu;
    }

    public void setPasswordAux(String pas) {
        this.passUsu = pas;
    }

    public String getUsuarioAux() {
        return this.usuario;
    }

    public String getPasswordAux() {
        return this.passUsu;
    }

    /**
    Este m�todo asigna el identificador del alumno que representamos.
     */
    public void setAlumnoID(String id) {
        this.alumnoID = id;
    }

    /**
    Este m�todo asigna el password que utilizara el alumno que representamos.
     */
    public void setAlumnoPass(String pass) {
        this.alumnoPass = pass;
    }

    /**
    Este m�todo devuelve el identificador del alumno que representamos.
     */
    public String getAlumnoID() {
        return this.alumnoID;
    }

    /**
    Este m�todo devuelve el password del alumno que representamos.
     */
    public String getAlumnoPass() {
        return this.alumnoPass;
    }

    /**
     * Este m�todo devuelve el Agente Corrector que hemos elegido
     **/
    public synchronized AID getAgenteCorrector() {
        while (this.AgenteCorrector == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return this.AgenteCorrector;
    }

    /**
     * Este m�todo asigna el Agente Corrector que hemos elegido
     */
    public synchronized void setAgenteCorrector(AID agenteCorr) {
        this.AgenteCorrector = agenteCorr;
        notify();
    }
    /**
     * Creamos la tabla Hash "almacenCorrec" para guardar los correctores que hemos utilizado
     */
    public Hashtable<AID, Long> almacenCorrec = new Hashtable<AID, Long>();
    /**
     * Usado guarda el numero de veces que se ha utilizado un corrector
     */
    public Integer usado;

    /**
    Este m�todo se ejecuta al iniciar el agente y es el encargado de configurarlo.
     */
    protected void setup() {

        // Register the codec for the SL0 language
        getContentManager().registerLanguage(codec);

        // Register the ontology used by this application
        getContentManager().registerOntology(AuthOntology.getInstance());
        getContentManager().registerOntology(pacaOntology.getInstance());

        terminadoSetup = true;


    }


    /*
    Antes de ejecutarse ninguna funci�n, debe terminar
    de ejecutarse el procedimiento "setup", as� que pondremos un
    peque�o cerrojo con una variable booleana.
     */
    /**
    Encapsula la comunicaci�n necesaria para realizar el protocolo de autenticaci�n definido.
    @return True o False dependiendo de si se ha producido de forma exitosa o no la autenticacion.
     */
    public final boolean doAutenticacion(String user, String pass) {

        // Cerrojo para que no se ejecute antes de que termine el setup
        while (!terminadoSetup) {
        }

        ACLMessage respuesta = new ACLMessage(ACLMessage.QUERY_IF);
        respuesta.setLanguage(codec.getName());
        respuesta.setOntology(AuthOntology.ONTOLOGY_NAME);
        respuesta.addReceiver(new AID(AgenteAutenticador, AID.ISLOCALNAME));


        // Creamos el predicado.
        Autenticado aut = new Autenticado();
        Usuario user2 = new Usuario();

        // Creamos el usuario
        user2.setUser_id(user);
        user2.setPassword(pass);


        aut.setUsuario(user2);


        try {
            //Mandamos el predicado "aut"
            getContentManager().fillContent(respuesta, aut);
            send(respuesta);
        } catch (CodecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OntologyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Esperamos la respuesta.
        respuesta = blockingReceive();

        try {

            // Ahora iremos cogiendo los objetos creados de la ontologia.
            AbsContentElement listaObj2 = null;
            listaObj2 = getContentManager().extractAbsContent(respuesta);
            String tipoObjetoContenido = listaObj2.getTypeName();

            if (tipoObjetoContenido.equals(SL1Vocabulary.NOT)) {
                return (false);
            } else {
                setAlumnoID(user);
                setAlumnoPass(pass);
                return (true);
            }
        } catch (Exception ex) {
            return (false);
        }
    }

    /**
    Este m�todo encapsula la interacci�n necesaria para obtener los identificadores de las
    pr�cticas disponibles en el sistema.
    @return Devuelve un array con los  nombres de las pr�cticas disponibles.
     */
    /**
    Encapsula la comunicaci�n necesaria para realizar la entrega de la pr�ctica seleccionada por el alumno.
    El agente corrector nos indicar� si la acci�n se ha realizado o no se ha podido realizar.
    Las pr�ctica a entregar est� almacenada en <code>ultimaPractica</code>
    (que se actualiza con cada consulta de ficheros) y los nombres de
    los ficheros en <code>ficherosUltimaPractica</code> (igualmente
    actualizados en cada consulta de ficheros de una pr�ctica).
    @param ContenidoFicheros El contenido de los ficheros de la evaluaci�n.
    Estos ficheros deben tener una correspondencia con los nombre de ficheros en <code>ficherosUltimaPractica</code>
    @param NombreCompanero El nombre del compa�ero con el que forma grupo el actual alumno que utiliza la interfaz
    @param PassCompanero El password del compa�ero con el que forma grupo el actual alumno que utiliza la interfaz
    @return El texto que indica si la acci�n se realiz�
     */
    public final String doEntregaPractica(String[] contenidoFicheros,
            String NombreCompanero,
            String PassCompanero) {

        // Cerrojo para que no se ejecute antes de que termine el setup
        while (!terminadoSetup) {
        }

        System.out.println("----- doEntregraPractica ----------");


        AID aaaAgente = getAgenteCorrector();

        ACLMessage msg_in;
        ACLMessage msg_out = new ACLMessage(ACLMessage.REQUEST);
        //msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
        msg_out.addReceiver(aaaAgente);
        msg_out.setLanguage(codec.getName());
        msg_out.setOntology(pacaOntology.NAME);

        String retornable = new String("Entrega no efectuada");

        /*
        Primero verificamos que el usuario compa�ero
        pueda identificarse (si existe)
        Se considerar� que no se tiene compa�ero cuando
        el login de este est� en blanco
         */

        if (!(NombreCompanero.equals(""))) {
            if (!doAutenticacion(NombreCompanero, PassCompanero)) {
                return "El usuario compa�ero de pr�ctica no pudo autenticarse";
            }
        }


        try {

            System.out.println("TRY ----- doEntregraPractica ----------");

            Action act = new Action();
            //act.setActor(new AID(AgenteCorrector, AID.ISLOCALNAME));
            act.setActor(aaaAgente);

            //EntregarPractica entp= new EntregarPractica();
            es.urjc.ia.paca.ontology.Corrector correc = new es.urjc.ia.paca.ontology.Corrector();
            correc.setId(aaaAgente.getName());
            //correc.setId(AgenteCorrector);

            //entp.setCorrector(correc);
            Practica pract = new Practica();
            pract.setId(ultimaPractica);
            pract.setDescripcion("");
            //entp.setPractica(pract);



            //Convertimos pract a concepto abstracto
            AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);

            //Convertimos correc a concepto abstracto
            AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);




            System.out.println("Predicado EntregarPractica");

            // Los Test
            Test te = new Test();
            Tests tes = new Tests();

            //Lista listaTes = new Lista();
            //Modificacion Carlos
            //ArrayList listaTes = new ArrayList();

            /*
            for (int i=0; i < TestUltimaPractica.length; i++) {
            te = new Test();
            tes = new Tests();
            te.setId(TestUltimaPractica[i]);
            tes.setPractica(pract);
            tes.setTest(te);
            //listaTes.add_0(tes);
            //Modificacion Carlos
            listaTes.add(tes);
            }
             */

            // Incluir los ficheros y rellenado de los ands

            //FicheroFuentes ff;
            //FuentesPrograma fp;
            //Lista listaFP = new Lista();
            //Modificacion Carlos
            //ArrayList listaFP = new ArrayList();

            te = new Test();
            te.setId("?allTest");
            te.setDescripcion("");

            /*
            for (int i=0; i < ficherosUltimaPractica.length; i++) {

            fp = new FuentesPrograma();
            fp.setNombre(ficherosUltimaPractica[i]);

            fp.setContenido(contenidoFicheros[i]);

            ff = new FicheroFuentes();
            ff.setTest(te);
            ff.setFuentesPrograma(fp);

            //listaFP.add_0(ff);
            //Modificacion Carlos
            listaFP.add(ff);
            }*/

            //Interactua inter = new Interactua();
            //FormaGrupoCon formg = new FormaGrupoCon();

            Test[] ts = EncontrarTest();
            List<AbsPredicate> listaTest = ConstruiListaTests(ts, AbsPract);
            List<AbsPredicate> listaFF = ConstruirListaFF(ficherosUltimaPractica, contenidoFicheros, te);

            System.out.println("Listas de Tests y ficheros");

            Alumno al1 = new Alumno(alumnoID, alumnoPass);
            Alumno al2 = new Alumno(NombreCompanero, PassCompanero);
            System.out.println("Alumno2 Nombre: " + NombreCompanero);
            System.out.println("Alumno2 Passwd: " + PassCompanero);

            //Convertimos al1 y al2 a conceptos abstractos
            AbsConcept AbsAl1 = (AbsConcept) PACAOntology.fromObject(al1);
            AbsConcept AbsAl2 = (AbsConcept) PACAOntology.fromObject(al2);

            System.out.println("Alumnos abstractos");

            //Creamos el predicado abstracto Interactua
            AbsPredicate AbsInter = new AbsPredicate(pacaOntology.INTERACTUA);

            //Creamos el predicado abstracto FormaGrupoCon
            AbsPredicate AbsFormg = new AbsPredicate(pacaOntology.FORMAGRUPOCON);

            AID interfaz1 = getAID();
            AbsConcept AbsInterfaz = (AbsConcept) PACAOntology.fromObject(interfaz1);

            AbsInter.set(pacaOntology.ALUMNO, AbsAl1);
            AbsInter.set(BasicOntology.STRING, AbsInterfaz);

            System.out.println("Predicado Interactua");

            //inter.setAlumno(al1);
            //inter.setInterfaz(getAID());

            AbsFormg.set(pacaOntology.ALUMNO, AbsAl1);
            AbsFormg.set(pacaOntology.ALUMNO, AbsAl2);

            System.out.println("Predicado Forma Grupo Con");
            AndBuilder predicado = new AndBuilder();
            predicado.addPredicate(AbsInter);
            predicado.addPredicate(AbsFormg);
            predicado.addPredicates(listaTest);
            predicado.addPredicates(listaFF);


            //Creamos la accion EntregarPractica
            AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.ENTREGARPRACTICA);
            AbsEntp.set(pacaOntology.PRACTICA, AbsPract);
            AbsEntp.set(pacaOntology.CORRECTOR, AbsCorrec);
            AbsEntp.set(pacaOntology.ENTREGA, predicado.getAnd());


            System.out.println("Rellenamos el AgentAction !?");


            //formg.setAlumno1(al1);
            //formg.setAlumno2(al2);

            //And and1 = new And();
            //And and2 = new And();
            //And and3 = new And();
            //And and4 = new And();

            //get1=RIGHT , get0=LEFT
            //AbsPredicate and1 = new AbsPredicate (SL1Vocabulary.AND);
            //AbsPredicate and2 = new AbsPredicate (SL1Vocabulary.AND);
            //AbsPredicate and3 = new AbsPredicate (SL1Vocabulary.AND);
            //AbsPredicate and4 = new AbsPredicate (SL1Vocabulary.AND);

            //and4.set_0(inter);
            //and4.set_1(formg);
            //and4.set(SL1Vocabulary.AND_LEFT, AbsInter);
            //and4.set(SL1Vocabulary.AND_RIGHT, AbsFormg);

            //and3.set_0(listaFP);
            //and3.set_1(and4);
            //and3.set(SL1Vocabulary.AND_LEFT, (AbsObject) listaFP);
            //and3.set(SL1Vocabulary.AND_RIGHT, and4);

            //and2.set_0(listaTes);
            //and2.set_1(and3);
            //and2.set(SL1Vocabulary.AND_LEFT, (AbsObject) listaTes);
            //and2.set(SL1Vocabulary.AND_RIGHT, and3);

            //and1.set_0(entp);
            //and1.set_1(and2);
            //and1.set(SL1Vocabulary.AND_LEFT, AbsEntp);
            //and1.set(SL1Vocabulary.AND_RIGHT, and2);

            //act.setAction(and1);
            //act.setAction((Concept) and1);

            //act.setAction((Concept) predicado);

            //System.out.println("Rellenamos el Action !?");






            //List l_out = new ArrayList();
            //l_out.add(act);


            //fillMsgContent(msg_out, l_out);
            //msg_out.setContentObject((Serializable) l_out);
            getContentManager().fillContent(msg_out, AbsEntp);
            System.out.println("Mandamos el Action");
            send(msg_out);


        } catch (OntologyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CodecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            //return e.toString();
            e.printStackTrace();
        }

        //send(msg_out);

        msg_in = blockingReceive();

        System.out.println("----------------------------------------------------");
        System.out.println(msg_in.toString());
        System.out.println("----------------------------------------------------");


        // Leemos el contenido del mensaje
        try {
            //List l_in = extractMsgContent(msg_in);

            // Si recibimos un INFORM, con un "done", la practica
            //se ha entregado correctamente.

            //Object requestedInfo = l_in.get(0);
            //Ontology o = lookupOntology(msg_in.getOntology());
            //Modificacion Carlos
            //Ontology o = getContentManager().lookupOntology(msg_in.getOntology());
            //String requestedInfoName = o.getRoleName(requestedInfo.getClass());

            AbsContentElement l_in = null;
            l_in = getContentManager().extractAbsContent(msg_in);
            String requestedInfoName = l_in.getTypeName();

            if (msg_in.getPerformative() == ACLMessage.INFORM &&
                    requestedInfoName.equals(BasicOntology.DONE)) {
                retornable = "La pr�ctica se entreg� correctamente";
            } else {
                // La pr�ctica no se ha entregado, porque alguno
                // de los usuarios ya ha entregado la
                // pr�ctica o porque se entrega fuera de plazo

                //And andF = (And) l_in.get(1);
                AbsObject andF = l_in.getAbsObject(SL1Vocabulary.AND);
                //Interactua interac = (Interactua) andF.get_1();
                Interactua interac = (Interactua) andF.getAbsObject(SL1Vocabulary.AND_RIGHT);

                if (interac.getAlumno().getIdentificador().equals("Entrega fuera de plazo")) {
                    retornable = "Error en la entrega de la pr�ctica: " +
                            "La entrega se ha realizado fuera de plazo.";
                } else {
                    retornable = "Error en la entrega de la pr�ctica: " +
                            "El alumno " + interac.getAlumno().getIdentificador() +
                            " ya ha entregado la pr�ctica.";
                }
            }
        } catch (Exception e) {
            return e.toString();
        }

        return retornable;
    }

    //M�todo que nos devuelve el corrector al que pediremos la correci�n
    public AID doBuscarCorrector(String politica, int porcentaje) {



        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Corrector");
        Property prop = new Property();
        prop.setName("Correciones");
        template.addServices(sd);
        AID agenteCorr = null;

        DFAgentDescription[] result;

        try {
            result = DFService.search(this, template);

            int tamano = result.length;
            AID[] agentesCorrectores = new AID[result.length];

            for (int i = 0; i < result.length; ++i) {

                agentesCorrectores[i] = result[i].getName();

            }

            if (politica.equals("aleatoria")) {
                agenteCorr = politicaAleatoria(agentesCorrectores);
            } else {
                agenteCorr = politicaMinimos(result, porcentaje);
            }

            setAgenteCorrector(agenteCorr);


        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return agenteCorr;
    }

    //---------------------- POLITICAS PARA ELECCION DE CORRECTOR
    public Integer getCorrecciones(DFAgentDescription dfAux) {
        Iterator sd1 = dfAux.getAllServices();
        ServiceDescription sd2 = (ServiceDescription) sd1.next();
        Iterator propiedades = sd2.getAllProperties();
        Property prop = (Property) propiedades.next();
        Integer valorProp = Integer.valueOf((String) prop.getValue());

        return valorProp;

    }

    //--------------------- ALEATORIA -------------------------------
    public AID politicaAleatoria(AID[] agentesCorrectores1) {
        Random rand = new Random();
        AID agenteCorr;
        int indiceCorrector = rand.nextInt(100);

        int politica = indiceCorrector % agentesCorrectores1.length;

        agenteCorr = agentesCorrectores1[politica];


        return agenteCorr;

    }

    public static void pintaLista(List lista) {
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(lista.get(i).toString());
        }
        System.out.println(" ================================================================= ");
    }

    @SuppressWarnings("unchecked")
    public AID politicaMinimos(DFAgentDescription[] result1, int porcentaje) {

        AID agenteCorr = null;

        List almacen = new ArrayList();

        Random rand = new Random();



        int correcAelegir = ((result1.length * porcentaje) / 100);

        if (correcAelegir < 1) {
            correcAelegir = 1;
        }


        for (int i = 0; i < result1.length; ++i) {

            //Para buscar propiedades
            DFAgentDescription dfd = result1[i];
            Integer valorProp = getCorrecciones(dfd);

            EstadoCorrector estadoAux = new EstadoCorrector(result1[i].getName(), valorProp);
            almacen.add(estadoAux);

        }

        long loQueTarda;
        long loQueTardaAnterior = 10000000;

        boolean seDeTodos = true;

        Collections.sort(almacen);
        //pintaLista(almacen);

        int i = 0;
        while ((seDeTodos) && (i < correcAelegir)) {
            EstadoCorrector correctorElegido = (EstadoCorrector) almacen.get(i);
            if (almacenCorrec.containsKey(correctorElegido.getIdentificador())) {
                loQueTarda = almacenCorrec.get(correctorElegido.getIdentificador());
                if (loQueTarda <= loQueTardaAnterior) {
                    agenteCorr = correctorElegido.getIdentificador();
                    loQueTardaAnterior = loQueTarda;
                }
            } else {
                seDeTodos = false;
                if (debug) {
                    System.out.println("Nos salimos porque no conocemos... ");
                }
            }
            i++;
        }

        if (!seDeTodos) {
            int indiceCorrector = rand.nextInt(correcAelegir);
            EstadoCorrector correctorElegido = (EstadoCorrector) almacen.get(indiceCorrector);
            agenteCorr = correctorElegido.getIdentificador();
        }

        return agenteCorr;
    }

    private List<AbsPredicate> ConstruiListaTests(Test[] TestsAux, AbsConcept practAux) {
        List<AbsPredicate> listaAux = new ArrayList<AbsPredicate>();

        for (int contador = 0; contador < TestsAux.length; contador++) {

            AbsConcept AbsTest;


            try {
                AbsTest = (AbsConcept) PACAOntology.fromObject(TestsAux[contador]);
                AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
                AbsTests.set(pacaOntology.TEST, AbsTest);
                AbsTests.set(pacaOntology.PRACTICA, practAux);
                listaAux.add(AbsTests);
            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NullPointerException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        return listaAux;
    }

    private List<AbsPredicate> ConstruirListaFF(String[] conjFich, String[] contFich, Test teAux) {
        List<AbsPredicate> listaAux = new ArrayList<AbsPredicate>();
        FuentesPrograma fp = new FuentesPrograma();
        for (int contador = 0; contador < conjFich.length; contador++) {

            AbsConcept AbsFP;
            try {
                fp.setNombre(conjFich[contador]);
                fp.setContenido(contFich[contador]);
                AbsFP = (AbsConcept) PACAOntology.fromObject(fp);
                AbsConcept AbsTest = (AbsConcept) PACAOntology.fromObject(teAux);
                //Creamos el predicado abstracto "FICHEROFUENTS"
                AbsPredicate AbsFicheros = new AbsPredicate(pacaOntology.FICHEROFUENTES);
                AbsFicheros.set(pacaOntology.TEST, AbsTest);
                AbsFicheros.set(pacaOntology.FUENTESPROGRAMA, AbsFP);
                listaAux.add(AbsFicheros);
            } catch (NullPointerException en) {
//				 TODO Auto-generated catch block
                en.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException ex) {
//				 TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e1) {
//				 TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return listaAux;
    }

    //====================== COMPORTAMIENTOS ===========================
    public class CorrectorBehaviour extends OneShotBehaviour {

        private Resultado tes1;
        private String politica;
        private int porcentaje;

        public CorrectorBehaviour(Agent _a, Resultado test, String poli1, int porcentaje_) {
            super(_a);
            this.tes1 = test;
            this.politica = poli1;
            this.porcentaje = porcentaje_;
        }

        public void action() {
            tes1.setResultado(doBuscarCorrector(politica, porcentaje));
        }
    }

    //---------------- COMPORTAMIENTOS PARA LA AUTENTICACION  ----------------------------
    public class EnviaAutenticaBehaviour extends OneShotBehaviour {

        private String usuAux;
        private String passAux;
        private Resultado tes1;

        public EnviaAutenticaBehaviour(Agent _a, Resultado tes, String usuario, String passw) {
            super(_a);
            this.usuAux = usuario;
            this.passAux = passw;
            this.tes1 = tes;
        }

        public void action() {
            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_IF);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(AuthOntology.ONTOLOGY_NAME);
            solicitud.addReceiver(new AID(AgenteAutenticador, AID.ISLOCALNAME));

            // Creamos el predicado.
            Autenticado aut = new Autenticado();
            Usuario user2 = new Usuario();

            // Creamos el usuario
            user2.setUser_id(usuAux);
            user2.setPassword(passAux);

            aut.setUsuario(user2);

            //Metodos auxiliares para no pasar parametros innecesarios al comportamiento "Recibiendo"
            //Guardamos el usuario y el password por si la autenticacion ha sido correcta
            setUsuarioAux(usuAux);
            setPasswordAux(passAux);

            try {
                //Mandamos el predicado "aut"
                getContentManager().fillContent(solicitud, aut);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    //Comportamiento para autentica al usuario
    public class RecibeAutenticacion extends OneShotBehaviour {

        private Resultado tes1;
        private boolean resultado;
        private AbsContentElement mens1;
        private String usu1;
        private String pass1;

        public RecibeAutenticacion(Agent _a, Resultado tes, AbsContentElement mensaje) {
            super(_a);
            this.tes1 = tes;
            this.mens1 = mensaje;
        }

        public void action() {
            try {

                String tipoObjetoContenido = mens1.getTypeName();

                if (tipoObjetoContenido.equals(SL1Vocabulary.NOT)) {
                    resultado = false;
                } else {
                    usu1 = getUsuarioAux();
                    pass1 = getPasswordAux();
                    setAlumnoID(usu1);
                    setAlumnoPass(pass1);
                    resultado = true;
                }
            } catch (Exception ex) {
                resultado = false;
                tes1.setResultadoB(resultado);
            }

            if (debug) {
                System.out.println("Interfaz - Autenticar: " + resultado);
            }

            tes1.setResultadoB(resultado);
            if (debug) {
                System.out.println("Rellenamos el testigo " + tes1.isRelleno());
            }
        }
    }
    //-------------- FIN COMPORTAMIENTOS PARA LA AUTENTICACION ----------------------------

    //-------------- COMPORTAMIENTOS PARA PEDIR LAS PRACTICAS ----------------------------
    public class PidePracticasBehavior extends OneShotBehaviour {

        private Resultado tes;

        public PidePracticasBehavior(Agent _a, Resultado tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {

            AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
            //AID agentCorrec = getAgenteCorrector();
            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(receiver);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            Practica pract = new Practica();
            pract.setId("?practica");
            pract.setDescripcion("");

            try {
                Corrector correc = new Corrector();
                correc.setId(gestorPracticas);

                //Convertimos la practica a un objecto abstracto
                AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(pract);

                //Convertimos el correcto a un objecto abstracto
                AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

                //Creamos el predicado abstracto "CORRIGE"
                AbsPredicate absPredCorr = new AbsPredicate(pacaOntology.CORRIGE);
                absPredCorr.set(pacaOntology.PRACTICA, absPract);
                absPredCorr.set(pacaOntology.CORRECTOR, absCorrec);

                //Creamos la variable que queremos pedir, en este caso "?practica"
                AbsVariable x = new AbsVariable("practica", pacaOntology.PRACTICA);

                //Creamos el IRE con la variable "x" y el predicado "CORRIGE"
                AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                qrall.setVariable(x);
                qrall.setProposition(absPredCorr);

                //Mandamos el mensaje
                getContentManager().fillContent(solicitud, qrall);
                addBehaviour(new RecibeMensajes(myAgent, tes));
                send(solicitud);
            } catch (NullPointerException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (OntologyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public class RecibePracticasBeh extends OneShotBehaviour {

        private Resultado tes1;
        private AbsAggregate practicas1;

        public RecibePracticasBeh(Agent _a, Resultado tes, AbsAggregate practicas) {
            super(_a);
            this.tes1 = tes;
            this.practicas1 = practicas;
        }

        public void action() {
            String[] retornable = new String[0];
            try {
                //Sacamos las practicas que vienen en el mensaje como conceptos abstractos

                Practica p = new Practica();
                retornable = new String[practicas1.size()];
                Practica[] aux = new Practica[practicas1.size()];

                for (int i = 0; i < practicas1.size(); i++) {
                    //Pasamos de concepto abstracto a objeto "real", en este caso son practicas
                    p = (Practica) PACAOntology.toObject(practicas1.get(i));

                    //Comprobamos que haya practicas disponibles
                    if (!(p.getId().equalsIgnoreCase("No hay practicas"))) {
                        retornable[i] = p.getId();
                        //Guardamos todas la practicas para despues saber cual es
                        //la descripcion y la fecha de entrega de la practica elegida
                        aux[i] = p;
                    } else {
                        retornable = new String[0];
                    }
                }
                auxPracticas = aux;
            } catch (OntologyException oe) {
                //printError(myAgent.getLocalName()+" getRoleName() unsucceeded. Reason:" + oe.getMessage());
                System.out.println("Excepcion en ontologia");
                oe.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                System.out.println("Empty message");
                e.printStackTrace();
            } catch (Exception e) {
                // Si obtenemos alguna excepci�n de error, directamente no
                // ofrecemos las practicas ...
                retornable = new String[2];
                retornable[0] = "Error en la obtenci�n de las pr�cticas";
                retornable[1] = practicas1.toString();
            }
            tes1.setResultado(retornable);
        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA PRACTICAS -----------------------

    //-------------------------- COMPORTAMIENTOS PARA TESTS -------------------------------
    public class PideTestBeha extends OneShotBehaviour {

        private Resultado tes1;
        private String IdPractica;

        public PideTestBeha(Agent _a, Resultado tes, String practica) {
            super(_a);
            this.tes1 = tes;
            this.IdPractica = practica;
        }

        public void action() {

            //Nos guardamos la �ltima pr�ctica solicitada
            if (IdPractica == null) {
                IdPractica = ultimaPractica;
            } else {
                ultimaPractica = IdPractica;
            }

            //AID agentCorrec = getAgenteCorrector();
            AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(receiver);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            // Generamos el mensaje para enviar
            Corrige cor = new Corrige();
            Practica pract = new Practica();
            pract.setId(IdPractica);
            pract.setDescripcion("");
            Corrector correc = new Corrector();
            correc.setId(gestorPracticas);
            cor.setPractica(pract);
            cor.setCorrector(correc);

            Test te = new Test();
            te.setId("?test");
            te.setDescripcion("");


            try {
                //Convertimos la practica a un concepto abstracto
                AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(pract);

                //Convertimo el corrector a un concepto abstracto
                AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

                //Convertimos el test a un concepto abstracto
                AbsConcept absTest = (AbsConcept) PACAOntology.fromObject(te);

                //Creamos el predicado CORRIGE de forma abstracta utilizando los conceptos abstractos creados anteriormente
                AbsPredicate absCorrige = new AbsPredicate(pacaOntology.CORRIGE);
                absCorrige.set(pacaOntology.PRACTICA, absPract);
                absCorrige.set(pacaOntology.CORRECTOR, absCorrec);

                //Creamos el predicado TESTS de forma abstracta utilizando los conceptos abstractos creados anteriormente
                AbsPredicate absTests = new AbsPredicate(pacaOntology.TESTS);
                absTests.set(pacaOntology.PRACTICA, absPract);
                absTests.set(pacaOntology.TEST, absTest);

                AbsVariable x = new AbsVariable("test", pacaOntology.TEST);

                AndBuilder predicado = new AndBuilder();
                predicado.addPredicate(absCorrige);
                predicado.addPredicate(absTests);

                AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                qrall.setVariable(x);
                qrall.setProposition(predicado.getAnd());

                getContentManager().fillContent(solicitud, qrall);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);

            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (OntologyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public class RecibeTestBeha extends OneShotBehaviour {

        private Resultado tes1;
        private AbsAggregate tests1;

        public RecibeTestBeha(Agent _a, Resultado tes, AbsAggregate tests_) {
            super(_a);
            this.tes1 = tes;
            this.tests1 = tests_;
        }

        public void action() {

            String[] retornable = new String[0];
            String[] posiblesID = new String[0];
            try {
                //Sacamos los tests que vienen en el mensaje como conceptos abstractos

                Test t = new Test();
                retornable = new String[tests1.size() * 2];
                posiblesID = new String[tests1.size()];
                Test[] aux = new Test[tests1.size()];
                int j = 0;
                for (int i = 0; i < tests1.size(); i++) {
                    //Pasamos de concepto abstracto a objeto "real", en este caso son tests
                    t = (Test) PACAOntology.toObject(tests1.get(i));

                    retornable[j++] = t.getId();
                    posiblesID[i] = t.getId();
                    retornable[j++] = t.getDescripcion();

                    //Guardamos todos los test de la practica para posteriormente saber
                    //la descripcion de los test elegidos
                    aux[i] = t;
                }
                auxTest = aux;
            } catch (Exception e) {
                // Si obtenemos alguna excepci�n de error, directamente no
                // ofrecemos los tests ...
                retornable = new String[2];
                retornable[0] = "Error en la obtenci�n de los tests";
                retornable[1] = "Error:" + e.toString();
            }
            TestPosiblesPractica = posiblesID;
            tes1.setResultado(retornable);


        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA TESTS ---------------------------

    //-------------------------- COMPORTAMIENTOS PARA PEDIR FICHEROS ----------------------
    public class PideFicherosBeha extends OneShotBehaviour {

        private Resultado tes1;
        private String[] IdTest;

        public PideFicherosBeha(Agent _a, Resultado tes, String[] test) {
            super(_a);
            this.tes1 = tes;
            this.IdTest = test;
        }

        public void action() {

            String IdPractica = ultimaPractica;

            // Y nos guardamos los �ltimos test solicitados
            TestUltimaPractica = IdTest;
            AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
            //AID agentCorrec = getAgenteCorrector();

            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(receiver);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            // Leemos la practica y pedimos los ficheros
            // necesarios

            try {

                Corrige cor = new Corrige();
                Practica pract = new Practica();
                pract.setId(IdPractica);
                pract.setDescripcion("");

                Corrector correc = new Corrector();
                correc.setId(gestorPracticas);
                cor.setPractica(pract);
                cor.setCorrector(correc);


                //FuentesPrograma fp = new FuentesPrograma();
                FicheroAlumno fa = new FicheroAlumno();
                fa.setNombre("?nombre");
                fa.setContenido("");




                //Convertimos el corrector a un objecto abstracto
                AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

                //Convertimos la practica a un objecto abstracto
                AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(pract);

                //Creamos el predicado abstracto "CORRIGE"
                AbsPredicate absCorrige = new AbsPredicate(pacaOntology.CORRIGE);
                absCorrige.set(pacaOntology.PRACTICA, absPract);
                absCorrige.set(pacaOntology.CORRECTOR, absCorrec);


                Test[] ts = EncontrarTest();
                //FicheroFuentes ff = new FicheroFuentes();
                FicherosAlumno fas = new FicherosAlumno();
                //Test te1 = new Test();
                //te1.setId(IdTest[0]);
                //te1.setDescripcion("");
                fas.setTest(ts[0]);
                fas.setFicheroAlumno(fa);

                //Pasamos el predicado FicheroFuentes a predicado abstracto
                AbsPredicate absff = (AbsPredicate) PACAOntology.fromObject(fas);

                List<AbsPredicate> listaTest = ConstruiListaTests(ts, absPract);

                AndBuilder constructor = new AndBuilder();

                constructor.addPredicate(absCorrige);
                constructor.addPredicates(listaTest);
                constructor.addPredicate(absff);

                AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                AbsVariable x = new AbsVariable("nombre", pacaOntology.FICHEROSALUMNO);
                qrall.setVariable(x);
                qrall.setProposition(constructor.getAnd());

                getContentManager().fillContent(solicitud, qrall);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);

            } catch (NullPointerException en) {
                // TODO Auto-generated catch block
                en.printStackTrace();
            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class RecibeFicherosBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            String[] retornable = new String[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroAlumno fp;
                retornable = new String[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fp = (FicheroAlumno) PACAOntology.toObject(ficheros1.get(i));
                    retornable[i] = fp.getNombre();
                }

            } catch (Exception e) {
                ficherosUltimaPractica = retornable;
                tes2.setResultado(retornable);
            }

            ficherosUltimaPractica = retornable;
            tes2.setResultado(retornable);

        }
    }
    //	-------------------------- FIN COMPORTAMIENTOS PARA FICHEROS ---------------------------

    //-------------------- COMPORTAMIENTOS PARA PEDIR CORRECCION ------------------
    public class PideCorreccionBeha extends OneShotBehaviour {

        private Resultado tes;
        private String[] contFichAux;

        public PideCorreccionBeha(Agent _a, Resultado tes, String[] contFich) {
            super(_a);
            this.tes = tes;
            this.contFichAux = contFich;
        }

        public void action() {

            AID agentCorrec = getAgenteCorrector();

            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(agentCorrec);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            String retornable = new String("Evaluaci�n no efectuada");


            //Buscamos la practica a traves de su Id, para obtener su descripcion y su fecha de entrega
            Practica pract = EncontrarPractica();
            //pract.setId(ultimaPractica);
            //pract.setDescripcion("");

            Corrector correc = new Corrector();
            correc.setId(agentCorrec.getName());

            Test te = new Test();
            te.setId("?allTest");
            te.setDescripcion("");

            //Convertimos pract en concepto abstracto
            AbsConcept absPract;
            try {
                absPract = (AbsConcept) PACAOntology.fromObject(pract);
                //Convertimos correc en concepto abstracto
                AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

                Alumno al = new Alumno(alumnoID, alumnoPass);
                AbsConcept absal = (AbsConcept) PACAOntology.fromObject(al);
                //Creamos el predicado abstracto Evaluacion Practica
                AbsPredicate AbsEvap = new AbsPredicate(pacaOntology.EVALUAPRACTICA);
                AbsEvap.set(pacaOntology.EVALUACIONPRACTICA_TEXTO, "?evaluacion");
                AbsEvap.set(pacaOntology.ALUMNO, absal);

                //Creamos el predicado abstracto Corrige con AbsPract y AbsCorr
                AbsPredicate absCorrige = new AbsPredicate(pacaOntology.CORRIGE);
                absCorrige.set(pacaOntology.PRACTICA, absPract);
                absCorrige.set(pacaOntology.CORRECTOR, absCorrec);

                Test ts[] = EncontrarTest();
                List<AbsPredicate> listaTest = ConstruiListaTests(ts, absPract);
                List<AbsPredicate> listaFF = ConstruirListaFF(ficherosUltimaPractica, contFichAux, te);

                AndBuilder predicado = new AndBuilder();
                predicado.addPredicate(absCorrige);
                predicado.addPredicates(listaTest);
                predicado.addPredicates(listaFF);
                predicado.addPredicate(AbsEvap);

                //Creamos el IRE para enviar. En este caso IOTA
                AbsIRE qriota = new AbsIRE(SL2Vocabulary.IOTA);
                AbsVariable x = new AbsVariable("evaluacion", pacaOntology.EVALUAPRACTICA);
                qriota.setVariable(x);
                qriota.setProposition(predicado.getAnd());

                getContentManager().fillContent(solicitud, qriota);
                addBehaviour(new RecibeMensajes(myAgent, tes));
                send(solicitud);

            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class RecibeCorrecBeha extends OneShotBehaviour {

        private Resultado tes2;
        private ResultadoEvaluacion salida1;

        public RecibeCorrecBeha(Agent _a, Resultado tes, ResultadoEvaluacion salida) {
            super(_a);
            this.tes2 = tes;
            this.salida1 = salida;
        }

        public void action() {

            String retornable = new String();
            try {
                EvaluacionPractica eva = new EvaluacionPractica();
                eva.setTextoEvaluacion(salida1.getResultadoEvaluacionTexto());
                retornable = eva.getTextoEvaluacion();
                //retornable es el XML

            } catch (Exception e) {
                retornable = e.toString();
            }

            tes2.setResultado(retornable);

        }
    }

    //	-------------------------- FIN COMPORTAMIENTOS PARA CORRECCION ---------------------------
    //-------------------------- COMPORTAMIENTO PARA RECIBIR MENSAJES ---------------------
    public class RecibeMensajes extends Behaviour {

        private Resultado tes1;
        private boolean finalizado = false;
        private MessageTemplate p1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        private MessageTemplate p2 = MessageTemplate.MatchOntology(AuthOntology.ONTOLOGY_NAME);
        private MessageTemplate plantilla = MessageTemplate.and(p1, p2);

        public RecibeMensajes(Agent _a, Resultado tes) {
            super(_a);
            this.tes1 = tes;
        }

        public void action() {

            ACLMessage respuesta = receive();

            if (respuesta != null) {
                try {
                    AbsContentElement listaAbs = null;
                    listaAbs = getContentManager().extractAbsContent(respuesta);
                    String tipoMensaje = listaAbs.getTypeName();

                    if (tipoMensaje.equals("autenticado") | tipoMensaje.equals("not")) {
                        addBehaviour(new RecibeAutenticacion(myAgent, tes1, listaAbs));
                        finalizado = true;
                    } else if (tipoMensaje.equals("=")) {
                        if (listaAbs.getAbsObject(SL1Vocabulary.EQUALS_RIGHT).getTypeName().equals(pacaOntology.RESULTADOEVALUACION)) {
                            ResultadoEvaluacion resultadoEv = (ResultadoEvaluacion) PACAOntology.toObject(listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT));
                            addBehaviour(new RecibeCorrecBeha(myAgent, tes1, resultadoEv));
                            finalizado = true;
                        } else {

                            AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);

                            //Cogemos el primer elemento de la lista
                            AbsConcept primerElem = (AbsConcept) listaElementos.get(0);

                            //Miramos el tipo del primer elemento
                            String tipo = primerElem.getTypeName();
                            if (tipo.equals(pacaOntology.PRACTICA)) {
                                addBehaviour(new RecibePracticasBeh(myAgent, tes1, listaElementos));
                                finalizado = true;
                            } else if (tipo.equals(pacaOntology.TEST)) {
                                addBehaviour(new RecibeTestBeha(myAgent, tes1, listaElementos));
                                finalizado = true;
                            } else if (tipo.equals(pacaOntology.FICHEROPROPIO)){
                                addBehaviour(new RecibeFicherosPropiosBeha(myAgent, tes1, listaElementos));
                                finalizado = true;
                            } else {
                                addBehaviour(new RecibeFicherosBeha(myAgent, tes1, listaElementos));
                                finalizado = true;
                            }
                        }

                    }
                } catch (IndexOutOfBoundsException ei) {
                    // TODO Auto-generated catch block
                    ei.printStackTrace();

                } catch (CodecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OntologyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            // TODO Auto-generated method stub
            return finalizado;
        }
    }
    //-------------------- FIN COMPORTAMIENTO QUE RECIBE MENSAJES ---------------------------

    public Practica EncontrarPractica() {

        int i = 0;
        while (!(ultimaPractica.equalsIgnoreCase(auxPracticas[i].getId()))) {
            i++;
        }

        return auxPracticas[i];
    }

    public Test[] EncontrarTest() {
        int j;
        Test t[] = new Test[TestUltimaPractica.length];
        for (int i = 0; i < TestUltimaPractica.length; i++) {
            j = 0;
            while (!(TestUltimaPractica[i].equalsIgnoreCase(auxTest[j].getId()))) {
                j++;
            }
            t[i] = auxTest[j];
        }
        return t;
    }

    /*********COMPORTAMIENTS PARA LA MODIFICACION DE PRACTICAS************/
    public class PideDescPractica extends OneShotBehaviour {

        private Resultado tes1;
        private String IdPractica;

        public PideDescPractica(Agent _a, Resultado tes, String practica) {
            super(_a);
            this.tes1 = tes;
            this.IdPractica = practica;
        }

        public void action() {
            ultimaPractica = IdPractica;
            Practica p = EncontrarPractica();
            tes1.setResultado(p);
        }
    }

    public class PideDescTest extends OneShotBehaviour {

        private Resultado tes1;
        private String IdTest;

        public PideDescTest(Agent _a, Resultado tes, String test) {
            super(_a);
            this.tes1 = tes;
            this.IdTest = test;
        }

        public void action() {
            String[] auxTest = new String[1];
            auxTest[0] = IdTest;
            TestUltimaPractica = auxTest;
            Test t[] = EncontrarTest();
            tes1.setResultado(t[0]);
        }
    }

    class PideFicherosPropios extends OneShotBehaviour {

        private Resultado tes;
        private String IdTest;

        public PideFicherosPropios(Agent a, Resultado tes, String IdTest) {
            super(a);
            this.tes = tes;
            this.IdTest = IdTest;

        }

        public void action() {

            try {

                //CREAMOS EL MENSAJE
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(pacaOntology.NAME);



                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROS PROPIOS QUE VAMOS A PEDIR
                Practica pt = new Practica(ultimaPractica);
                Test ts = new Test(IdTest);
                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROS PROPIOS
                FicheroPropio fp = new FicheroPropio("?FicheroPropio");
                AbsConcept absfp = (AbsConcept) PACAOntology.fromObject(fp);
                AbsPredicate absfps = new AbsPredicate(pacaOntology.FICHEROSPROPIOS);
                absfps.set(pacaOntology.TEST, absts);
                absfps.set(pacaOntology.FICHEROPROPIO, absfp);

                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(absfps);


                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("ficheroPropio", pacaOntology.FICHEROPROPIO);
                AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                qrall.setVariable(x);
                qrall.setProposition(constructor.getAnd());
                getContentManager().fillContent(msg, qrall);
                addBehaviour(new RecibeMensajes(myAgent, tes));
                myAgent.send(msg);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class RecibeFicherosPropiosBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosPropiosBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            FicheroPropio[] retornable = new FicheroPropio[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroPropio fp;
                retornable = new FicheroPropio[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fp = (FicheroPropio) PACAOntology.toObject(ficheros1.get(i));
                    retornable[i] = fp;
                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }
            FicherosPropiosDisponibles = retornable;
            //ficherosUltimaPractica = retornable;
            tes2.setResultado(retornable);

        }
    }

    public class ModificarPractica extends OneShotBehaviour {

        private Resultado tes1;
        private String descripcion;
        private String fechaEntrega;

        public ModificarPractica(Agent _a, Resultado tes, String descripcion, String fechaEntrega) {
            super(_a);
            this.tes1 = tes;
            this.descripcion = descripcion;
            this.fechaEntrega = fechaEntrega;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = new Practica(ultimaPractica, descripcion, fechaEntrega);
                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaPractica mdp = new ModificaPractica();
                mdp.setPractica(pt);

                Action act = new Action();
                act.setAction(mdp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarTest extends OneShotBehaviour {

        private Resultado tes1;
        private String descripcion;

        public ModificarTest(Agent _a, Resultado tes, String descripcion) {
            super(_a);
            this.tes1 = tes;
            this.descripcion = descripcion;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = new Practica(ultimaPractica);
                Test ts = new Test( TestUltimaPractica[0], descripcion);


                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaTest mts = new ModificaTest();
                mts.setTest(ts);
                mts.setPractica(pt);

                Action act = new Action();
                act.setAction(mts);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarFicherosPropios extends OneShotBehaviour {

        private Resultado tes1;
        private String descripcion;
        private String id_Fichero;

        public ModificarFicherosPropios(Agent _a, Resultado tes, String descripcion, String id_Fichero) {
            super(_a);
            this.tes1 = tes;
            this.descripcion = descripcion;
            this.id_Fichero = id_Fichero;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = new Practica(ultimaPractica);
                Test ts = new Test( TestUltimaPractica[0]);
                FicheroPropio fp = new FicheroPropio(id_Fichero, descripcion);



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaFicheroPropio mfp = new ModificaFicheroPropio();
                mfp.setTest(ts);
                mfp.setFicheroPropio(fp);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

