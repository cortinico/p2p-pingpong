/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

package it.ncorti.pingpong;

import java.util.concurrent.TimeUnit;

/**
 * Classe che rappresenta un peer che non implementa il pong caching
 * e che effettua la fase di bootstrap
 * 
 * @author nicola
 */
public class SimpleBootstrapPeer extends SimplePeer {

	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////
	
	static final int NEIGHBOOR_PONG_PEER = Par.BOOTSTRAP_NEIGHBOOR_PONG_PEER;
	static final int NEIGHBOOR_PONG_SECONDS = Par.BOOTSTRAP_NEIGHBOOR_PONG_SECONDS;
	
	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////
	
	/**
	 * Costruttore per generare un nuovo peer di tipo SimpleBootstrapPeer
	 * 
	 * @param ip Indirizzo del nuovo peer
	 */
	public SimpleBootstrapPeer(String ip) { super(ip); }
	
	/**
	 * Costruttore per generare un nuovo peer di tipo SimpleBootstrapPeer e connetterlo ad un altro
	 * peer gia' esistente
	 * 
	 * @param ip Indirizzo del nuovo peer
	 * @param first Peer esistente a cui connettersi
	 */
	public SimpleBootstrapPeer(String ip, Peer first) { super(ip, first); }
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////
	

	@Override
	protected void bootOperations() {
		try{
			// Esegue la fase di boot e aggiorna il conteggio dei pong
			PONG_COUNTER += bootStrap(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	/**
	 * Metodo statico per eseguire la fase di boot di un Peer: invia un ping al primo peer nella lista
	 * dei peer connessi ed attende di ricevere dei Pong dai vicini per effettuare delle connesioni. 
	 * 
	 * @param p Peer che deve effettuare la fase di Boot
	 * @return Ritorna il numero di Pong che ha ricevuto.
	 * @throws InterruptedException Se il peer che ha effettuato il poll e' stato interrotto
	 */
	public static int bootStrap(Peer p) throws InterruptedException {
				
		Message firstping = new Message(MessType.PING, p);
		Message consume;
		
		Peer bootpeer = p.getConnections().get(0);
		bootpeer.send(firstping);
		
		int findPeer = 0;
		long start = System.currentTimeMillis();
		
		// Termina nel caso in cui abbia trovato abbastanza peer oppure sia passato troppo tempo
		while(findPeer < NEIGHBOOR_PONG_PEER && ((System.currentTimeMillis() - start)/1000.0) < NEIGHBOOR_PONG_SECONDS){
			
			consume = p.queue.poll(NEIGHBOOR_PONG_SECONDS, TimeUnit.SECONDS);
			
			if (consume != null && consume.GUID.contentEquals(firstping.GUID) && consume.function == MessType.PONG){
				p.connect(consume.sender);
				findPeer++;
			}
		}
		
		// Stampa il riepilogo del boot
		System.out.println(" #### " + p.address + " BOOT OVER ####");
		for (Peer pcon: p.getConnections()){
			System.out.println(" #### " + pcon.address + " CONNECTED WITH " + p + " ####");
		}
		
		
		
		return findPeer;
	}
	
	 
}
