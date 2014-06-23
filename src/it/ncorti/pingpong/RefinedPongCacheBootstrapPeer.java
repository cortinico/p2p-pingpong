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
 * ed effettua il bootstrap sulla rete.
 * 
 * @author Nicola Corti
 *
 */

public class RefinedPongCacheBootstrapPeer extends RefinedPongCachePeer {

	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////
	
	/**
	* Costruttore per generare un nuovo peer di tipo RefinedPongCacheBootstrapPeer
	* 
	* @param ip Indirizzo del nuovo peer
	*/
	public RefinedPongCacheBootstrapPeer(String ip) { super(ip); }
	
	/**
	* Costruttore per generare un nuovo peer di tipo RefinedPongCacheBootstrapPeer e connetterlo ad un altro
	* peer gia' esistente
	* 
	* @param ip Indirizzo del nuovo peer
	* @param first Peer esistente a cui connettersi
	*/
	public RefinedPongCacheBootstrapPeer(String ip, Peer first) { super(ip, first); }
	
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////
	

	@Override
	protected void bootOperations() {
		try{
			PONG_COUNTER += SimpleBootstrapPeer.bootStrap(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*System.out.println("BOOTIP: " + address + " #### BOOT OVER ####");
		for (Peer p: connection){
			System.out.println("BOOTIP: " + address + " ############ CONNECTED WITH " + p);
		}*/
		
	}		
}