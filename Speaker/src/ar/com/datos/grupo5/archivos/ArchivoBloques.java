package ar.com.datos.grupo5.archivos;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.excepciones.UnImplementedMethodException;
import ar.com.datos.grupo5.interfaces.Registro;

/**
 * Clase que permite manipular el arbol B# a nivel fisico.
 * @author LedZeppeling
 */
public class ArchivoBloques extends Directo {
	
	/**
	 * Logger.
	 */
	private static Logger logger  = Logger.getLogger(ArchivoBloques.class);
	
	/**
	 * Tamanio del Bloque en disco.
	 */
	private static int  tamanio = Constantes.SIZE_OF_INDEX_BLOCK;

	/**
	 * Offset del ultimo registro insertado.
	 */
	private long offset = 0L;

	/**
	 * @return El offset del ultimo registro insertado.
	 */
	public final long getOffset() {
		return offset;
	}

	/**
	 * @see Archivo#primero()
	 */
	@Override
	public Registro primero() throws UnImplementedMethodException {
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}

	/**
	 * @see Archivo#siguiente()
	 */
	@Override
	public Registro siguiente() throws UnImplementedMethodException {
		
		throw new UnImplementedMethodException("Funcionalidad no implementada.");
	}
	
	/**
	 * M�todo para recuperar un bloque de un archivo directo por bloques.
	 * @param offset
	 *              La posici�n en la cual empieza el registro buscado.
	 * @return 
	 *        Retorna el registro que se encuentra en la posici�n offset.
	 * @throws IOException .
	 */
	@Override
	public final byte[] leerBloque(final Long offset) throws IOException {
		
		byte[] bufferDato = new byte[Constantes.SIZE_OF_INDEX_BLOCK];
		file.seek(offset * Constantes.SIZE_OF_INDEX_BLOCK);

        file.read(bufferDato, 0, Constantes.SIZE_OF_INDEX_BLOCK);

		return bufferDato;
	}
	
	/**
	 * Metodo para Insertar la cadena en el archivo en el que se est�
	 * trabajando.
	 * 
	 * @param registro
	 *            Es el registro que se va a agregar al archivo.
	 * @param offset
	 *            Es la posici�n en donde comienza el bloque a modificar.
	 * @throws IOException
	 *             Excepcion de extrada/salida.
	 */
	public void insertar(final byte[] bytes, final Long offset) throws IOException {
		// Me posiciono al comienzo del bloque.
		file.seek(offset * Constantes.SIZE_OF_INDEX_BLOCK);
		file.write(bytes, 0, Constantes.SIZE_OF_INDEX_BLOCK);
	}
}