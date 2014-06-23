/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;

/**
 * Classe per gestire un messaggio di Ping/Pong fra due peer
 * 
 * @author Nicola Corti
 */
public class Message {
	
	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////	

	final static int MAX_TTL = Par.MESSAGE_MAX_TTL;
	
	
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////	
	
	/** Campo contenente il tipo del messaggio */
	MessType function;
	/** Time to leave del messaggio */
	int TTL;
	/** Numero di HOP effettuati dal messaggio */
	int HOP;
	/** ID univoco del messaggio (IP + timestamp) */
	String GUID;
	/** Numero di bytes condivisi dal peer che invia il messaggio (non usati)*/
	int sharedbytes;
	/** Numero di files condivisi dal peer che invia il messaggio (non usati)*/
	int sharedfiles;
	
	/** Peer che ha inviato il messaggio */
	Peer sender;
	/** Ultimo peer che ha effettuato il routing del messaggio */
	Peer lastpeer;
	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////	
	
	/**
	 * Costruttore per generare un nuovo messaggio da inviare
	 * 
	 * @param t Tipo del messaggio
	 * @param send Peer che sta generando il messaggio
	 */
	public Message(MessType t, Peer send){
		
		this.function = t;
		HOP = 1;
		TTL = MAX_TTL-1;
		GUID = send + "-" + System.currentTimeMillis();
		this.sharedbytes = 0;
		this.sharedfiles = 0;
		
		this.sender = send;
		this.lastpeer = send;
	}
	
	/**
	 * Costruttore per generare un nuovo messaggio da inviare indicando l'ID univoco
	 * 
	 * @param t Tipo del messaggio
	 * @param send Peer che sta generando il messaggio
	 * @param GUID Id univoco che deve avere il messaggio
	 */
	public Message(MessType t, Peer send, String GUID){
		this(t, send);
		this.GUID = GUID; 
	}
	
	/**
	 * Costruttore per generare la copia di un messaggio
	 * 
	 * @param m Messsaggio di partenza per la copia
	 */
	public Message(Message m){
		this.function = m.function;
		this.TTL = m.TTL;
		this.HOP = m.HOP;
		this.GUID = new String(m.GUID);
		this.sender = m.sender;
		this.lastpeer = m.lastpeer;
	}
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////	
	
	
	/**
	 * Effettua l'aggiornamento di un messaggio, modificando l'ultimo peer che ne ha effettuato il routing,
	 * il numero degli HOP e il TTL
	 * 
	 * @param p Peer che sta effettuato l'operazione
	 */
	public void updatePeer(Peer p){
		this.lastpeer = p;
		this.HOP++;
		this.TTL--;
	}
	
	/**
	 * Getter per il peer che ha inviato il messaggio
	 * 
	 * @return Il peer che ha inviato il messaggio
	 */
	public Peer getSender(){
		return this.sender;
	}
	
	/**
	 * Getter per l'ultimo peer che ha effettuato il routing del messaggio
	 * 
	 * @return L'ultimo peer che ha effettuato il routing del messaggio
	 */
	public Peer getLastPeer(){
		return this.lastpeer;
	}


	@Override
	public String toString() {
		return new String("Message ID:" + this.GUID + " Type:" + this.function + " SND:" + this.sender + " LST:" + this.lastpeer + " TTL:" + this.TTL + " HOP:" + this.HOP);
	}

}
