/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

package it.ncorti.pingpong;

/**
 * Classe che rappresenta un Peer che implementa il Pong Caching refined
 * 
 * @author Nicola Corti
 *
 */
public class RefinedPongCachePeer extends PongCachePeer {
	
		
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////
	
	/** Cache di tipo refined utilizzata per salvare i pong ricevuti */
	RefinedPongCache cache = new RefinedPongCache();
	
	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////
	
	/**
	 * Costruttore per generare un nuovo peer di tipo RefinedPongCachePeer
	 * 
	 * @param ip Indirizzo del nuovo peer
	 */
	public RefinedPongCachePeer(String ip) { super(ip); }
	
	/**
	 * Costruttore per generare un nuovo peer di tipo RefinedPongCachePeer e connetterlo ad un altro
	 * peer gia' esistente
	 * 
	 * @param ip Indirizzo del nuovo peer
	 * @param first Peer esistente a cui connettersi
	 */
	public RefinedPongCachePeer(String ip, Peer first) { super(ip, first); }

	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////
	
	
	@Override
	protected void pingMessage(Message m) {

		if (m.TTL <= 1)
			return;
		cache.removeExpired();
		super.pingMessage(m);
		// Alla ricezione di ogni ping rimuovo i pong scaduti
	}
	
}
