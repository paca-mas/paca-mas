package es.urjc.ia.paca.parser;

public class PaginaXml
{
	
	private String usuario; // identificador usuario
	private String idpractica; // identificador practica
	private String descr_practica; // decripcion practica
	private String idtest; // identificador test
	private String descr_test; // descripcion test
	private String eva_test; // evaluacion test
	private String idcaso; // identificador caso
	private String eva_caso; // evaluacion caso

	public PaginaXml(){}

	public void setusuario (String usuario)
	{
		this.usuario = usuario;
	}
	
	public String getusuario(){
		return usuario;	
	}

	public void setIDPractica (String idpractica)
	{
		this.idpractica = idpractica;
	}
	
	public String getIDPractica(){
		return idpractica;	
	}
	
	public void setDescrPractica (String descr_practica)
	{
		this.descr_practica = descr_practica;
	}
	
	public String getDescrPractica(){
		return descr_practica;	
	}
	
	public void setIDTest (String idtest)
	{
		this.idtest = idtest;
	}
	
	public String getIDTest(){
		return idtest;	
	}

	public void setDescrTest (String descr_test)
	{
		this.descr_test = descr_test;
	}
	
	public String getDescrTest(){
		return descr_test;	
	}
	
	public void setEvaTest (String eva_test)
	{
		this.eva_test = eva_test;
	}
	
	public String getEvaTest(){
		return eva_test;	
	}
	
	public void setIDCaso (String idcaso)
	{
		this.idcaso = idcaso;
	}
	
	public String getIDCaso(){
		return idcaso;	
	}

	public void setEvaCaso (String eva_caso)
	{
		this.eva_caso = eva_caso;
	}
	
	public String getEvaCaso(){
		return eva_caso;	
	}
}
