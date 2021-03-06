package ar.com.datos.tests;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ar.com.datos.grupo5.AudioFileManager;
import ar.com.datos.grupo5.AudioManager;
import ar.com.datos.grupo5.Constantes;
import ar.com.datos.grupo5.Diccionario;
import ar.com.datos.grupo5.DocumentsManager;
import ar.com.datos.grupo5.FTRSManager;
import ar.com.datos.grupo5.SimilitudDocumento;
import ar.com.datos.grupo5.UnidadesDeExpresion.IunidadDeHabla;
import ar.com.datos.grupo5.interfaces.InterfazUsuario;
import ar.com.datos.grupo5.parser.ITextInput;
import ar.com.datos.grupo5.parser.TextInterpreter;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;


/**
 * @author LedZeppeling
 *
 */
public class TestCoreListasInvertidas {


	/**
	 * Obtiene todas las posibles palabras a ser lidas. 
	 */
	private ITextInput parser;
	
	/**
	 * Contiene todas las palabras conocidas por el sistema.
	 */
	private Diccionario diccionario;
	
	/**
	 * Permite grabar y reproducir el audio correspondiente a un palabra.
	 */
	private AudioManager audioManager;
	
	/**
	 * 
	 */
	private AudioFileManager audioFileManager;
	
	/**
	 * Conteniene todas las palabras a grabar o a leer del documento ingresado.
	 */
	private Collection<IunidadDeHabla>  contenedor; 

	private FTRSManager ftrsManager;
	
	private ArrayList<SimilitudDocumento> ranking;
	
	/**
	 * Logger para la clase.
	 */
	private static Logger logger = Logger.getLogger(TestCoreListasInvertidas.class);
	
	private long tiempoConsulta;
	
	/**
	 * Busca las palabras a grabar, graba el audio y lo guarda.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @param pathDocumento
	 *            Direcci�n donde se encuentra el archivo a ser examinado.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String load(final String pathDocumento) {
		
		logger.debug("Entre en load");
		
		try {
			Iterator<IunidadDeHabla> iterador;
			// Cargo el parser con el documento en modo aprendizaje
			try {
				//Inicio la grabaci�n del documento.
				contenedor = this.parser.modolecturaYalmacenamiento(pathDocumento);
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			} 
			/*catch (FileNotFoundException e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "El archivo solicitado no existe.";
			}
			*/
			logger.debug("tengo el contenedor de palabras.");
			
			Long offsetRegistroAudio;
			
			if (!abrirArchivo()) {
				return "Intente denuevo";
			}
						
			Long offsetDoc = DocumentsManager.getInstance().getOffsetUltDoc();
		
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {

				elemento = iterador.next();
		//		logger.debug("Termino: " + elemento.getTextoEscrito());
				this.logger.debug(elemento.getTextoEscrito());
				// Si no es StopWord entonces utilizo el Ftrs.
				if (!elemento.isStopWord()) {
					if (elemento.getTextoEscrito().compareTo("fray") == 0) {
						this.logger.debug("Problema se viene");
					}
					ftrsManager.validarTermino(elemento
							.getTextoEscrito(), offsetDoc);
				}

			}
			
			System.out.println("entro a generar listas invertidas");
			this.ftrsManager.generarListasInvertidas();

