package Turing;
import java.rmi.RemoteException;
/**
 * La classe rappresenta l'implementazione dell'interfaccia di registrazione
 * @author Matteo Biviano 543933
 *
 */
public class RmiRegistration implements RmiInterface {
	//Dati di contesto
	ServerTuringPayload payload;
	/**
	 * Costruttore della classe
	 * @param payload Dati di contesto
	 */
	public RmiRegistration(ServerTuringPayload payload) {
		this.payload=payload;
	}
	@Override
	public synchronized int registrationHandler(String username, String password) throws RemoteException {
		//Creazione di un nuovo utente
		Utente usr = new Utente(username, password);
		if(payload.isIn(username, usr)==null) {
			//L'utente è stato registrato
			payload.addUser(username);
			return 1;
		}
		else {
			//L'utente era già registrato
			return 0;
		}
	}

}
