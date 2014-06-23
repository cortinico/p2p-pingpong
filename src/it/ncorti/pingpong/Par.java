/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/

package it.ncorti.pingpong;

/**
 * Classe contenente i parametri di esecuzione di tutto il sistema.
 * Attenzione! - Modificare questi valori in modo errato puo' portare il sistema a condizioni di instabilita'
 * 
 * @author Nicola Corti
 */
public class Par {
	
	
	/** Parametro per impostare i secondi ogni quanto si desidera che il thread
	 * Main stampi le statistiche sul sistema */
	final static int MAIN_POLL_STATS_SECONDS = 10;

	
	/** Valore massimo del TTL del messaggio. Abbassando il valore si dimunisce il numero di peer
	 * che un messaggio puo' raggiungere. Il valore deve essere sempre >= 2	 */
	final static int MESSAGE_MAX_TTL = 5;
	
	
	/** Numero di secondi che un peer deve stare in attesa di un messaggio, dopo questi secondo il peer
	 * esegue la funzione noMessage() se non ha ricevuto messaggi. */
	final static int PEER_POLL_SECONDS = 20;
	
	
	/** Parametro utilizzato dai peer che effettuano il bootstrap, rappresenta il numero di peer
	 * che si spera di riuscire a contattare nella fase di bootstrap */
	final static int BOOTSTRAP_NEIGHBOOR_PONG_PEER = 5;
	/** Numero di secondo che il peer in fase di bootstrap aspetta durante la ricezione dei Pong.
	 * Allo scadere di questi secondi il peer ha terminato la fase di bootstrap */
	final static int BOOTSTRAP_NEIGHBOOR_PONG_SECONDS = 6;
	
	
	/** Numero minimo di Pong che sono richiesti nella cache per poter rispondere utilizzandola*/
	final static int PONG_CACHE_REQUIRED_PONGS = 2;
	/** Numero di secondi che vengono attesi per effettuare il refresh della pong cache
	 * nel caso in cui un vicino non supporti il pong cache.
	 * Se lo supporta viene usato PEER_POLL_SECONDS come numero di secondi*/
	final static int PONG_CACHE_REFRESH_NOSUPPORT = 180;
	
	
	/** Numero di secondi dopo i quali un Pong nella Pong Cache Refined scade*/
	final static long REFINED_PONG_CACHE_EXPIRING_SECONDS = 5;

}
