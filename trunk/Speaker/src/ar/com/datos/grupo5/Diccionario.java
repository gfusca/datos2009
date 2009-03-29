package ar.com.datos.grupo5;
import java.io.FileNotFoundException;
import java.io.IOException;

import ar.com.datos.grupo5.interfaces.Archivo;



/**
 * Clase que permite manipular el diccionario
 * @see ar.com.datos.grupo5.interfaces.Archivo
 * @author Diego
 *
 */
public class Diccionario {
	
	/**
	 * Archivo que contendr� las palabras y que ser� manejado por el diccionario.
	 */
	private Archivo archivo;
	
	/**
	 * @param archivo El archivo f�sico diccionario.
	 */
	private void setArchivo(final Archivo archivo) {
		this.archivo = archivo;
	}
	/**
	 * Metodo para cargar el diccionario, accediendo al archivo.
     * @see ar.com.datos.grupo5.interfaces.Archivo#cargar()
	 * @throws FileNotFoundException 
	 */

	public boolean  cargar(final String archivo, final String modo) throws FileNotFoundException {
		return this.archivo.abrir(archivo, modo);
	}
	/**
	 * M�todo que devuelve el archivo referenciado.
     * @see ar.com.datos.grupo5.interfaces.Archivo#getArchivo()
	 */
	public Archivo getArchivo() {
		return archivo;
	}
	/**
	 * M�todo que cierra el diccionario.
     * @see ar.com.datos.grupo5.interfaces.Archivo#cerrar()
	 */
	public void cerrar() throws IOException {
		this.archivo.cerrar();
	}
	
	/**
	 * Busca en el diccionario la palabra que recibe.
	 * @param palabra La palabra que se quiere buscar.
	 * @return null si no encuentra la palabra, si no devuelve elregitro.
	 */
	public final RegistroDiccionario buscarPalabra(final String palabra) {

		RegistroDiccionario registro = new RegistroDiccionario();
		
		//TODO buscar en el archivo secuencial la palabra
		// y armar el registro.
		
		return null;
	}

}
