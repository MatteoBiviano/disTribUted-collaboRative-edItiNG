package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import Turing.ServerTuring;
/**
 * La classe rappresenta il client del progetto Turing
 * @author Matteo Biviano 543933
 *
 */
public class Client {
	
	// Socket per gestire la connessione TCP del client
	private Socket skt_client;
	// Canale per poter lavorare con i file (visualizzarli o scaricarli)
	private SocketChannel skt_files;	
		// Server socket channel utilizzato per il server
		//private ServerSocketChannel skt_server;

	// Dati di contesto per l'interfaccia grafica (Lato Interfaccia Utente)
	private static ClientPayload obj;
	
	/**
	 * Dichiarazioni utili per lettura/scrittura
	 */
	// Input al server
	private BufferedReader input;
	// Stream di output dal server
	private DataOutputStream output;
	
	//Flag per controllare se il server è online o no
	private int isOnline;
	
	/**
	 * Costruttore della classe
	 * @throws IOException 
	 */
	public Client() throws IOException {
		// Ipotizzo a true il server
		isOnline=1;
		try {
			// Inizializzo i vari canali
			skt_files=null;
			skt_client = new Socket("localhost", ServerTuring.DEF_PORT);
			input = new BufferedReader(new InputStreamReader(skt_client.getInputStream()));
			output = new DataOutputStream(skt_client.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// Se il server non è online non posso procedere
			// Setto il booleano di conseguenza
			isOnline=0;
			// Mostro sull'interfaccia il problema
			JOptionPane.showMessageDialog(null, "Server Turing offline. Eseguire per primo <ServerTuring.java> ", "Error", JOptionPane.ERROR_MESSAGE);			
		}
		// Creo il contesto dell'interfaccia grafica
		obj = new ClientPayload(isOnline, input, output, skt_client, skt_files);
	}
	/**
	 * Il metodo si occupa di creare un client tramite l'apposito costruttore, mostrando 
	 * l'interfaccia grafica
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Creo il client
		Client client = new Client();
		// Attivo l'interfaccia grafica
		client.getInterface();
	}
	/**
	 * Il metodo si occupa di mostrare l'interfaccia grafica relativa al client
	 */
	public void getInterface() {
		// Setto la posizione della finestra secondo le coordinate X e Y, partendo dallo 0
		// che si ricorda essere nell'angolo in alto a sinistra dello schermo
		obj.setLocation(400,200);
		// Rendiamo visibile la finestra
		obj.setVisible(true);
	}
}
