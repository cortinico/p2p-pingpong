/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;

import java.util.Enumeration;

/**
 * Classe per gestire la cache dei pong di un peer che implementa il pong caching in modalita' refined
 * 
 * @author nicola
 */
public class RefinedPongCache extends PongCache {
	
	
	//////////////////////////////////
	//	PARAMETRI (vedi Par.java)	//
	//////////////////////////////////

	public static final long EXPIRING_SECONDS = Par.REFINED_PONG_CACHE_EXPIRING_SECONDS;
	
	/**
	 * Metodo per filtrare dalla cache i pong che sono scaduti (calcolato in base al timestamp).
	 * 
	 */
	public void removeExpired(){
		Enumeration<Message> e = cache.keys();
		long time = System.currentTimeMillis();
		
		while(e.hasMoreElements()){
			Message m = e.nextElement();
			
			if ((time - cache.get(m)) > EXPIRING_SECONDS*1000.0){
				cache.remove(m);
			}
		}
	}
}
