/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

package it.ncorti.pingpong;

/**
 * Classe che rappresenta un peer che non implementa il pong caching
 * e che e' connesso alla rete dal principio
 * 
 * @author nicola
 */
public class SimplePeer extends Peer implements Runnable {

	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////	
	
	/**
	* Costruttore per generare un nuovo peer di tipo SimplePeer
	* 
	* @param ip Indirizzo del nuovo peer
	*/
	public SimplePeer(String ip) { super(ip); }
	
	/**
	* Costruttore per generare un nuovo peer di tipo SimplePeer e connetterlo ad
	* peer gia' esistente
	* 
	* @param ip Indirizzo del nuovo peer
	* @param first Peer esistente a cui connettersi
	*/
	public SimplePeer(String ip, Peer first) { super(ip, first); }

	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////	
	

	@Override
	protected void noMessage() {
		return;
	}


	@Override
	protected void pingMessage(Message received) {
		
		SimplePeer lastPeer = (SimplePeer) received.getLastPeer();
		received.updatePeer(this);
		
		// Scarto il ping se ha TTL = 0 o se l'ho gia' visto (ciclo)
    	if (received.TTL <= 0 || seenGUIDPing.containsKey(received.GUID))
    		return;
    	
    	seenGUIDPing.put(received.GUID, lastPeer);
    	sendNeighboor(received, lastPeer);
    
    	// Genero il pong di risposta
    	Message answer = new Message(MessType.PONG, this, received.GUID);
    	lastPeer.send(answer);
		
	}

	
	@Override
	protected void pongMessage(Message received) {
		
    	if (seenGUIDPing.containsKey(received.GUID)){

    		// Faccio il backward routing del pong
        	SimplePeer t = (SimplePeer) seenGUIDPing.get(received.GUID);
        	received.updatePeer(this);
        	if (received.TTL > 0){
        		
        		t.send(new Message(received));
        	}
    	}
		
	}
	

	@Override
	protected void bootOperations() {
		return;
	}
}
