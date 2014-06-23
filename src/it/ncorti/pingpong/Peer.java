/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Classe astratta che contiene i metodi comuni a tutti i tipi di peer.
 * Implementa l'interfaccia Runnable per potere essere avviato in un thread.
 * 
 * @author Nicola
 */
public abstract class Peer implements Runnable {

	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////	
	
	static final int POLL_SECONDS = Par.PEER_POLL_SECONDS;
	
	
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////	
	
	/** Indirizzo del peer */
	protected String address;
	
	/** Coda dei messaggi del peer */
	protected BlockingQueue<Message> queue  = new LinkedBlockingQueue<>();
	/** Elenco delle connessioni del peer */
	protected ArrayList<Peer> connection = new ArrayList<>();
	/** Elenco dei peer che sono stati visti dal peer */
	protected Hashtable<String, Peer> seenGUIDPing = new Hashtable<String, Peer>();
	
	/** Conteggio dei messaggi di ping visti */
	protected int PING_COUNTER = 0;
	/** Conteggio dei messaggi di pong visti */
	protected int PONG_COUNTER = 0;

	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////	
	
	/**
	 * Costruttore per generare un nuovo peer
	 * 
	 * @param ip Indirizzo del nuovo peer
	 */
	public Peer(String ip){
		this.address = ip;
	}
	
	/**
	 * Costruttore per generare un nuovo peer e connetterlo ad un altro
	 * peer gia' esistente
	 * 
	 * @param ip Indirizzo del nuovo peer
	 * @param first Peer esistente a cui connettersi
	 */
	public Peer(String ip, Peer first){
		this.address = ip;
		connect(first);
	}
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////	
	
	/**
	 * Getter per l'indirizzo del peer
	 * 
	 * @return L'indirizzo del peer.
	 */
	public String getAddress(){
		return this.address;
	}
	
	/**
	 * Getter per l'elenco dei peer connessi al peer su cui viene invocata
	 * 
	 * @return Una lista di peer che sono connessi al peer su cui viene invocata
	 */
	public List<Peer> getConnections(){
		return connection;
	}
	
	/**
	 * Effettua la connessione fra due peer (in tutti e 2 i sensi)
	 * 
	 * @param dest Peer con cui effettuare la connessione
	 */
	public void connect(Peer dest){
		if (!connection.contains(dest))
			connection.add(dest);
		if (!dest.connection.contains(this))
			dest.connection.add(this);
	}
	
	/**
	 * Effettua la disconnessione fra due peer
	 * 
	 * @param dest Peer da cui effettuare la disconnesione
	 */
	public void disconnect(Peer dest){
		connection.remove(dest);
		dest.disconnect(this);
	}
	
	/**
	 * Controlla se due peer sono connessi.
	 * 
	 * @param dest Peer per cui effettuare il controllo
	 * @return True se sono connessi, false altrimenti
	 */
	public boolean isconnected(Peer dest){
		return (connection.contains(dest) && dest.isconnected(this));
	}
	
	/**
	 * Metodo per inviare un messaggio al peer sui cui viene invocata.
	 * Accoda il messaggio alla coda dei messaggi del peer per essere successivamente gestito
	 * 
	 * @param m Messaggio da inviare
	 */
	public void send(Message m){
		try {
			this.queue.put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Invia un messaggio a partire da un Peer a tutti i suoi vicini connessi.
	 * Permette di indicare un peer a cui non inviare (utile per il routing dei pacchetti)
	 * 
	 * @param consume Messaggio da inviare
	 * @param lastPeer Peer a cui non inviare
	 */
	protected void sendNeighboor(Message consume, Peer lastPeer) {
		for(int i = 0; i < connection.size(); i++){
			Peer toSend = connection.get(i);
			
			// Non invia a lastPeer e al sender del messaggio (per evitare cicli) 
			if (!(toSend==lastPeer) && !(toSend==consume.getSender())){
				//System.out.println("IP: " + address + " SENDING: " + consume + " To:" + toSend);
				toSend.send(new Message(consume));
			}
		}
	}
		
	
	/**
	 * Funzione che viene eseguita dal thread.
	 * Questo metodo rappresenta lo scheletro che tutti i peer eseguono, la `business-logic` e' definita nelle funzioni
	 * astratte bootOperations(), noMessage(), pingMessage(), pongMessage() che vengono implementate dalle sottoclassi.
	 */
	@Override
	public void run() {
		
		bootOperations();
				
		while(true){
            try {
                Message consume = queue.poll(POLL_SECONDS, TimeUnit.SECONDS);
                if (consume == null){
                	noMessage();
                	continue;
                }
                
                if (consume.function == MessType.PING){
                	PING_COUNTER++;
                	pingMessage(consume);
                }
                
                if (consume.function == MessType.PONG){
                	PONG_COUNTER++;
                	pongMessage(consume);
                }
                
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            }
        }
	}
	
	/**
	 * Metodo che rappresenta il comportamento del peer prima di iniziare ad esaminare la coda dei messaggi.
	 * Questo metodo deve essere implementato dalle sottoclassi di Peer
	 */
	protected abstract void bootOperations();
	
	/**
	 * Metodo che rappresenta il comportamento del peer quando la coda dei messaggi e' vuota per piu' di Par.PEER_POLL_SECONDS
	 * Questo metodo deve essere implementato dalle sottoclassi di Peer
	 */
	protected abstract void noMessage();
	
	/**
	 * Metodo che rappresenta il comportamento del peer alla ricezione di un messaggio di Ping
	 * Questo metodo deve essere implementato dalle sottoclassi di Peer
	 * 
	 * @param m il messaggio di Ping ricevuto
	 */
	protected abstract void pingMessage(Message m);
	
	/**
	 * Metodo che rappresenta il comportamento del peer alla ricezione di un messaggio di Pong
	 * Questo metodo deve essere implementato dalle sottoclassi di Peer
	 * 
	 * @param m il messaggio di Pong ricevuto
	 */
	protected abstract void pongMessage(Message m);

	/**
	 * Metodo che ritorna una stringa con le statistiche su ping e pong del peer,
	 * utile per le stampe di debug.
	 * 
	 * @return Stringa contenente indirizzo, n. ping, n. pong e n. totale di messaggi ricevuti 
	 */
	public String getStats(){
		return new String("Address: " + address + " Ping: " + PING_COUNTER + " Pong: " + PONG_COUNTER);
	}
	
	/**
	 * Ritorna il numero di ping ricevuti
	 * 
	 * @return Numeri di ping ricevuti
	 */
	public int getPingsCount(){
		return PING_COUNTER;
	}
	
	/**
	 * Ritorna il numero di pong ricevuti
	 * 
	 * @return Numeri di pong ricevuti
	 */
	public int getPongsCount(){
		return PONG_COUNTER;
	}
	
	/**
	 * Ritorna il numero di messaggi totali ricevuti
	 * 
	 * @return Numeri di messaggi totali ricevuti
	 */
	public int getTotalCount(){
		return (PING_COUNTER+PONG_COUNTER);
	}
	
	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Peer)
			return ((Peer)obj).getAddress().contentEquals(this.getAddress());
	    else
	        return false;
	}


	@Override
	public String toString() {
		return address.toString();
	}

}
