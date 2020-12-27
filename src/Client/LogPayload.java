package Client;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

// Import per poter accedere ai valori di configurazione
import Turing.ServerTuring;
/**
 * La classe rappresenta l'involucro contenente i dati di contesto utilizzati
 * all'interno di "Log.java". Insieme alla classe "Log.java" realizzano la guida
 * all'utilizzo di Turing, per l'utente
 */
public class LogPayload {
	/**
	 * Immagini utilizzate per i documenti:
	 */
	// Immagine per la creazione del documento
	private Image Img_newDoc;
	// Immagine per mostrare un documento
	private Image Img_showDoc;
	// Immagine per editare un documento
	private Image Img_editDoc;
	// Immagine per mostrare la lista documenti
	private Image Img_listDoc;
	/**
	 * Immagini utilizzate per gli utenti
	 */
	// Immagine per invitare un utente
	private Image Img_inviteUsr;
	// Immagine per scollegare un utente
	private Image Img_logoutUsr;
	/**
	 * Bottoni utilizzati per i documenti:
	 */
	// Bottone per la creazioen del documento
	private JButton B_newDoc;
	// Bottone per mostrare un documento
	private JButton B_showDoc;
	// Bottone per editare un documento
	private JButton B_editDoc;
	// Bottone per mostrare la lista dei documenti
	private JButton B_listDoc;
	/**
	 * Bottoni utilizzati per gli utenti
	 */
	// Bottone per invitare un utente
	private JButton B_inviteUsr;
	// Bottone per scollegare un utente
	private JButton B_logoutUsr;
	/**
	 * Label utilizzato per l'utente
	 */
	private JLabel label;
	/**
	 * Costruttore della classe
	 * @throws IOException 
	 */
	public LogPayload() throws IOException {
		// Istanzio le immagini 
		setImage();
		// Istanzio i bottoni
		setButton();
	}

