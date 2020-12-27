package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JOptionPane;

import Turing.ServerTuring;

/**
 * La classe si occupa di gestire l'arrivo di un messaggio fra client e server
 * @author Matteo Biviano 543933
 */
public class MessageHandler {
	// Operazione catturata
	private String operazione;
	// Nome dell'utente che ha richiesto l'operazione
	private String editor;
	// Nome del documento
	private String documento; 
	// Nome di un eventuale nuovo utente aggiunto come editor
	private String utente;
	// Numero della sezione del documento
	private Integer sezione;
	/**
	 * Costruttore della classe 
	 * @param operazione Operazione catturate
	 * @param editor Utente che ha richiesto l'operazione
	 * @param documento Nome del documento (facoltativo)
	 * @param utente Nome dell'utente aggiunto come editor (facoltativo) 
	 * @param sezioni Numero della sezione del documento (facoltativo)
	 */
	public MessageHandler(String operazione, String editor, String documento, String utente, Integer sezione) {
		this.operazione=operazione;
		this.editor=editor;
		this.documento=documento;
		this.utente=utente;
		this.sezione=sezione;
	}
	/**
	 * Il metodo di occupa di mostrare sull'interfaccia i messaggi di avvenuta operazione o di fallimento
	 * in base al valore della stringa "operazione". Il valore di "operazione" può essere uno dei seguenti:
	 * 
	 * - OP_NEWDOC : creazione del documento avvenuta con successo
	 * - OP_INVITE : invito di un nuovo utente avvenuto con successo
	 * - OP_SHOW : sezione/documento scaricato correttamente
	 * - OP_EDIT_DOC : documento scaricato correttamente, mentre qualcuno lo sta già editando
	 * - OP_LIST : lista dei documenti
	 * - OP_DOC_ALREADY_EXISTS : documento già presente
	 * - OP_USR_ALREADY_EXISTS : l'utente risulta già editor
	 * - OP_DOC_UNKNOWN : documento non esistente
	 * - OP_USR_UNKNOWN : utente non esistente
	 * - OP_USR_ILLEGAL : utente non creatore del documento
	 * 
	 */	
	public void handler(){
		switch(operazione) {
			/**
			 *  Messaggi di successo
			 */
			case "OP_NEWDOC":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> Il documento <" + documento + "> è stato creato con successo");
				break;
			case "OP_INVITE":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> L'utente <" + utente + "> è stato invitato con successo all'edit del documento <"+ documento + ">");
				break;
			case "OP_SHOW":
				if(sezione==ServerTuring.N_SECTIONS)
					// Si richiede tutto il documento
					JOptionPane.showMessageDialog(null, "[Interfaccia] >> Il documento <" + documento + "> è stato scaricato correttamente");
				else
					// Si richiede una determinata sezione
					JOptionPane.showMessageDialog(null, "[Interfaccia] >> La sezione <" + sezione + "> del documento <"+ documento +"> è stata scaricata correttamente");				
				break;			
			case "OP_EDIT_DOC":
				// La sezione non è quella aggiornata, perchè qualcuno ci sta già lavorando
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> Il documento <" + documento + "> è stato scaricato: qualcuno lo sta già editando");
				break;				
			case "OP_LIST":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> La lista di documenti è  <" + documento + ">");
				break;
			case "OP_EDIT":
				/**
				 * GP 626
				 */
			/**
			 *  Messaggi di errore
			 */
			case "OP_DOC_ALREADY_EXISTS":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> Il documento <" + documento + "> è già presente", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			case "OP_USR_ALREADY_EXISTS":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> L'utente <" + utente + "> è già collaboratore per il documento <"+ documento + ">", "Error", JOptionPane.ERROR_MESSAGE);
				break;				
			case "OP_DOC_UNKNOWN":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> Il documento <" + documento + "> non è presente", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			case "OP_USR_UNKNOWN":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> L'utente <" + utente + "> non è registrato", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			case "OP_USR_ILLEGAL":
				JOptionPane.showMessageDialog(null, "[Interfaccia] >> L'utente <" + utente + "> non è creatore del documento <" + documento + ">", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			default:
				//GP "GUILoggedClass" r 479 ??
				JOptionPane.showMessageDialog(null, "Ricevuto un errore da "+ operazione);
				// Scriviamolo anche sullo stderr
				System.err.println("Ricevuto un errore: " + operazione);
				break;
		}
	}
	/**
	 * Il metodo si occupa di gestire il download parziale o totale di un documento
	 * @param skt_files Canale usato per leggere il file (facoltativo)
	 * @param skt_server Server socket utilizzato per permettere nuove accept
	 * @param input input al Server
	 * @return Il nuovo messaggio di operazione
	 * @throws IOException
	 */
	public String download(SocketChannel skt_files, ServerSocketChannel skt_server, BufferedReader input) throws IOException {
		/**
		 * Per scaricare il file: 
		 * 1) Creo le apposite cartelle (se non esistono)
		 * 2) Scarico il file
		 */
		// Creo la cartella "TuringDownloads" dove scaricare il file (se non esiste)
		File downloadDir = new File ("TuringDownloads/");
		if(downloadDir.exists()==false) {
			// Se non esiste la creo
			downloadDir.mkdir();
		}
		// Creo la cartella "TuringDownloads/nome_dell_utente" dove scaricare il file (se non esiste)
		File docDir = new File("TuringDownloads/" + editor);
		if(docDir.exists()==false) {
			// Se non esiste la creo
			docDir.mkdir();
		}				
		// Creo il file da scaricare
		File download;
		if(sezione==ServerTuring.N_SECTIONS) {
			/**
			 *  Devo scaricare il file completo se: come sezione è stato inserito il tetto massimo
			 */
			download = new File("TuringDownloads/" + editor, documento + ".txt");
		}
		else {
			// Devo scaricare solo una determinata sezione
			download = new File("TuringDownloads/" + editor, documento + "_" + sezione + ".txt");
		}
		// Elimino eventuali vecchie copie del file
		if(download.exists()) {
			download.delete();
		}
		// Creo il file
		download.createNewFile();
		/**
		 * Scarico il file
		 */
		FileChannel canaleOut;
		if(sezione==ServerTuring.N_SECTIONS) {
			// Avevo richiesto tutto il file
			canaleOut = FileChannel.open(Paths.get("TuringDownloads/" + editor + "/" + documento + ".txt"), StandardOpenOption.WRITE);
		}
		else {
			// Avevo richiesto una sola sezione
			canaleOut = FileChannel.open(Paths.get("TuringDownloads/" + editor + "/" + documento + "_" + sezione + ".txt"), StandardOpenOption.WRITE);					
		}
		// Flag per continuare il ciclo
		boolean go = true;
		// La capacità del byte buffer è di 1 Mega
		ByteBuffer bb = ByteBuffer.allocateDirect(1048576);
		// Ciclo finchè non ho letto tutti i byte del documento/sezione => uso NIO
		while(go) {
			if(skt_files.read(bb)==-1) {
				// Dovrò fermarmi => ho letto tutto
				go = false;
			}
			// Cambio modalità di utilizzo del bytebuffer
			bb.flip();
			// Scrivo sul canale i byte
			while(bb.hasRemaining()) {
				canaleOut.write(bb);
			}
			bb.clear();
		}
		// Chiudo il canale nio
		skt_files.close();
		skt_files=null;
		// Permetto di accettare nuove richieste
		skt_files = skt_server.accept();
		System.out.println("Il file è stato scaricato");
		return input.readLine();
	}
	/**
	 * Il metodo si occupa di gestire il download parziale o totale di un documento
	 * @param skt_files Canale usato per leggere il file (facoltativo)
	 * @param skt_server Server socket utilizzato per permettere nuove accept
	 * @param input input al Server
	 * @param output Stream di output dal server
	 * @return Il nuovo messaggio di operazione
	 * @throws IOException
	 */
	public String editing(SocketChannel skt_files, ServerSocketChannel skt_server, BufferedReader input, DataOutputStream output) throws IOException {
		/**
		 * Per scaricare il file: 
		 * 1) Creo le apposite cartelle (se non esistono)
		 * 2) Scarico il file
		 */
		// Creo la cartella "TuringEditing" dove scaricare il file (se non esiste)
		File downloadDir = new File ("TuringEditing/");
		if(downloadDir.exists()==false) {
			// Se non esiste la creo
			downloadDir.mkdir();
		}
		// Creo la cartella "TuringEditing/nome_dell_utente" dove scaricare il file (se non esiste)
		File docDir = new File("TuringEditing/" + editor);
		if(docDir.exists()==false) {
			// Se non esiste la creo
			docDir.mkdir();
		}				
		// Creo il file da scaricare
		File download;
		if(sezione==ServerTuring.N_SECTIONS) {
			/**
			 *  Devo scaricare il file completo se: come sezione è stato inserito il tetto massimo
			 */
			download = new File("TuringEditing/" + editor, documento + ".txt");
		}
		else {
			// Devo scaricare solo una determinata sezione
			download = new File("TuringEditing/" + editor, documento + "_" + sezione + ".txt");
		}
		// Elimino eventuali vecchie copie del file
		if(download.exists()) {
			download.delete();
		}
		// Creo il file
		download.createNewFile();
		/**
		 * Scarico il file
		 */
		FileChannel canaleOut;
		// Flag per continuare il ciclo
		boolean go = true;
		canaleOut = FileChannel.open(Paths.get("TuringEditing/" + editor + "/" + documento + "_" + sezione + ".txt"), StandardOpenOption.WRITE);

		// La capacità del byte buffer è di 1 Mega
		ByteBuffer bb = ByteBuffer.allocateDirect(1048576);
		// Ciclo finchè non ho letto tutti i byte del documento/sezione => uso NIO
		while(go) {
			if(skt_files.read(bb)==-1) {
				// Dovrò fermarmi => ho letto tutto
				go = false;
			}
			// Cambio modalità di utilizzo del bytebuffer
			bb.flip();
			// Scrivo sul canale i byte
			while(bb.hasRemaining()) {
				canaleOut.write(bb);
			}
			bb.clear();
		}
		// Chiudo il canale nio
		skt_files.close();
		skt_files=null;
		output.close();
		// Permetto di accettare nuove richieste
		skt_files = skt_server.accept();
		return "OP_EDIT";
	}
	/**
	 * Il metodo si occupa di aggiornare l'operazione richiesta
	 * @param operazione Nuova operazione da inserire
	 */
	public void setOp(String operazione) {
		this.operazione=operazione;
	}
}