			//cerrarArchivo(invocador);
		}  catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			
			return "Error inesperado";
		}
		
		logger.debug("Sali de al funcion load");
		return "Las palabras han sido correctamente ingresadas.";
	}

	

	/**
	 * 
	 * @param invocador .
	 */
	public final void help(final InterfazUsuario invocador) {
		//TODO: Actualizar Help
		String mensaje = "Funcion: load \n"
			+ "Caracteristicas: carga un documento para almacenar las palabras "
			+ "desconocidas \n"
			+ "Uso: load <\"path_absoluto_del_documento\"> \n"
			+ "Ej: load \"/home/usuario/Escritorio/prueba.txt\" \n\n"

			+ "Funcion: playDocument \n"
			+ "Caracteristicas: carga un documento reproduciendo las "
			+ "palabras reconocidas \n"
			+ "Uso: playDocument <\"path_absoluto_del_documento\"> \n"
			+ "Ej: playDocument \"/home/usuario/Escritorio/prueba.txt\" \n\n"

			+ "Funcion: playText \n"
			+ "Caracteristicas: reproduce el texto ingresado, omitiendo las "
			+ "palabras que no conoce \n"
			+ "Uso: playText <\"texto ingresado\"> \n"
			+ "Ej: playText \"hola, como estas\" \n\n"

			+ "Funcion: clear \n"
			+ "Caracteristicas: borra la pantalla \n"
			+ "Uso: clear \n"
			+ "Ej: clear \n\n"

			+ "Funcion: help \n"
			+ "Caracteristicas: muestra los comandos disponibles para su ejecuci�n \n"
			+ "Uso: help \n" + "Ej: help \n\n"

			+ "Funcion: query \n" 
			+ "Caracteristicas: Ejecuta una consulta en el indice \n"
			+ "Uso: query <\"texto ingresado\"> \n"
			+ "Ej: query \"hola como estas\" \n\n"

			+ "Funcion: fin \n" 
			+ "Caracteristicas: sale del programa \n"
			+ "Uso: fin \n" + "Ej: fin \n\n";	
		
		this.clear(invocador);
		invocador.mensaje(mensaje);
	}
	
	
	/**
	 * 
	 * @param invocador .
	 */
	public final void clear(final InterfazUsuario invocador) {
		
		String clear = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "\n\n\n\n\n\n";
		
		invocador.mensaje(clear);
	}
	
	
	/**
	 * Da comienzo a la grabaci�n del audio de la palabra en cuesti�n.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @return 0 si la grabaci�n termino bien, -1 si la grabaci�n fue cancelada.
	 */
	private int iniciarGrabacion(final InterfazUsuario invocador) {

		String mensaje;
		String respuesta;
	
		mensaje = "Para iniciar la grabaci�n ingrese la tecla i "
				+ "y luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("i")
				&& !respuesta.equalsIgnoreCase("c")) {
			mensaje = "Comando incorrecto, por favor presione la tecla i.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		
		if (respuesta.equalsIgnoreCase("c")) {
			return -1;
		}
		
		OutputStream byteArray = new ByteArrayOutputStream();
		
		try {
			// Pido grabar el audio
			this.audioManager.grabar(byteArray);
			return 0;
		} catch (Exception e) {
			logger
				.error("Error, no se pudo grabar el audio intentelo nuevamente."
							+ e.getMessage());
			return -2;
		}
		
	}
	
	/**
	 * Termina la grabaci�n del audio, tanto con un final correcto como por una
	 * cancelaci�n del usuario.
	 * 
	 * @param invocador
	 *            Interfaz por la cual se realizan peticiones al usuario y se
	 *            obtienen las respuestas.
	 * @return 0 si la grabaci�n termino bien, -1 si la grabaci�n fue cancelada.
	 */
	private int finalizarGrabacion(final InterfazUsuario invocador) {
		
		String mensaje, respuesta;

		mensaje = "Para detener la grabaci�n ingrese la tecla f y "
				+ "luego enter: ";
		respuesta = invocador.obtenerDatos(mensaje);
		while (!respuesta.equalsIgnoreCase("f")
				&& !respuesta.equalsIgnoreCase("c")) {
			
			mensaje = "Comando incorrecto, por favor presione la tecla f.";
			respuesta = invocador.obtenerDatos(mensaje);
		}
		//Termino la grabacion
		this.audioManager.terminarGrabacion();
		if (respuesta.equalsIgnoreCase("f")) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * Reproduce un documento entero.
	 * 
	 * @param invocador
	 *            .
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	private final String playDocumentInterno(final InterfazUsuario invocador,
			final String indice) {
		
		try {
			
			Iterator<IunidadDeHabla> iterador;
			
			SimilitudDocumento simDocs = this.ranking.get(Integer.parseInt(indice) - 1);
			
			// Mando a parsear el documento y obtengo un collection
			try {

				contenedor = this.parser.modoLecturaDocAlmacenado(simDocs.getDocumento());
				
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			}
			
			if (!abrirArchivo()) {
				
				return "Intente denuevo";
				
			}
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {
				
				elemento = (IunidadDeHabla) iterador.next();
				
				// Si lo encontro sigo en el bucle
				if (elemento.esPronunciable()) {

				Long offsetAudio = this.diccionario
					.buscarPalabra(elemento.getEquivalenteFonetico());

				if (offsetAudio != null) {
					invocador.mensajeSinSalto(elemento.getTextoEscrito() + " ");
					playWord(this.audioFileManager.leerAudio(offsetAudio));
					audioManager.esperarFin();
				}

				}
			}
			logger.debug("Sali de la funcion playDocument");

			invocador.mensaje("");
			//cerrarArchivo(invocador);
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage());
			return "Error en dispositivo de audio.";
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage(), e);
			return "Error inesperado.";
		}
			
		return "Reproduccion finalizada";
	}

	/**
	 * Reproduce un documento entero.
	 * 
	 * @param invocador
	 *            .
	 * @param pathDocumento
	 *            direccion del archivo que va a ser leido.
	 * @return devuelve un mensaje informando el estado final del proceso.
	 */
	public final String playDocument(final InterfazUsuario invocador,
			final String rutaDocumento) {
		
		try {
			
			Iterator<IunidadDeHabla> iterador;
			
			// Mando a parsear el documento y obtengo un collection
			try {
				contenedor = this.parser.modoLecturaSinAlmacenamiento(rutaDocumento, true);
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
				return "Error inesperado, consulte al proveedor del software";
			}
			
			if (!abrirArchivo()) {
				return "Intente denuevo";
			}
			
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
			// Mientras tenga palabras para verificar consulto
			while (iterador.hasNext()) {
				
				elemento = (IunidadDeHabla) iterador.next();
				
				// Si lo encontro sigo en el bucle
				if (elemento.esPronunciable()) {

					Long offsetAudio = this.diccionario
						.buscarPalabra(elemento.getEquivalenteFonetico());
	
					if (offsetAudio != null) {
						invocador.mensajeSinSalto(elemento.getTextoEscrito() + " ");
						playWord(this.audioFileManager.leerAudio(offsetAudio));
						audioManager.esperarFin();
					}
				}
			}
			logger.debug("Sali de la funcion playDocument");

			invocador.mensaje("");
			//cerrarArchivo(invocador);
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage());
			return "Error en dispositivo de audio.";
		} catch (Exception e) {
			logger.error("Error : " + e.getMessage());
			return "Error inesperado.";
		}
			
		return "Reproduccion finalizada";
	}

	/**
	 * Reproduce un texto introducido palabra por palabra.
	 * 
	 * @param textoAReproducir
	 *            Las palabras a leer.
	 * @param invocador .
	 */
	public final void playText(final InterfazUsuario invocador,
			final String textoAReproducir) {
		
		try { 	
			
			Iterator<IunidadDeHabla> iterador;
			// Mando a parsear el documento y obtengo un collection
			try {
	
				contenedor = this.parser.modoLecturaSinAlmacenamiento(textoAReproducir, false);
			} catch (Exception e) {
				logger.error("Error al crear contenedor: " + e.getMessage(), e);
			}
				
			if (!abrirArchivo()) {
				invocador.mensaje("no eexisten los archivos de "
						+ "diccionario o datos ");
			}
	
			IunidadDeHabla elemento;
			iterador = contenedor.iterator();
	
			while (iterador.hasNext()) {
	
				elemento = iterador.next();
				if (elemento.esPronunciable()) {

					Long offsetAudio = this.diccionario
							.buscarPalabra(elemento.getEquivalenteFonetico());
					if (offsetAudio != null) {
						invocador.mensajeSinSalto(elemento.getTextoEscrito()
								+ " ");
						playWord(this.audioFileManager.leerAudio(offsetAudio));
						audioManager.esperarFin();
	
					} else {
						logger.debug("No se encontr� la palabra ["
								+ elemento.getEquivalenteFonetico()
								+ "] en el diccionario.");
					}
				}
			}
			logger.debug("Sali de al funcion playText");
			
		} catch (SimpleAudioPlayerException e) {
			logger.error("Error: " + e.getMessage(), e);
			invocador.mensaje("Error en dispositivo de audio");
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			invocador.mensaje("Error inesperado");
		}
		
		invocador.mensaje("");
	}

	/**
	 * Reproduce la �ltima palabra leida.
	 * 
	 * @throws SimpleAudioPlayerException .
	 */
	public final void playWord() throws SimpleAudioPlayerException {
		this.audioManager.reproducir();
	}

	/**
	 * Reproduce el audio que recibe.
	 * 
	 * @param audioAReproducir
	 *            es el audio que se va a reproducir.
	 * @throws SimpleAudioPlayerException .
	 */
	public final void playWord(final InputStream audioAReproducir)
			throws SimpleAudioPlayerException {
		this.audioManager.reproducir(audioAReproducir);
	}
	
	/**
	 * Constructor de la clase.
	 */
	public TestCoreListasInvertidas() {
		this.audioManager = new AudioManager();
		this.parser = new TextInterpreter();
		this.audioFileManager = new AudioFileManager();
		try {
			this.diccionario = new Diccionario();
			this.ftrsManager = new FTRSManager();
		} catch (Exception e) {
			this.logger.debug("No se ha podido crear el FTRS.");
		}
		this.ranking = null;
	}
	
	/**
	 * Se llama al terminar, para dar un mensaje.
	 * 
	 * @param invocador .
	 */
	public final void quit() {
		try {
			this.cerrarArchivo();
			this.ftrsManager.cerrarArchivos();
			this.diccionario.cerrar();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Cierra los archivos.
	 * 
	 * @param invocador
	 *            .
	 * @return true si pudo cerrar los archivos.
	 */
	private boolean cerrarArchivo() {
		
		try {

			if (this.audioFileManager != null) {
				this.audioFileManager.cerrar();
			}
			return true;

		} catch (Exception e) {
			
			try {
				if (this.audioFileManager != null) {
					this.audioFileManager.cerrar();
				}
			} catch (Exception g) {

			}

			return false;
		}
	}
	
	/**
	 * Abre los archivo.
	 * 
	 * @param invocador .
	 * @return true si pudo abrir los archivos.
	 */
	private boolean abrirArchivo() {

		/*
		 * Abro el archivo para la carga y consulta de los audios
		 */
		try {
			this.audioFileManager.abrir(Constantes.ARCHIVO_AUDIO,
					Constantes.ABRIR_PARA_LECTURA_ESCRITURA);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		logger.debug("Abrio el archivo Audio");
				
		this.ftrsManager.abrirArchivos();
		
		return true;
	}
	

	/**
	 * Metodo que se encarga de administrar la consulta del usuario.
	 * @param invocador 
	 * 			.
	 * @param query cadena de caracteres que debe tener los documentos.
	 * @return
	 * 		Devuelve un mensaje con el estado final del proceso.
	 */
	public final String query(final InterfazUsuario invocador, final String query) {
		Float tiempoFinal = new Float(0.0);
		try {
			this.tiempoConsulta = System.currentTimeMillis();
			Long cantidadDocs = DocumentsManager.getInstance().getCantidadDocsAlmacenados();
			if (cantidadDocs == 0) {
				return "No hay documentos cargados al sistema";
			}
			try {
				ftrsManager.abrirArchivos();
				ranking = this.ftrsManager.consultaRankeada(query, cantidadDocs);
				tiempoFinal = (float)(System.currentTimeMillis() - this.tiempoConsulta) / 1000;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (this.ranking == null) {
				invocador.mensaje("No se encontraron documentos.");
				return tiempoFinal.toString() + " segundos";
			}

			//TODO: pasarle el filtro a las palabras
			  Iterator<SimilitudDocumento> it;
			  it = this.ranking.iterator();
			  SimilitudDocumento nodo;
			  Integer i = 1;
			  invocador.mensaje("Seleccione un de los documentos para ser reproducido:");
			  while (it.hasNext()) {
				  nodo = it.next();
				  String mensaje = i.toString() + ". " + DocumentsManager.getInstance().getNombreDoc(nodo.getDocumento());  
				  invocador.mensaje(mensaje);
				  i++;
			  }
			 int opcion = -1;
			 String documento = "0";
			 
			  while (opcion < 1 || opcion > (i-1)){
				  documento = invocador.obtenerDatos("Elija el documento a reproducir: ");
				  try {
					  opcion = Integer.parseInt(documento);
				  } catch (NumberFormatException e) {
					  invocador.mensaje("Opci�n incorrecta. Intente nuevamente.");
				  }  
			  }
			  
			  
			  this.playDocumentInterno(invocador, documento);
			
			return tiempoFinal.toString() + " segundos";
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			return "Error inesperado.";
		}
	}



	@Override
	protected void finalize() throws Throwable {
		try {

			if (this.audioFileManager != null) {
				this.audioFileManager.cerrar();
			}
			
			
			this.ftrsManager.cerrarArchivos();
			

		} catch (Exception e) {
			
			try {
				if (this.audioFileManager != null) {
					this.audioFileManager.cerrar();
				}
			} catch (Exception g) {
				
			}
			
			this.ftrsManager.cerrarArchivos();
			
		}
		super.finalize();
	}	
	
	public static void main(final String[] args) {
		TestCoreListasInvertidas list = new TestCoreListasInvertidas();
		list.load("./poemas.txt");
		list.quit();
	}
}