	/**
	 * Il metodo si occupa di leggere i file "png" con le immagini
	 * e di scalarle con le rispettive dimensioni
	 * @throws IOException
	 */
	public void setImage() throws IOException {
		/**
		 * Istanzio le immagini con le rispettive dimensioni
		 */
		Img_newDoc = ImageIO.read(new File("Bottoni/newDocument.png"));
		Img_newDoc = Img_newDoc.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		Img_showDoc = ImageIO.read(new File("Bottoni/showDocument.png"));
		Img_showDoc = Img_showDoc.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		Img_editDoc = ImageIO.read(new File("Bottoni/editDocument.png"));
		Img_editDoc = Img_editDoc.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		Img_listDoc = ImageIO.read(new File("Bottoni/listDocument.png"));
		Img_listDoc = Img_listDoc.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		Img_inviteUsr = ImageIO.read(new File("Bottoni/inviteUsr.png"));
		Img_inviteUsr = Img_inviteUsr.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		Img_logoutUsr = ImageIO.read(new File("Bottoni/logout.png"));
		Img_logoutUsr = Img_logoutUsr.getScaledInstance(60, 60, Image.SCALE_DEFAULT);	
	}
	/**
	 * Il metodo si occupa di creare i bottoni associandovi le immagini corrispondenti
	 */
	public void setButton() {
		/**
		 * Creo i bottoni
		 */
		B_newDoc = new JButton();
		B_showDoc = new JButton();
		B_editDoc = new JButton();
		B_listDoc = new JButton();
		B_inviteUsr = new JButton();
		B_logoutUsr = new JButton();
		/**
		 * Associo ad ogni bottone una determinata icona
		 */
		B_newDoc.setIcon(new ImageIcon(Img_newDoc));
		B_showDoc.setIcon(new ImageIcon(Img_showDoc));
		B_editDoc.setIcon(new ImageIcon(Img_editDoc));
		B_listDoc.setIcon(new ImageIcon(Img_listDoc));
		B_inviteUsr.setIcon(new ImageIcon(Img_inviteUsr));
		B_logoutUsr.setIcon(new ImageIcon(Img_logoutUsr));
		/**
		 * Setto le dimensioni dei bottoni, come un BoudingBox rappresentato da un rettangolo
		 */
		B_newDoc.setBounds(90,50,110,110);
		B_showDoc.setBounds(90,50,110,110);
		B_editDoc.setBounds(90,50,110,110);
		B_listDoc.setBounds(90,50,110,110);
		B_inviteUsr.setBounds(90,50,110,110);
		B_logoutUsr.setBounds(90,50,110,110);
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di creazione di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente
	 * @return action L'action listener corrispondente
	 */
	public ActionListener newDocumentListener(DataOutputStream output, BufferedReader input, String user) {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la creazione di un documento
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Creo due nuovi campi di testo
				// Campo di testo del nome del documento
				JTextField docText = new JTextField();
				// Campo di testo del numero di sezioni
				JTextField sectionText = new JTextField();
				// Salvo il numero massimo di sezioni
				int maxSezioni = ServerTuring.N_SECTIONS;
				// Numero di sezioni del documento da creare
				int numSezioni = 1;
				// Nome del documento da creare
				String documento = null;
				
				// Creo una TextBox che contiene i precedenti campi
				Object[] textBox = {
					"Nome del documento = ", 
					docText,
					"Numero di Sezioni del documento = ",
					sectionText, "range [2 - "+ maxSezioni +"]"
				};

				while(numSezioni > maxSezioni || documento ==null || !documento.matches("[0-9a-zA-Z]+") || numSezioni <2) {
					/**
					 * Cicliamo finché vale una delle seguenti condizioni:
					 * 1) Numero di sezioni > numero massimo (specificato nel file "ServerTuring.java"
					 * 2) Non è presente il nome del documento
					 * 3) Il nome del documento contiene caratteri non validi
					 * 4) Numero di sezioni < 2 (2 è il numero minimo di sezioni per avere un editing collaborativo)
					 */
					if(JOptionPane.showConfirmDialog(null, textBox, "Creazione di un nuovo Documento", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						// Aggiorno i rispettivi campi, prelevandoli dai Campi di testo
						numSezioni = Integer.parseInt(sectionText.getText());
						documento = docText.getText().trim();
					}
					else return;
				}
				try {
					resultNewDoc(output, input, user, documento, numSezioni);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		};
		return action;
	}
	/**
	 * Il metodo si occupa di gestire le letture/scritture utili durante la gestione del listener
	 * della creazione del documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param documento Nome del documento
	 * @param numSezioni Numero di sezioni del documento
	 * @throws IOException 
	 */
	public void resultNewDoc(DataOutputStream output, BufferedReader input, String user, String documento, int numSezioni) throws IOException {
		// Scrivo la richiesta (Creazione di un nuovo documento
		output.writeBytes("createNewDocument" + '\n');
		// INPUT PER LA RICHIESTA
		// 1) Nome dell'utente
		output.writeBytes(user + '\n');			
		// 2) Documento creato
		output.writeBytes(documento + '\n');
		// 3) Numero di sezioni del documento
		output.writeByte(numSezioni);
		// Ack di ritorno (come risposta) dal server
		String ack = input.readLine();	
		// Creiamo il messaggio
		MessageHandler msg = new MessageHandler(ack, user, documento, null, null);
		// Mostriamo un messaggio diverso in caso ai tipi di ack
		msg.handler();
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di invito di un utente alla modifica di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @return action L'action listener corrispondente
	 */
	public ActionListener inviteUsrListener(DataOutputStream output, BufferedReader input, String user) {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per invitare un nuovo utente all'edit
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Creo due nuovi campi di testo
				// Campo di testo del nome dell'utente
				JTextField userText = new JTextField();
				// Campo di testo del nome del documento
				JTextField docText = new JTextField();
				
				// Nome del documento
				String documento = null;
				// Nome dell'utente da invitare
				String username = null;
				// Creo una TextBox che contiene i precedenti campi
				Object[] textBox = {
					"Nome del documento = ", 
					docText,
					"Numero dell'utente invitato = ",
					userText
				};

				while(username==null || !username.matches("[0-9a-zA-Z]+") || documento ==null ||  !documento.matches("[0-9a-zA-Z]+")) {
					/**
					 * Cicliamo finché vale una delle seguenti condizioni:
					 * 1) Non è presente il nome dell'utente
					 * 2) Il nome dell'utente contiene caratteri non validi 
					 * 3) Non è presente il nome del documento
					 * 4) Il nome del documento contiene caratteri non validi
					 */
					if(JOptionPane.showConfirmDialog(null, textBox, "Invito all'edit di un Documento", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						// Aggiorno i rispettivi campi, prelevandoli dai Campi di testo
						// Nome dell'utente da invitare
						username = userText.getText().trim();
						// Nome del documento 
						documento = docText.getText().trim();
					}
					else return;
				}
				try {
					resultInviteUsr(output, input, user, documento, username);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		};
		return action;
	}
	/**
	 * Il metodo si occupa di gestire le letture/scritture utili durante la gestione del listener
	 * della creazione del documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param documento nome del documento
	 * @param username nome del nuovo utente aggiunto
	 * @throws IOException 
	 */
	public void resultInviteUsr(DataOutputStream output, BufferedReader input, String user, String documento, String username) throws IOException {
		// Scrivo la richiesta (Creazione di un nuovo documento
		output.writeBytes("inviteUser" + '\n');
		// INPUT PER LA RICHIESTA
		// 1) Nome dell'utente (owner)
		output.writeBytes(user + '\n');			
		// 2) Nome del documento
		output.writeBytes(documento + '\n');
		// 3) Nome del nuovo utente aggiunto all'edit
		output.writeBytes(username + '\n');
		// Ack di ritorno (come risposta) dal server
		String ack = input.readLine();	
		// Creiamo il messaggio
		MessageHandler msg = new MessageHandler(ack, user, documento, username, null);
		// Mostriamo un messaggio diverso in caso ai tipi di ack
		msg.handler();
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di visione di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server Server socket usato per il server
	 * @return action L'action listener corrispondente
	 */
	public ActionListener showDocListener(DataOutputStream output, BufferedReader input, String user, SocketChannel skt_files, ServerSocketChannel skt_server) {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la visualizzazione di un documento/sezione
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Creo due nuovi campi di testo
				// Campo di testo del nome del documento
				JTextField docText = new JTextField();
				// Campo di testo del numero di sezioni
				JTextField sectionText = new JTextField();
				
				// Salvo il numero massimo di sezioni
				int maxSezioni = ServerTuring.N_SECTIONS;
				// Numero della sezione del documento richiesta
				int numSezione = -1;
				// Nome del documento da creare
				String documento = null;
				
				// Creo una TextBox che contiene i precedenti campi
				Object[] textBox = {
					"Nome del documento = ", 
					docText,
					"Numero di Sezioni del documento = ",
					sectionText, "range [0 - "+ maxSezioni +"]"
				};

				while(numSezione > maxSezioni || documento ==null || !documento.matches("[0-9a-zA-Z]+") || numSezione <0) {
					/**
					 * Cicliamo finché vale una delle seguenti condizioni:
					 * 1) Numero di sezioni > numero massimo (specificato nel file "ServerTuring.java"
					 * 2) Non è presente il nome del documento
					 * 3) Il nome del documento contiene caratteri non validi
					 * 4) Numero di sezione < 0
					 */
					if(JOptionPane.showConfirmDialog(null, textBox, "Mostro il documento o una sezione", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						// Aggiorno i rispettivi campi, prelevandoli dai Campi di testo
						documento = docText.getText().trim();
						if(sectionText.getText().length()>0) {
							// Se ho inserito una sezione
							numSezione = Integer.parseInt(sectionText.getText());
						}
					}
					else return;
				}
				try {
					resultShowDoc(output, input, user, documento, numSezione, skt_files, skt_server);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		};
		return action;
	}
	/**
	 * Il metodo si occupa di gestire le letture/scritture utili durante la gestione del listener
	 * della creazione del documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param documento Nome del documento
	 * @param numSezione Sezione del documento richiesta
	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server server socket usato per il server
	 * @throws IOException 
	 */
	public void resultShowDoc(DataOutputStream output, BufferedReader input, String user, String documento, int numSezione, SocketChannel skt_files, ServerSocketChannel skt_server) throws IOException {
		// Scrivo la richiesta (Creazione di un nuovo documento
		output.writeBytes("showDoc" + '\n');
		// INPUT PER LA RICHIESTA
		// 1) Nome dell'utente
		output.writeBytes(user + '\n');			
		// 2) Documento creato
		output.writeBytes(documento + '\n');
		// 3) Numero di sezione del documento richiesta
		output.writeByte(numSezione);
		// Ack di ritorno (come risposta) dal server
		String ack = input.readLine();	
		// Creiamo il messaggio
		MessageHandler msg = new MessageHandler(ack, user, documento, null, numSezione);
		// Controlliamo il messaggio in caso di aggiornamento
		if(ack=="OP_SHOW") {
			// Scarico il file
			String op = msg.download(skt_files, skt_server, input);
			// Setto l'eventuale nuova operazione
			msg.setOp(op);		
		}
		// Restituisco un messaggio a seconda dell'operazione
		msg.handler();
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di visione di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @return action L'action listener corrispondente
	 */
	public ActionListener listDocListener(DataOutputStream output, BufferedReader input, String user){
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la visualizzazione della lista di documenti
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Lista dei documenti
				String listaDoc = null;
				String doc = null;
				// Contatore del numero di documenti (indicati dai '\n' ricevuti)
				int cont = 0;
				// Scrivo la richiesta (Creazione di un nuovo documento
				try {
					output.writeBytes("listDoc" + '\n');
					// INPUT PER LA RICHIESTA
					// 1) Nome dell'utente
					output.writeBytes(user + '\n');	
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				while(!doc.equals("Lista vuota") && cont < 2) {
					// Mi occupo di costruire la lista di documenti
					try {
						doc = input.readLine();
					} catch (IOException ex1) {
						ex1.printStackTrace();
					}
					if(doc.length()>=1) {
						// Letto un nuovo documento
						cont = 0;
						if(listaDoc!=null) {
							listaDoc = listaDoc + doc + '\n';
						}
						else {
							listaDoc = doc + '\n';
						}
					}
					else {
						// Ho ricevuto un '\n'
						cont++;
						listaDoc = listaDoc + '\n';
					}
				}
				String operazione = null;
				if(listaDoc!= null) {
					operazione = "OP_LIST";
				}
				else {
					operazione = "OP_ERR";
				}
				// Creiamo il messaggio
				MessageHandler msg = new MessageHandler(operazione, user, listaDoc, null, null);
				// Restituisco un messaggio a seconda dell'operazione
				msg.handler();
			}
		};
		return action;
	}
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di creazione di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente
 	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server server socket usato per il server
	 * @return action L'action listener corrispondente
	 */
	public ActionListener editDocListener(DataOutputStream output, BufferedReader input, String user, SocketChannel skt_files, ServerSocketChannel skt_server) {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per l'aggiunta di un nuovo utente all'edit
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Creo due nuovi campi di testo
				// Campo di testo del nome del documento
				JTextField docText = new JTextField();
				// Campo di testo del numero di sezioni
				JTextField sectionText = new JTextField();
				// Salvo il numero massimo di sezioni
				int maxSezioni = ServerTuring.N_SECTIONS;
				
				// Numero di sezioni del documento da creare
				int numSezioni = -1;
				// Nome del documento da creare
				String documento = null;
				
				// Creo una TextBox che contiene i precedenti campi
				Object[] textBox = {
					"Nome del documento = ", 
					docText,
					"Numero di Sezioni del documento = ",
					sectionText, "range [0 - "+ maxSezioni +"]"
				};

				while(numSezioni >= maxSezioni || documento ==null || !documento.matches("[0-9a-zA-Z]+") || numSezioni <0) {
					/**
					 * Cicliamo finché vale una delle seguenti condizioni:
					 * 1) Numero di sezioni >= numero massimo (specificato nel file "ServerTuring.java"
					 * 2) Non è presente il nome del documento
					 * 3) Il nome del documento contiene caratteri non validi
					 * 4) Numero di sezioni < 0
					 */
					if(JOptionPane.showConfirmDialog(null, textBox, "Modifica di una sezione di un Documento", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						// Aggiorno i rispettivi campi, prelevandoli dai Campi di testo
						documento = docText.getText().trim();
						numSezioni = Integer.parseInt(sectionText.getText());
					}
					else return;
				}
				try {
					resultEditDoc(output, input, user, documento, numSezioni, skt_files, skt_server);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		return action;
	}
	/**
	 * Il metodo si occupa di gestire le letture/scritture utili durante la gestione del listener
	 * della creazione del documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param documento Nome del documento
	 * @param numSezioni Numero di sezioni del documento
  	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server server socket usato per il server
	 * @throws IOException 
	 */
	public void resultEditDoc(DataOutputStream output, BufferedReader input, String user, String documento, int numSezioni, SocketChannel skt_files, ServerSocketChannel skt_server) throws IOException {
		// Scrivo la richiesta (Creazione di un nuovo documento
		output.writeBytes("editDoc" + '\n');
		// INPUT PER LA RICHIESTA
		// 1) Nome dell'utente
		output.writeBytes(user + '\n');			
		// 2) Nome del documento
		output.writeBytes(documento + '\n');
		// 3) Numero di sezioni del documento
		output.writeByte(numSezioni);
		// Ack di ritorno (come risposta) dal server
		String ack = input.readLine();	
		// Controllo se non sia un messaggio di fuoriuscita dai range
		if(ack.equals("OP_OVER_SECT")) {
			// Ho sorpassato il range previsto 
			JOptionPane.showMessageDialog(null, "Sezione non presente", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Vedo il risultato della richiesta
		ack = input.readLine();
		// Creiamo il messaggio
		MessageHandler msg = new MessageHandler(ack, user, documento, null, numSezioni);
		if(ack.startsWith("2"))
			ack = msg.editing(skt_files, skt_server, input, output);
		else ack= "OP_ERROR";
		msg.setOp(ack);
		// Mostriamo un messaggio diverso in caso ai tipi di ack
		msg.handler();
	}
	
	
	
	
	/**
	 * Il metodo restituisce un ActionListener che si occupa della richiesta di visione di un documento
	 * @param output Stream di output dal server
	 * @param input Stream di input al server
	 * @param user Nome dell'utente (owner)
	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server Server socket usato per il server
	 * @param skt_client Socket per gestire la connessione TCP del client
	 * @return action L'action listener corrispondente
	 */
	public ActionListener logoutListener(DataOutputStream output, BufferedReader input, String user, Socket skt_client, SocketChannel skt_files, ServerSocketChannel skt_server) {
		// Creo una nuova Azione per ogni bottone
		ActionListener action;
		// Azione per la creazione di un documento
		action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operazione = null;
				// Scrivo la richiesta (Creazione di un nuovo documento
				try {
					output.writeBytes("logout" + '\n');
					// INPUT PER LA RICHIESTA
					// 1) Nome dell'utente
					output.writeBytes(user + '\n');	
					operazione = input.readLine();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if(operazione.equals("OP_ERROR") == false) {
					
					resultLogout(lnr, user, skt_client, skt_files, skt_server);
					// Devo tornare alla pagina iniziale

					/***
					 * GUIClass w = new GUIClass();			
					 					w.getContentPane().setBackground(Configurations.GUI_LOGIN_BACKGROUND);	
					w.setLocation(Configurations.GUI_X_POS, Configurations.GUI_Y_POS);
					w.setVisible(true);
					this.dispose();

					 */
				}
					
			}
		};
		return action;
	}
	/**
	 * Il metodo si occupa di gestire le letture/scritture utili durante la gestione del listener
	 * della creazione del documento
	 * @param lnr Listner degli inviti live
	 * @param user Nome dell'utente (owner)
	 * @param skt_client Socket per gestire la connessione TCP del client
	 * @param skt_files Canale usato per leggere il file
	 * @param skt_server server socket usato per il server
	 * @throws IOException 
	 */
	public void resultLogout(Listener lnr, String user,  Socket skt_client, SocketChannel skt_files, ServerSocketChannel skt_server){
		user="";
		/***
		 * DEVO DISABILITARE IL LISTENER
		 */
		// Chiudo i canali
		skt_client.close();
		skt_server.close();
		skt_files.close();
		
	}
	
	
	/**
	 * Metodi per l'acquisizione delle immagini
	 */
	public Image getImgNewDoc() {
		return this.Img_newDoc;
	}
	public Image getImgShowDoc() {
		return this.Img_showDoc;
	}
	public Image getImgEditDoc() {
		return this.Img_editDoc;
	}
	public Image getListDoc() {
		return this.Img_listDoc;
	}
	public Image getInvite() {
		return this.Img_inviteUsr;
	}

	public Image getLogout() {
		return this.Img_logoutUsr;
	}
	
	/**
	 * Metodi per l'acquisizione dei bottoni
	 */
	public JButton getBtNewDoc() {
		return this.B_newDoc;
	}
	public JButton getBtShowDoc() {
		return this.B_showDoc;
	}	
	public JButton getBtEditDoc() {
		return this.B_editDoc;
	}
	public JButton getBtListDoc() {
		return this.B_listDoc;
	}
	public JButton getBtInvite() {
		return this.B_inviteUsr;
	}
	public JButton getBtLogout() {
		return this.B_logoutUsr;
	}
	/**
	 * Metodo per l'acquisizione del label
	 */
	public JLabel getLabel() {
		return this.label;
	}
}
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//		// Azione per la creazione di un documento
//		action = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		};
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//		// Azione per la creazione di un documento
//		action = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		};
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//		// Azione per la creazione di un documento
//		action = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		};
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//		// Azione per la creazione di un documento
//		action = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		};
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//		// Azione per la creazione di un documento
//		action = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//			}
//		};
//		// Event listener per creare un nuovo documento
//		B_newDoc.addActionListener(action);
//	}
//	public void controlCreateDocument(String document, int numSezioni) {
//		
//	}
