package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Turing.RmiInterface;
import Turing.ServerTuring;
/**
 * La classe rappresenta il contesto del client (La parte interfaccia utente)
 * utilizzato in "Client.java" 
 * @author Matteo Biviano 543933
 */
public class ClientPayload extends JFrame{

	private static final long serialVersionUID = 1L;
	// Background della nostra applicazione
	private JLabel label_background;
	// Logo del servizio Turing
	private JLabel label_logo;

	// Campo dove inserire il nome utente
	private JTextField text_user;
	// Etichetta per il nome utente
	private JLabel label_user;

	// Campo dove inserire la password
	private JPasswordField text_pswd;
	// Etichetta per la password
	private JLabel label_pswd;

	// Bottone per la registrazione
	private JButton button_register;
	// Bottone per il login
	private JButton button_login;
	
	//Flag per controllare se il server è online o no
	private int isOnline;
	
	// Input al server
	private BufferedReader input;
	// Stream di output dal server
	private DataOutputStream output;
	// Socket per gestire la connessione TCP del client
	private Socket skt_client;
	// Canale per poter lavorare con i file (visualizzarli o scaricarli)
	private SocketChannel skt_files;	
	/**
	 * Costruttore della classe
	 * I parametri sono stati inizializzati nella classe "Client.java"
	 * @param isOnline
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public ClientPayload(int isOnline, BufferedReader input, DataOutputStream output, Socket skt_client, SocketChannel skt_files) throws IOException {
		this.isOnline=isOnline;
		this.input=input;
		this.output=output;
		this.skt_client=skt_client;
		this.skt_files=skt_files;
		// Facciamo in modo di terminare l'applicazione quando la finestra viene chiusa
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Facciamo in modo di gestire manualmente tutto il contest grafico
		setLayout(null);
		// Ridimensioniamo la finestra 600x800 in modo che 
		// stilisticamente dia un'idea di possibile applicazione mobile
		setSize(800, 600);
		
		/**
		 * Carichiamo Sfondo e Logotipo
		 */
		// Leggo le immagini dalla cartella
		Image img_background = ImageIO.read(new File("src/Interfaccia/sfondo-blue.png"));
		Image img_logo = ImageIO.read(new File("src/Interfaccia/logo-blue.png"));
		// Creo le etichette
		label_background = new JLabel(new ImageIcon(img_background));
		label_logo = new JLabel(new ImageIcon(img_logo));
		// Settiamo le posizioni
		label_background.setBounds(0, 0, 800, 600);
		label_logo.setBounds(250, 30, 300, 300);
		
		/**
		 * Creiamo i campi dove inserire nome utente e password
		 */
/*		text_user = new JTextField();
		text_pswd = new JPasswordField();
		text_user.setBounds(400, 350, 150, 25);
		text_user.setColumns(10);
		text_pswd.setBounds(400, 380, 150, 25);
		label_user = new JLabel("[Username]");
		label_pswd = new JLabel("[Password]");

		label_user.setBounds(295, 353, 120, 15);
		label_user.setFont(new Font("Futura", Font.BOLD, 18));
		label_user.setForeground(Color.WHITE);
		label_pswd.setBounds(295, 385, 120, 15);
		label_pswd.setForeground(Color.WHITE);
		label_pswd.setFont(new Font("Futura", Font.BOLD, 18));
*/
		text_user = new JTextField();
		text_pswd = new JPasswordField();
		text_user.setBounds(205, 380, 150, 25);
		text_user.setColumns(10);
		text_pswd.setBounds(205, 430, 150, 25);
		
		label_user = new JLabel("Username");
		label_pswd = new JLabel("Password");
		label_user.setBounds(100, 383, 120, 15);
		label_user.setFont(new Font("Futura", Font.BOLD, 18));
		label_user.setForeground(Color.WHITE);
		label_pswd.setBounds(100, 435, 120, 15);
		label_pswd.setForeground(Color.WHITE);
		label_pswd.setFont(new Font("Futura", Font.BOLD, 18));

		
		/**
		 * Carichiamo i bottoni di login e registrazione
		 */
		Image img_login = ImageIO.read(new File("src/Interfaccia/login-btn-blue.png"));
		Image img_register = ImageIO.read(new File("src/Interfaccia/registrazione-btn-blue.png"));
		button_login = new JButton();
		button_login.setIcon(new ImageIcon(img_login));
		button_register = new JButton();
		button_register.setIcon(new ImageIcon(img_register));
		button_login.setBounds(505, 430, 180, 30);
		button_register.setBounds(505, 380, 180, 30);
		
		// Controlliamo che il server sia online prima di installare i listner
		if(isOnline==0) {
			JOptionPane.showMessageDialog(null, "Il Server è Offline. Eseguire <ServerTuring.java> prima di eseguire il client", "Server Offline", JOptionPane.ERROR_MESSAGE);
			// Chiudo il programma
			System.exit(0);
			return;
		}
		/**
		 * Aggiungo ad ogni bottone un event listener corrispondente 
		 */
		// Aggiungo l'event listener che si occupa registrare un nuovo utente
		button_register.addActionListener(registerUsrListener());

