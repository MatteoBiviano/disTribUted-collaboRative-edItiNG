package Turing;
import java.util.Set;

/**
 * La classe rapresenta un utente iscritto al servizo Turing
 * @author Matteo Biviano 543933
 *
 */
public class Utente {
	// Nome dell'utente
	private String username;
	// Password associata all'utente
	private String password;
	//Dati di contesto dell'Utente
	private UtentePayload data;
	/**
	 * Costruttore della classe
	 * @param username Nome dell'utente
	 * @param password Password associata all'utente
	 */
	public Utente(String username, String password) {
		this.username=username;
		this.password=password;
		this.data=new UtentePayload();
	}
	/**
	 * Il metodo restituisce il nome dell'utente
	 * @return this.username
	 */
	public synchronized String getUsername() {
		return this.username;
	}	
	/**
	 * Il metodo restituisce la password associata all'utente
	 * @return this.password
	 */
	public synchronized String getPassword() {
		return this.password;
	}	
	/**
	 * Il metodo restituisce i dati di contesto associati all'utente
	 * @return this.data
	 */
	public synchronized UtentePayload getDati() {
		return this.data;
	}	
}