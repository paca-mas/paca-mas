package es.urjc.ia.paca.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.leap.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import es.urjc.ia.paca.parser.EvaluacionParser;
import es.urjc.ia.paca.util.Testigo;

/** 
Agente proxy entre la interfaz JSP y la plataforma JADE.
Hereda de un agente con funciones para interactuar con la 
plataforma JADE y extiende las funciones propias para interacturar
con JSP.
@author Sergio Saugar Garc�a.
Modificado para pruebas Carlos Sim�n Garc�a
 */
public class InterfazJSP extends Interfaz {

    public String[] doTestPracticasRequest(HttpServletRequest request) {

        ArrayList lista = new ArrayList();

        // Miramos de todos los posibles, los que est�n seleccionados.
        for (int i = 0; i < this.TestPosiblesPractica.length; i++) {
            if (request.getParameter(this.TestPosiblesPractica[i]) != null &&
                    ((String) request.getParameter(this.TestPosiblesPractica[i])).equals("on")) {
                lista.add(this.TestPosiblesPractica[i]);
            }
        }
        ;

        // Ahora los insertamos en el array de salida.
        String[] output = new String[lista.size()];

        if (!lista.isEmpty()) {
            for (int i = 0; i < lista.size(); i++) {
                output[i] = (String) lista.get(i);
            }
        }

        return output;
    }

    /**
    Resuelve los tests que son necesarios para la entrega de una pr�ctica.
     */
    public String[] doTestEntregaFinal(HttpServletRequest req) {
        // En realidad no tratamos la petici�n, sino que siempre
        // querremos todos los ficheros posibles.

        return this.TestPosiblesPractica;
    }

    /**
    Atiende a la petici�n de entrega de una pr�ctica.
     */
    public String doEntregaFinalRequest(HttpServletRequest request) throws IOException {

        java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
        MultipartParser parser = new MultipartParser(request, 1000000);

        // Almacenamos los ficheros en un array de String
        String[] contenidos = new String[ficherosUltimaPractica.length];
        int cont = 0;

        // Login y password del compa�ero.

        String login = "";
        String pass = "";


        // Empezamos a leer.

        Part parte = parser.readNextPart();

        while (parte != null) {
            if (parte.isFile()) {//Es un fichero.
                FilePart filepart = (FilePart) parte;
                InputStream is = filepart.getInputStream();
                StringWriter sw = new StringWriter();

                int tempo = is.read();
                while (tempo != -1) {
                    sw.write(tempo);
                    tempo = is.read();
                }

                //Modificado por ssaugar@platon.escet.urjc.es
                //Codificamos el contenido a base64.
                //contenidos[cont] = sw.toString();

                contenidos[cont] = sw.toString();
                cont++;
            }
            if (parte.isParam()) {//Entonces es un par�metro normal.
                if (parte.getName().equals("login")) {
                    ParamPart parampart = (ParamPart) parte;
                    login = parampart.getStringValue();
                } else {
                    ParamPart parampart = (ParamPart) parte;
                    pass = parampart.getStringValue();
                }
            }

            parte = parser.readNextPart();
        }

        String salida = "";

        salida = doEntregaPractica(contenidos, login, pass);

        return salida;
    }

    //================================================================================
    public String[] doObtieneContenidoRequest(HttpServletRequest request) throws IOException {

        java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
        MultipartParser parser = new MultipartParser(request, 1000000);

        // Almacenamos los ficheros en un array de String
        String[] contenidos = new String[ficherosUltimaPractica.length];
        int cont = 0;

        Part parte = parser.readNextPart();

        while (parte != null) {
            if (parte.isFile()) {
                FilePart filepart = (FilePart) parte;
                InputStream is = filepart.getInputStream();
                StringWriter sw = new StringWriter();

                int tempo = is.read();
                while (tempo != -1) {
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

    public String ParseaSalida(String salida1) {
        String salida = "";
        try {

            EvaluacionParser XMLparser = new EvaluacionParser(salida1, this);
            salida = XMLparser.getEvaluacion();
        } catch (Exception e) {
            salida = "Hubo Errores " + e.getMessage();
        }
        return salida;
    }

    //========================================================================
    public class AutenticaRequestBeha extends OneShotBehaviour {

        private Testigo test2;

        public AutenticaRequestBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.test2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) test2.getParametro();
            String auxUsuario = param1.getParameter("user_id");
            String auxPass = param1.getParameter("password");
            addBehaviour(new EnviaAutenticaBehaviour(this.myAgent, test2, auxUsuario, auxPass));
        }
    }

    public class PideTestRequestBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideTestRequestBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
            String practica = param1.getParameter("practica");
            addBehaviour(new PideTestBeha(this.myAgent, tes2, practica));
        }
    }

