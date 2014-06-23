/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Classe per gestire la cache dei pong di un peer che implementa il pong caching
 * 
 * @author nicola
 */
public class PongCache {
	
	
	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////

	public static final int REQUIRED_PONGS = Par.PONG_CACHE_REQUIRED_PONGS;
	
	
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////
	
	/** Hashtable per memorizzare i pong che sono passati */
	protected Hashtable<Message, Long> cache = new Hashtable<Message, Long>();
	
	
	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////
	
	/**
	 * Crea un'istanza di PongCache vuota
	 */
	public PongCache(){
	}
	
	
	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////
	
	/**
	 * Aggiunge un nuovo pong alla cache.
	 * Se era gia' presente un pong proveniente dallo stesso peer questo viene rinnovato
	 * 
	 * @param fresh
	 */
	public void addEntry(Message fresh){
		
		long time = System.currentTimeMillis();
		Enumeration<Message> e = cache.keys();
		
		Message old;
		
		while( e.hasMoreElements()){
			old = e.nextElement();
			
			// Aggiorna la cache se c'era gia' un pong inviato dallo stesso peer
			if (old.getSender() == fresh.getSender()){
				cache.remove(old);
				break;
			}
		}
		cache.put(fresh, time);
	}
	
	/**
	 * Rimuove tutti gli elementi dalla Cache
	 */
	public void removeAll(){
		cache = new Hashtable<Message, Long>(); 
	}
	
	/**
	 * Ritorna true se la cache contiene abbastanza elementi da permettere di rispondere
	 * ad un ping ricevuto da un Peer.
	 * 
	 * @return True se il numero di elementi in cache e' sufficiente (>= di REQUIRED_PONGS)
	 */
	public boolean containsEnough(){
		return (cache.size() >= REQUIRED_PONGS);
	}
	
	/**
	 * Ritorna l'elenco dei Pongs che devono essere restituiti in seguito ad un messaggio di Ping.
	 * 
	 * @param Pi Il messaggio di Ping ricevuto
	 * @return Una lista di Messaggi di Pong generati per esseri inviati in risposta
	 */
	public List<Message> getPongs(Message Pi){
		
		List<Message> l = new ArrayList<>();
		Enumeration<Message> e = cache.keys();
		int i = 0;
		
		Message Pc, Po;
		
		while(i < REQUIRED_PONGS && e.hasMoreElements()){
			Pc = e.nextElement();
			Po = new Message(Pc);
			
			Po.GUID = Pi.GUID;
			Po.TTL = Message.MAX_TTL - Pc.HOP;
			Po.HOP = Pc.HOP + 1;
			
			// In caso contrari non lo si aggiunge, non potrebbe raggiungere
			// il peer destinazione.
			if (!(Po.TTL < Pi.HOP)){
				l.add(Po);
				i++;
			}
		}
		return l;
	}
}
