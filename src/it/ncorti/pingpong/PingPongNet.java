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
 * Classe che rappresenta una rete di Peer che cominicano tramite il protocollo Ping/Pong.
 * La classe si occupa di aggiungere ed avviare i peer, di connetterli e di mostrare la statistiche
 * della rete 
 * 
 * @author Nicola
 */
public class PingPongNet {

	
	//////////////////////////////////
	//	MEMBRI						//
	//////////////////////////////////		
	
	/** Hashtable che contiene l'elenco dei peer (coppie indirizzo,peer) */
	Hashtable<String, Peer> plist = new Hashtable<>();
	/** Lista dei thread associati ai peer */
	List<Thread> tlist = new ArrayList<Thread>();


	//////////////////////////////////
	//	COSTRUTTORI					//
	//////////////////////////////////			
	
	/**
	 * Costruttore che genera un'istanza vuota
	 */
	public PingPongNet(){	
	}
	

	//////////////////////////////////
	//	METODI						//
	//////////////////////////////////		
	
	/**
	 * Aggiunge alla rete un peer di tipo SimplePeer alla rete
	 * 
	 * @param p Nome del nuovo peer da creare 
	 * @return Ritorna true se il peer e' stato inserito correttamente (se e' gia' presente un peer con lo stesso nome ritorna false), false altrimenti
	 */
	public boolean addSimplePeer(String p){
		if (plist.containsKey(p))
			return false;
		plist.put(p, new SimplePeer(p));
		return true;
	}
	
	/**
	 * Aggiunge alla rete un peer di tipo PongCachePeer alla rete
	 * 
	 * @param p Nome del nuovo peer da creare 
	 * @return Ritorna true se il peer e' stato inserito correttamente (se e' gia' presente un peer con lo stesso nome ritorna false), false altrimenti
	 */
	public boolean addPongCachePeer(String p){
		if (plist.containsKey(p))
			return false;
		plist.put(p, new PongCachePeer(p));
		return true;
	}
	
	/**
	 * Aggiunge alla rete un peer di tipo RefinedPongCachePeer alla rete
	 * 
	 * @param p Nome del nuovo peer da creare 
	 * @return Ritorna true se il peer e' stato inserito correttamente (se e' gia' presente un peer con lo stesso nome ritorna false), false altrimenti
	 */
	public boolean addRefinedPongCachePeer(String p){
		if (plist.containsKey(p))
			return false;
		plist.put(p, new RefinedPongCachePeer(p));
		return true;
	}
	
	/**
	 * Aggiunge alla rete un peer di Bootstrap e lo avvia.
	 * 
	 * @param p Peer da aggiungere e abbiare
	 * @return Ritorna true se il peer e' stato inserito correttamente, false altrimenti
	 */
	private boolean addBootstrapPeer(Peer p){
		plist.put(p.address, p);
		Thread t = new Thread(p);
		tlist.add(t);
		t.start();
		return true;
	}
	
	/**
	 * Aggiunge alla rete un peer di tipo SimpleBootstrapPeer e lo avvia.
	 * 
	 * @param p Nome del peer da aggiungere
	 * @param conn Nome del peer che verra' usato come prima connessione
	 * @return Ritorna true se il peer e' stato inserito correttamente, false altrimenti
	 */
	public boolean addSimpleBootstrapPeer(String p, String conn){
		if (!plist.containsKey(conn))
			return false;
		SimpleBootstrapPeer bp = new SimpleBootstrapPeer(p, plist.get(conn));
		return addBootstrapPeer(bp);
	}
	
	/**
	 * Aggiunge alla rete un peer di tipo PongCacheBootstrapPeer e lo avvia.
	 * 
	 * @param p Nome del peer da aggiungere
	 * @param conn Nome del peer che verra' usato come prima connessione
	 * @return Ritorna true se il peer e' stato inserito correttamente, false altrimenti
	 */
	public boolean addPongCacheBootstrapPeer(String p, String conn){
		if (!plist.containsKey(conn))
			return false;
		PongCacheBootstrapPeer bp = new PongCacheBootstrapPeer(p, plist.get(conn));
		return addBootstrapPeer(bp);
	}
	
	/**
	 * Aggiunge alla rete un peer di tipo RefinedPongCacheBootstrapPeer e lo avvia.
	 * 
	 * @param p Nome del peer da aggiungere
	 * @param conn Nome del peer che verra' usato come prima connessione
	 * @return Ritorna true se il peer e' stato inserito correttamente, false altrimenti
	 */
	public boolean addRefinedPongCacheBootstrapPeer(String p, String conn){
		if (!plist.containsKey(conn))
			return false;
		RefinedPongCacheBootstrapPeer bp = new RefinedPongCacheBootstrapPeer(p, plist.get(conn));
		return addBootstrapPeer(bp);
	}

	/**
	 * Aggiunge una connessione alla rete fra due peer
	 * 
	 * @param s1 Primo peer della connessione
	 * @param s2 Secondo peer della connessione
	 * @return Ritorna true se la connessione e' stata realizzata correttamente, false altrimenti
	 */
	public boolean addConnection(String s1, String s2){
		if (plist.containsKey(s1) && plist.containsKey(s2)){
			Peer p1 = plist.get(s1);
			Peer p2 = plist.get(s2);
			
			p1.connect(p2);
			return true;
		}
		return false;
	}
	
	/**
	 * Genera ed avvia tutti i thread relativi ai peer presenti nella rete.
	 */
	public void start(){
		Enumeration<Peer> e = plist.elements();
		while(e.hasMoreElements()) {
			tlist.add(new Thread(e.nextElement()));
		}
		for(Thread t : tlist){
			t.start();
		}
	}
	
	/**
	 * Stampa l'elenco dei peer con tutte le connesioni esistenti.
	 */
	public void print(){
		
		Enumeration<Peer> e = plist.elements();
		List<Peer> pconn = null;
		
		while (e.hasMoreElements()) {
			
			Peer actual = (Peer) e.nextElement();
			System.out.print("PEER ## " + actual.getAddress() + " ## - ");
			
			pconn = actual.getConnections();
			for (Peer p : pconn){
				System.out.print(p.getAddress() + " - ");
			}
			
			System.out.print("\n");
		}
	}
	
	/**
	 * Stampa l'elenco delle statistiche di tutti i Peer della rete (numero di ping/pong/messaggi totali)
	 */
	public void printStats(){
		
		Enumeration<Peer> e = plist.elements();
		int ping=0, pong=0, total=0;
		
		System.out.println("************BEGIN TOTALS************");
		
		while (e.hasMoreElements()) {
			
			Peer actual = (Peer) e.nextElement();
			
			System.out.println(actual.address + ";" + actual.getPingsCount() + ";" + actual.getPongsCount() + ";" + actual.getTotalCount() + ";");
			
			ping += actual.getPingsCount();
			pong += actual.getPongsCount();
			total += actual.getTotalCount();
		}
		
		System.out.println("Totals;" + ping + ";" + pong + ";" + total + ";");
		System.out.println("************END TOTALS************");
		
	}
}