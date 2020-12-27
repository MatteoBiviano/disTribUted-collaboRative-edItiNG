package Turing;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * La classe rappresenta i dati di contesto associati ad un Documento
 * @author Matteo Biviano 543933
 *
 */
public class DocumentoPayload {
	// Insieme di utenti invitati a modificare quel documento
	private Set<String> users;
	// Lista di lock associate al documento (una per ogni sezione)
	private ArrayList<ReentrantLock> locks;		
	// Indirizzo statico di multicast per la chat associata al documento
	private InetAddress chat;			
}
