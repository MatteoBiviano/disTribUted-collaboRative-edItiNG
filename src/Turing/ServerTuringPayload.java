package Turing;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * La classe rappresenta il contenitore dei dati di contesto utili nel server
 * @author Matteo Biviano 543933
 *
 */
public class ServerTuringPayload {
	// Insieme degli utenti online
	private Set<String> onUser;
	// Insieme degli utenti offline
	private Set<String> offUser;
	// Insieme degli indirizzi di multicast
	private Set<InetAddress> indirizzi;
	//Tabella Hash che realizza il collegamento <nome_utente, utente>
	private ConcurrentHashMap<String, Utente> hash_user;
	//Tabella Hash che realizza il collegamento <nome_documento, documento>
	private ConcurrentHashMap<String, Documento> hash_document;
    /** 
     * File dir = new File("Documents/");		// Dove vengono salvati i documenti (lato server)
        if(!dir.exists())
    	dir.mkdir();
     */
	// ********************************
	// Object lockHash;			// Usato per la gestione sincronizzazione tra hash_user e hash_doc 
	// Object lockUser;		// Usato per la gestione sincronizzazione tra uOnline ed uOffline
	// ********************************	
	
	// Costruttore della classe Payload
	public ServerTuringPayload() {
		this.onUser=new HashSet<String>();
		this.offUser=new HashSet<String>();
		this.indirizzi=new HashSet<InetAddress>();
		this.hash_user=new ConcurrentHashMap<String,Utente>();
		this.hash_document=new ConcurrentHashMap<String,Documento>();
	}
	/**
	 * Il metodo restituisce l'insieme degli utenti online
	 * @return this.onUser
	 */
	public Set<String> getOnline(){
		return this.onUser;
	}
	/**
	 * Il metodo restituisce l'insieme degli utenti offline
	 * @return this.offUser
	 */
	public Set<String> getOffline(){
		return this.offUser;
	}
	/**
	 * Il metodo restituisce l'insieme degli indirizzi multicas
	 * @return this.indirizzi
	 */
	public Set<InetAddress> getIndirizzi(){
		return this.indirizzi;
	}
	/**
	 * Il metodo restituisce la tabella hash degli utenti
	 * @return this.hash_user
	 */
	public ConcurrentHashMap<String, Utente> getUsers(){
		return this.hash_user;
	}
	/**
	 * Il metodo restituisce la tabella hash dei documenti
	 * @return this.hash_document
	 */
	public ConcurrentHashMap<String, Documento> getDocs(){
		return this.hash_document;
	}
	/**
	 * Il metodo controlla se un utente è presente negli insiemi
	 * @param username Utente del quale verificare la presenza
	 * @return true se l'utente è presente almeno in uno dei due insiemi
	 * 			false altrimenti
	 */
	public Utente isIn(String username, Utente usr) {
		return hash_user.putIfAbsent(username, usr);
	}
	/**
	 * Il metodo aggiunge un nuovo utente nell'insieme degli utenti offline
	 * @param username Nome dell'utente
	 */
	public void addUser(String username) {
		offUser.add(username);
	}
		
}
