package es.urjc.ia.paca.auth.agents;

//Paquetes JAVA. 
import jade.content.ContentElement;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Hashtable;

import es.urjc.ia.paca.auth.ontology.Autenticado;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.auth.ontology.Usuario;
import es.urjc.ia.paca.auth.util.PasswordParser;

/** 
Este agente se encarga de autenticar un usuario. Para ello, se utiliza un nombre de usuario 
y un password que identifica de forma inequ�voca a un usuario en el sistema.
@author Sergio Saugar Garc�a.
 */
public class AuthAgent extends Agent {

    private boolean ejecucionEnPruebas = true;
    public boolean debug = false;

    //A�adido Carlos
    private Codec codec = new SLCodec();
    /**
    AID de esta instancia del agente de autenticaci�n.
     */
    private AID miAID;

    //Nombre de la ontologia
    private Ontology ontologia = AuthOntology.getInstance();

    protected void setup() {

        miAID = getAID();

        // Register the codec for the SL0 language
        getContentManager().registerLanguage(codec);


        // Register the ontology used by this application
        getContentManager().registerOntology(ontologia);

        if (debug) {
            System.out.println("Comienza el Agente Autenticador");
        }

        // A�adir el comportamiento.
        AutenticarBehaviour autentifica = new AutenticarBehaviour(this);
        addBehaviour(autentifica);
    }

    /**
    Este comportamiento recibe un mensaje QUERY-IF que utiliza la ontologia AuthOntology. 
    Extrae el contenido de dicho mensaje que es el predicado "Autenticado". Extrae el usuario
    que viene dentro de dicho predicado y comprueba que el identificador de usuario y su clave
    est�n registrados. En dicho caso, env�a un mensaje informando de la autenticaci�n. En caso 
    contrario, env�a un mensaje notificando la no autenticaci�n.
     */
    class AutenticarBehaviour extends CyclicBehaviour {

        private MessageTemplate p1 = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
        private MessageTemplate p2 = MessageTemplate.MatchOntology(AuthOntology.ONTOLOGY_NAME);
        /**
        Creamos una plantilla para s�lo responder a los mensajes de nuestra ontolog�a y en concreto,
        a los mensajes de petici�n de autentificaci�n.
         */
        private MessageTemplate plantilla = MessageTemplate.and(p1, p2);
        /**
        Tabla Hash donde se van a almacenar los pares identificadorUsuario-password.
         */
        private Hashtable tablaPass;

        /**
        Constructor del comportamiento. 
        @param a Agente al que pertenece el comportamiento.
         */
        public AutenticarBehaviour(Agent a) {

            super(a);

            if (ejecucionEnPruebas) {
                tablaPass = new Hashtable();
                
                tablaPass.put("ssaugar", "admin");
                tablaPass.put("romartin", "admin");
                tablaPass.put("csimon", "admin");
                tablaPass.put("admin", "admin");
                
            } else {
                //Rellenamos la tabla desde el fichero de passwords, a trav�s del parser de XML.
                PasswordParser parser = new PasswordParser();
                tablaPass = parser.getTabla();
            }
        }

        /**
        M�todo que se ejecuta cada vez que el comportamiento recibe su rodaja de Round-Robin.
         */
        public void action() {

            // Nos bloqueamos hasta recibir un mensaje.
            block();

            ACLMessage msg = receive(plantilla);

            if (msg != null) {
                Evaluar(msg);
            }
        }

        /**
        Comprueba si el user y el password son correctos.
        En caso de que sean correctos devuelve true, falso en caso contrario.
        @param user El identificador del usuario.
        @param password La contrase�a del usuario.
         */
        private void Evaluar(ACLMessage mens) {

            // Capturamos el solicitante del mensaje.
            AID solicitanteAgent = mens.getSender();

            ACLMessage respuesta = mens.createReply();
            respuesta.setPerformative(ACLMessage.INFORM);

            if (debug) {
                System.out.println("------- EVALUAR: Llegada del mensaje--------");
                System.out.println(mens.toString());
                System.out.println("--------FIN Llegada del mensaje-------------");
            }

            try {
                // Extraemos los objetos del mensaje.
                ContentElement contenido = null;
                contenido = getContentManager().extractContent(mens);

                // Capturamos el predicado.
                Autenticado aut = (Autenticado) contenido;

                // Capturamos el usuario.
                Usuario user = aut.getUsuario();

                // Ahora que tenemos el usuario comprobamos que est� en la tabla de passwords.
                if (tablaPass.containsKey(user.getUser_id()) && ((String) tablaPass.get(user.getUser_id())).equals(user.getPassword())) {
                    // Entonces el usuario existe y la password es correcta.
                    // Por lo tanto, mandamos un INFORM con el mismo predicado.
                    respuesta.setContent(mens.getContent());
                    send(respuesta);
                } else {

                    //Creamos la "negaci�n" para negar el predicado al ser el Login incorrecto
                    AbsPredicate not = new AbsPredicate(SLVocabulary.NOT);
                    not.set(SLVocabulary.NOT_WHAT, ontologia.fromObject(contenido));

                    if (debug) {
                        System.out.println("Rellenamos un NOT");
                    }

                    // Por lo tanto, mandamos un INFORM negando el predicado.
                    getContentManager().fillContent(respuesta, not);
                    send(respuesta);

                }

            } catch (java.lang.NullPointerException e) {
                // El mensaje no tiene contenido
                e.printStackTrace();
                System.out.println("El mensaje no tiene contenido");
            } catch (OntologyException oe) {
                //Error en la ontologia (paso de mensajes)
                System.out.println("AuthAgent.java: Ocurrio Excepcion en la ontologia");
            } catch (Exception exc) {
                System.out.println("---------- EXCEPCION --------------------");
                System.err.println(exc);
                System.out.println("---------- FIN EXCEPCION ----------------");
                System.exit(1);
            }

        }
    }
}
