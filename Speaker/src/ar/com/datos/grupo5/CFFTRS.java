package ar.com.datos.grupo5;

import ar.com.datos.grupo5.registros.RegistroFTRS;

public class CFFTRS implements Comparable<CFFTRS>{

	
	private RegistroFTRS registroAsociado;
	
	private String palabraDecodificada;
	
	
	public CFFTRS() {
		
	}
	

	
	
	public String getPalabraDecodificada() {
		return palabraDecodificada;
	}



	public void setPalabraDecodificada(String palabraDecodificada) {
		this.palabraDecodificada = palabraDecodificada;
	}


	public RegistroFTRS getRegistroAsociado() {
		return registroAsociado;
	}
	
	public void setRegistroAsociado(RegistroFTRS registroAsociado) {
		this.registroAsociado = registroAsociado;
	}
	
	public String getPalabraCodificada(){
		
		return this.registroAsociado.getClaveFrontCoding().getTermino();
	}
	
	public boolean equals (Object obj) {
		
		CFFTRS cfftrs = (CFFTRS) obj;
		return this.palabraDecodificada.equalsIgnoreCase(cfftrs.getPalabraDecodificada()); 
	}

	public int compareTo(CFFTRS o) {
		
		return this.palabraDecodificada.compareTo(o.palabraDecodificada);
	}
}
