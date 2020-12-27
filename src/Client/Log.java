package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.JFrame;

/**
 * La classe rappresenta l'implementazione dell'interfaccia di Log del servizio Turing
 */
public class Log extends JFrame{

	private static final long serialVersionUID = 1L;
	
	// Socket per gestire la connessione TCP del client
	private Socket skt_client;
	// Canale per poter lavorare con i file (visualizzarli o scaricarli)
	private SocketChannel skt_files;	
	// Server socket channel utilizzato per il server
	private ServerSocketChannel skt_server;

	// Nome dell'utente attualmente connesso
	private String user;

	// Dati di contesto per i bottoni legati ai listening
	private LogPayload obj;
	/**
	 * Dichiarazioni utili per lettura/scrittura
	 */
	// Stream di output dal server
	private DataOutputStream output;
	// Input al server
	private BufferedReader input;
	
	//private static NotSoGUIListener l;				// Listener degli Inviti Live
	
	/**
	 * Costruttore della classe
	 * @param op Se è =1 allora ho chiamato il costruttore nell'operazione di login
	 * @throws IOException 
	 */
	public Log(String user, int op, Socket skt_client, SocketChannel skt_files) throws IOException {
		System.out.println("[Client] >> Fase di inizializzazione di Log.java");
		this.user=user;
		this.skt_client=skt_client;
		try {
			output=new DataOutputStream(skt_client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Il caso in cui skt_files sia null viene gestito esternamente al costruttore
		this.skt_files=skt_files;
		try {
			input=new BufferedReader(new InputStreamReader(skt_client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
			// Il caso in cui skt_server sia null viene gestito esternamente al costruttore
			//this.skt_server=skt_server;
		/**
		 * Fase di costruzione dell'interfaccia grafica vera e propria
		 */
		// Imponiamo la possibilità di modificare manualmente l'interfaccia
		setLayout(null);
		// Ridimensioniamo la finestra
		setSize(480, 520);
		// Quando chiudiamo la schermata, terminiamo l'operazione
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Inizializziamo i dati di contesto dell'interfaccia
		obj = new LogPayload();
		/**
		 * Aggiungo ad ogni bottone un event listener corrispondente 
		 */
		// Aggiungo l'event listener che si occupa di una richiesta di creazione di un nuovo documento
		obj.getBtNewDoc().addActionListener(obj.newDocumentListener(output, input, user));
		// Aggiungo l'event listener che si occupa di invitare un nuovo utente all'edit del documento
		obj.getBtInvite().addActionListener(obj.inviteUsrListener(output, input, user));
		// Aggiungo l'event listener che si occupa di visualizzare un documento/sezione
		obj.getBtInvite().addActionListener(obj.showDocListener(output, input, user, skt_files, skt_server));
		// Aggiungo l'event listener che si occupa di mostrare la lista dei documenti
		obj.getBtInvite().addActionListener(obj.listDocListener(output, input, user));
		
		if(op==1) {
			/****
			 * ****
			 * DEVO GESTIRE L'OPERAZIONE DI LOGIN
			 */
		}
		
	}
}
