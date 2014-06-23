/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

package it.ncorti.pingpong;

import java.util.List;

/**
 * Classe che rappresenta un Peer che implementa il Pong Caching semplice
 * 
 * @author Nicola Corti
 *
 */
public class PongCachePeer extends SimplePeer {

	
	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////	
	
	public static final int REFRESH_NOSUPPORT = Par.PONG_CACHE_REFRESH_NOSUPPORT;
	
	
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////
	
	/** Cache utilizzata per salvare i pong ricevuti */
	PongCache cache = new PongCache();
	
	/** Contatore utilizzato per memorizzare i secondi passati prima di fare
	 * l'update della cache (nel caso in cui i vicini non supportino il pongcache */
	private int counter;
	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////
	
	/**
	 * Costruttore per generare un nuovo peer di tipo PongCachePeer
	 * 
	 * @param ip Indirizzo del nuovo peer
	 */
	public PongCachePeer(String ip) { super(ip); }
	
	/**
	 * Costruttore per generare un nuovo peer di tipo PongCachePeer e connetterlo ad un altro
	 * peer gia' esistente
	 * 
	 * @param ip Indirizzo del nuovo peer
	 * @param first Peer esistente a cui connettersi
	 */
	public PongCachePeer(String ip, Peer first) { super(ip, first); }
	
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////
	

	@Override
	protected void pingMessage(Message m) {

		if (m.TTL <= 1)
			return;
		
		// Se ci sono abbastanza pong nella cache rispondo, altrimenti mi comporto come il SimplePeer
		if (cache.containsEnough()){
			
			List<Message> pongs = cache.getPongs(m);
			
			pongs.add(new Message(MessType.PONG, this));
			for (Message pong : pongs){
				m.getLastPeer().send(pong);
			}
		} else {
			super.pingMessage(m);
		}
			
	}

	@Override
	protected void pongMessage(Message m) {

		if (m.HOP > 0){
			
			// Salvo nella cache il pong ricevuto
			cache.addEntry(m);
			
		}	
		super.pongMessage(m);
	}
	
	
	@Override
	protected void noMessage() {
		Message firstping = new Message(MessType.PING, this);
		if (neighboorSupportCache()){
			
			super.sendNeighboor(firstping, this);
		} else {
			
			// Caso se i vicini non supportano il pong caching
			counter += Peer.POLL_SECONDS;
			if (counter > REFRESH_NOSUPPORT){
				super.sendNeighboor(firstping, this);
				counter = 0;
			}
		}
	}
	
	/**
	 * Metodo per controllare se i vicini supportano il Pong Cache
	 * 
	 * @return True se tutti i vicini lo supportano, false altrimenti
	 */
	private boolean neighboorSupportCache(){
		boolean supp = true; 
		for(int i = 0; i < connection.size(); i++){
			Peer toSend = connection.get(i);
			supp = supp && (toSend instanceof PongCachePeer);
		}
		return supp;
	}
	
}