    public class PideDescripcionPracticaBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideDescripcionPracticaBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
            String practica = param1.getParameter("practica");
            addBehaviour(new PideDescPractica(this.myAgent, tes2, practica));
        }
    }


        public class ModificarPracticaBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarPracticaBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
            String descripcion = param1.getParameter("descripcion");
            String fechaEntrega = param1.getParameter("fechaEntrega");
            addBehaviour(new ModificarPractica(this.myAgent, tes2, descripcion, fechaEntrega));
        }
    }

    public class ProcesaTestigo extends OneShotBehaviour {

        private Testigo testigo;

        public ProcesaTestigo(Testigo _tes) {

            this.testigo = _tes;

        }

        public void action() {
            while (this.myAgent == null) {
                System.out.println("AGENT ES NULL");
            }

            InterfazJSP agent = (InterfazJSP) this.myAgent;
            while (!agent.FinSetup) {

                System.out.println("FINSETUP");
            }


            if (this.testigo != null) {

                switch (testigo.getOperacion()) {

                    case buscarCorrector:
                        addBehaviour(new CorrectorBehaviour(agent, testigo, "minimos", 100));
                        break;

                    case autenticar:
                        addBehaviour(new AutenticaRequestBeha(agent, testigo));
                        break;

                    case pedirPracticas:
                        addBehaviour(new PidePracticasBehavior(agent, testigo));
                        break;

                    case pedirTests:
                        addBehaviour(new PideTestRequestBeha(agent, testigo));
                        break;

                    case pedirFicheros:
                        testigo.setResultado(agent.doTestPracticasRequest((HttpServletRequest) testigo.getParametro()));
                        break;

                    case insertarFicheros:
                        addBehaviour(new PideFicherosBeha(agent, testigo, (String[]) testigo.getParametro()));
                        break;

                    case corregir:
                        try {
                            String[] contenido = doObtieneContenidoRequest((HttpServletRequest) testigo.getParametro());
                            addBehaviour(new PideCorreccionBeha(agent, testigo, contenido));
                        } catch (IOException e) {

                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;

                    case parsear:
                        testigo.setResultado(ParseaSalida((String) testigo.getParametro()));
                        break;

                    case pedirFicherosFinal:
                        testigo.setResultado((agent.doTestEntregaFinal((HttpServletRequest) testigo.getParametro())));
                        break;

                    case entregarPractica:
                        try {
                            testigo.setResultado((agent.doEntregaFinalRequest((HttpServletRequest) testigo.getParametro())));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;

                    case descripcionPractica:
                        addBehaviour(new PideDescripcionPracticaBeha(agent, testigo));
                        break;

                    case modificarPractica:
                        addBehaviour(new ModificarPracticaBeha(agent,testigo));
                        break;



                    default:
                        testigo.setResultado("-");
                        break;
                }
            }
        }
    }
    public boolean FinSetup = false;

    /**
     * Method to proccess a testigo object. Create new
     * "ProcesaTestigo" behaviour to proccess <b>one</b>
     * "Testigo" only.
     * (Replace the old "Put2Object" method)
     * @param testigo
     */
    public void sendTestigo(Testigo testigo) {

        this.addBehaviour(new ProcesaTestigo(testigo));

    }

    @Override
    protected void setup() {
        super.setup();
        FinSetup = true;
    }

    public boolean isFinSetup() {
        return FinSetup;
    }
}



	
	
	

      

