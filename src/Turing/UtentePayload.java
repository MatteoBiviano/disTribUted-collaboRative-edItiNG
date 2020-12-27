package Turing;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * La classe rappresenta i Dati di contesto associati ad un utente
 * @author Matteo Biviano 543933
 *
 */
public class UtentePayload {
	// Insieme dei documenti che un utente può modificare
	private Set<String> set_doc;
	// insieme dei documenti a cui l'utente è stato invitato a modificare mentre era scollegato
	private Set<String> set_pending;
	/****
	 * private Set<String> instaInvites;	// Insieme dei documenti usato per la gestione degli inviti mentre è online
	 */
	public UtentePayload() {
		this.set_doc=new LinkedHashSet<>();
		this.set_pending=new LinkedHashSet<>();
	}
	/**
	 * Il metodo restituisce l'insieme dei documenti modificabili
	 * @return this.set_doc
	 */
	public synchronized Set<String> getDoc() {
		return this.set_doc;
	}
	/**
	 * Il metodo restituisce l'insieme dei documenti modificabili
	 * @return this.set_doc
	 */
	public synchronized Set<String> getPending() {
		return this.set_pending;
	}	
}
