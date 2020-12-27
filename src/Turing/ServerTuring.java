package Turing;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * La classe rappresenta il Server del progetto Turing
 * @author Matteo Biviano 543933
 */
public class ServerTuring {
	/**
	 * Configurazioni del server
	 */
	// Numero di Thread utilizzati
	public static final int DIM_POOL = 6;	
	// Numero massimo di sezioni per file
	public static final int N_SECTIONS = 8;	
	// Porta di registrazione
	public static final int RMI_PORT = 1022;
	// Porta di Default utilizzata
	public static final int DEF_PORT = 6234;	
	// Porta utilizzata per gli inviti
	public static final int SENT_PORT = 6345;	
	// Porta utilizzata per gli indirizzi di multicast
	public static final int MULTI_PORT = 6455;
	// Valore del Timeout
	public static final int TIME_VAL = 998;
	// Nome del Servizio RMI
	public static final String RMI_NAME = "TuringRegister";
	// Lunghezza massima dei campi da poter inserire:
	// - Nome Utente; - Nome Documento; - Password
	public static final int MAX_CHARACTERS = 30;
	//
	private static ServerSocket serverTuring;
	/***************************************************************
	
	/**
	 * Metodo Principale
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * Fase di Inizializzazione del Server
		 */
		System.out.println("[Turing] >> Fase di Inizializzazione del Server");
		serverTuring = new ServerSocket(DEF_PORT, 0, InetAddress.getByName(null));
		//Socket per la connessione
		Socket skt;
		//Inizializzo i dati di contesto
		ServerTuringPayload payload = new ServerTuringPayload();
		ExecutorService threadpool = Executors.newFixedThreadPool(DIM_POOL);
		System.out.println("[Turing] >> Fase di Inizializzazione completata con successo");
		
		/**
		 * Fase di Creazione del servizio di registrazione usando RMI
		 */
		try {
			System.out.println("[Turing] >> Fase di Registrazione");
			registration(payload);
		} catch (AlreadyBoundException e) {
			//Il servizio di registrazione è già attivo
			System.err.println("[ERR] >> Errore nella fase di Registrazione");
			e.printStackTrace();
		}
		//Servizio di registrazione attivo
		System.out.println("[Turing] >> Fase di Registrazione completata con successo");
		
		/**
		 * 
		 * PendingInvites p = new PendingInvites();				// Listener di supporto per gli inviti live
		p.start();
		 * 
		 */
		System.out.println("[Turing] >> Il servizio Turing è online");		
		while(true) {
			//Accetto una connessione
			skt = serverTuring.accept();
			System.out.println("[Turing] >> Ricevuta una nuova richiesta di connessione");
			
			
		}
	}
	/**
	 * Il metodo si occupa di attivare il servizio di registrazione
	 * il quale usa RMI
	 * @throws RemoteException 
	 * @throws AlreadyBoundException 
	 */
	public static void registration(ServerTuringPayload payload) throws RemoteException, AlreadyBoundException {
		//Attivo il servizio di registrazione RMI
		RmiRegistration rmi = new RmiRegistration(payload);
		//Utiliziamo lo stub RMI
		RmiInterface interfRMI = (RmiInterface) UnicastRemoteObject.exportObject(rmi, 0);

        Registry reg = LocateRegistry.createRegistry(RMI_PORT);
        reg.bind(RMI_NAME, interfRMI); 
	}
}