		// Aggiungo l'event listener che si occupa effettuare il login dell'utente
		button_login.addActionListener(loginUsrListener());
		/**
		 *  Aggiungo sull'interfaccia le componenti.
		 *  L'ordine di aggiunta delle componenti è stabilito dalla 
		 *  visione a livelli dell'interfaccia
		 *  (= l'ordine influisce sulla visibilità delle componenti)
		 */
		add(label_logo);
		add(label_user);
		add(label_pswd);
		add(button_register);        
		add(button_login);
		add(text_user);
		add(text_pswd);
		add(label_background);
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di registrazione di un Utente
	 * (Tramite RMI)
	 * @return action L'action listener corrispondente
	 */
	public ActionListener registerUsrListener() {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la registrazione di un nuovo utente
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Prima di poter effettuare il login bisogna controllare che 
				 * siano stati inseriti dei campi nelle apposite box
				 */
				if(text_user.getText().length() != 0 && text_pswd.getPassword().length != 0) {
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Inserire dei caratteri in entrambi i campi", "Error", JOptionPane.ERROR_MESSAGE);
				}
				/**
				 * Acquisisco l'username e password del nuovo utente
				 */
				String username = text_user.getText();
				// Controllo la validità dell'utente inserito
				if(isValid(username)==-1) return;
				String password = new String(text_pswd.getPassword());
				// Controllo la validità della password inserita
				if(isValid(password)==-1) return;
		       /**
		        * Uso RMI per gestire la registrazione
		        */
				try {
					register(username, password);
				} catch (RemoteException | NotBoundException e1) {
					e1.printStackTrace();
				}
			}
		};
		return action;
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta login di un utente
	 * @return action L'action listener corrispondente
	 */
	public ActionListener loginUsrListener() {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la registrazione di un nuovo utente
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					output.writeBytes("Login utente" + '\n');
					
					/**
					 * Acquisisco l'username e password dell'utente.
					 * I controlli sono già stati effettuati in fase di registrazione
					 */
					String username = text_user.getText();
					output.writeBytes(username + '\n');				
					String password = new String(text_pswd.getPassword());
					output.writeBytes(password + '\n');	
					
					// Ack di ritorno da parte del server (risultato dell'operazione)
					String ack = input.readLine();
					switch(ack) {
						case "OP_LOGIN":
							Log client = new Log(username, 1, skt_client, skt_files);

							/********
							 * AGGIUNGERE IL BACKGROUD A LOGPAYLOAD
							 */
							
							client.setLocation(400, 200);
							client.setVisible(true);
							// Rilascio le risorse della finestra corrente
							dispose();
							break;
						case "OP_USR_UNKNOWN":
							JOptionPane.showMessageDialog(null, "Campi errati nel login", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						case "OP_USR_LOG":
							JOptionPane.showMessageDialog(null, "Utente già online", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						default:
							JOptionPane.showMessageDialog(null, "Ricevuto un errore da "+ ack);
							// Scriviamolo anche sullo stderr
							System.err.println("Ricevuto un errore: " + ack);
							break;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		};
		return action;
	}
	/**
	 * Il metodo verifica che la stringa possa essere considerata valida
	 * @param text Campo di testo inserito (username o password)
	 * @return 0 se l'username è valido
	 * 			-1 altrimenti
	 */
	public int isValid(String text) {
		if(text.length() == 0) {
			JOptionPane.showMessageDialog(null, "Inserire dei caratteri", "Campo vuoto", JOptionPane.ERROR_MESSAGE);
	        return -1;
	     }
	    if(text.length() > ServerTuring.MAX_CHARACTERS) {
	    	JOptionPane.showMessageDialog(null, "Il campo può contenere fino a " + ServerTuring.MAX_CHARACTERS + " caratteri", "Campo troppo lungo", JOptionPane.WARNING_MESSAGE);
	    	return -1;
	    }
	    if(!text.matches("[a-zA-Z0-9]+")) { 
	        JOptionPane.showMessageDialog(null, "Il campo può contenere solo i seguenti caratteri [a-zA-Z0-9]+ ", "Caratteri non validi", JOptionPane.ERROR_MESSAGE);
	        return -1;
		}
	    return 0;
	}
	
	/**
	 * Il metodo si occupa di azionare RMI per la registrazione di un nuovo utente
	 * @param username Nome dell'utente
	 * @param password Password dell'utente
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 */
	public void register(String username, String password) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ServerTuring.RMI_PORT);
        RmiInterface stubRMI = (RmiInterface) registry.lookup(ServerTuring.RMI_NAME);
        if(stubRMI.registrationHandler(username, password)==0) {
        	// L'utente era già registrato
        	JOptionPane.showMessageDialog(null, "Un utente con questo nome è già registrato", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
        	// Utente registrato correttamente
        	JOptionPane.showMessageDialog(null, "Utente registrato con successo");        
        }
	}
}
