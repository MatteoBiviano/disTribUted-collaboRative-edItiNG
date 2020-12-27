package Turing;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interfaccia (Stub) di registrazione che utilizza RMI
 * @author Matteo Biviano 543933
 *
 */
public interface RmiInterface extends Remote{
	/**
	 * Il metodo si occupa di gestire una registrazione al servizio
	 * @param username Nome dell'utente
	 * @param password Password associata al nome
	 * @return 1 se è andato tutto a buon fine
	 * 		   0 altrimenti
	 * @throws RemoteException
	 */
	public int registrationHandler(String username, String password) throws RemoteException;

}
