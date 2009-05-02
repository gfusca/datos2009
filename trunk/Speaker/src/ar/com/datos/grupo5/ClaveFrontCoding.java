package ar.com.datos.grupo5;

import ar.com.datos.parser.CodificadorFrontCoding;

public class ClaveFrontCoding {
    
	private int caracteresCoincidentes;
	private int longitudTermino; 
	/**
	 * Ver si seria una clave en vez de un String.
	 */
	private String termino;
	
	public ClaveFrontCoding(int caracteres, int longitud, String termino) {
		this.caracteresCoincidentes = caracteres;
		this.longitudTermino = longitud;
		this.termino = termino;
	}
	public void setCaracteresCoincidentes(int caracteresCoincidentes) {
		this.caracteresCoincidentes = caracteresCoincidentes;
	}
	public int getCaracteresCoincidentes() {
		return caracteresCoincidentes;
	}
	public void setLongitudTermino(int longitud) {
		this.longitudTermino = longitud;
	}
	public int getLongitudTermino() {
		return longitudTermino;
	}
	public void setTermino(String termino) {
		this.termino = termino;
	}
	public String getTermino() {
		return termino;
	}
	public String toString() {
		String termino = new String();
		termino += (new Integer(caracteresCoincidentes).toString());
		termino += (new Integer(longitudTermino).toString());
		termino += (this.termino);
		return termino;
	}
	
	 
}
