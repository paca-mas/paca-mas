<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@page import="es.urjc.ia.paca.ontology.Practica" %>

<jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session"/>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n de pr&aacute;ctica</title>
        <LINK REL=STYLESHEET TYPE="text/css" HREF="./estilos/estiloPaca.css">
        <SCRIPT TYPE="text/javascript">
            <!--

            salida = true;


            function exit()
            {
                if (salida)
                    window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes');
                return true;
            }


            function comprobar_entrega(form){

                if (!form.EntregaFin.checked){
                    alert("Por favor, seleccione la casilla de entrega de práctica.");
                    salida = true;
                }
                else{
                    desactivarBoton();
                    salida = false;
                }
                return form.EntregaFin.checked;
            }



            function desactivarBoton() {
                document.formentregar.entregar.disabled=true;
                document.formseleccionar.seleccionar.disabled=true;
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">
        <%@ include file="cab.html"%>
        <%@ include file="barra1.html"%>

        <br><br><br>
        <p class="center"  class="color">
			Modificaci&oacute;n de la pr&aacute;ctica.
        </p>
        <br>

        <p class="center">
        <form method="post" name="formpractica" action="peticionTest2.jsp" onsubmit="desactivarBoton();">
            <P class="form"><BR>
            <table style="width: 100%;" class="color">
                <%


Testigo resultado2 = new Testigo();
resultado2.setOperacion(Testigo.Operaciones.descripcionPractica);
resultado2.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
interfaz.sendTestigo(resultado2);

while (!resultado2.isRelleno()) {
}

Practica pt = (Practica) resultado2.getResultado();
                %>
                <tr>
                    <td>
                        <p> <%= pt.getId() %> </p>
                        <p> Descripci&oacute;n: <input type="text" name="descripcion" size="25" value="<%= pt.getDescripcion() %>">  </p>
                        <p> Fecha Entrega: <input type="text" name="fechaEntrega" size="25" value="<%= pt.getFechaEntrega() %>">  </p>
                    </td>
                </tr>
            </table><BR>
            </P>
            <br>
            <p style="text-align: right;">
                <input type="submit" name="seleccionar" value="Cambiar Valores" onclick="javascript:salida=false;">
            </p>
        </form>
    </body>
</html>
