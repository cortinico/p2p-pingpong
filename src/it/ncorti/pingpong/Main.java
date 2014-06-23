/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


/**
 * Classe principale per effettuare il test di una rete PingPong partendo da 3 file: 
 * - Un file contenente l'elenco dei peer della rete
 * - Un file contenente l'elenco degli archi della rete
 * - Un file che descrive come si evolve la rete
 * @author Nicola Corti
 */
public class Main {
	
	public static void main(String[] args) throws IOException, NumberFormatException, InterruptedException {
		
		if (args.length < 1){
			throw new RuntimeException("Wrong Invocation!");
		}
		
		boolean simpleMode = false;
		boolean cachedMode = false;
		boolean refinedMode = false;
		
		// Setup modalita'
		if (args[0].contentEquals("simple"))
			simpleMode = true;
		if (args[0].contentEquals("cache"))
			cachedMode = true;
		if (args[0].contentEquals("refined"))
			refinedMode = true;
		
		
		// Lettura files
		FileInputStream fpeer = new FileInputStream(args[1]);
		FileInputStream fconn = new FileInputStream(args[2]);
		FileInputStream fdyna = new FileInputStream(args[3]);
		BufferedReader brpeer = new BufferedReader(new InputStreamReader(fpeer));
		BufferedReader brconn = new BufferedReader(new InputStreamReader(fconn));
		BufferedReader brdyna = new BufferedReader(new InputStreamReader(fdyna));
		String line = null, temp;
		StringTokenizer strtok = null;
		
		PingPongNet net = new PingPongNet();

		while ((line = brpeer.readLine()) != null){
			if (simpleMode)
				net.addSimplePeer(line);
			if (cachedMode)
				net.addPongCachePeer(line);
			if (refinedMode)
				net.addRefinedPongCachePeer(line);
		}

		while ((line = brconn.readLine()) != null) {
			strtok = new StringTokenizer(line, ";");
			net.addConnection(strtok.nextToken(),strtok.nextToken());
		}
		
		System.out.println(" --- NETWORK SUCCESFULLY INITIALIZED ---");
		net.print();
		System.out.println(" --- NETWORK STARTING ---");
		net.start();
		
		// Esecuzione azioni dinamiche
		while ((line = brdyna.readLine()) != null) {

			strtok = new StringTokenizer(line, ";");
			
			if ((temp = strtok.nextToken()).contains("sleep"))
				Thread.sleep(Long.parseLong(strtok.nextToken())*1000);
			else {
				if (simpleMode)
					net.addSimpleBootstrapPeer(temp, strtok.nextToken());
				if (cachedMode)
					net.addPongCacheBootstrapPeer(temp, strtok.nextToken());
				if (refinedMode)
					net.addRefinedPongCacheBootstrapPeer(temp, strtok.nextToken());
			}
		}
		

		fpeer.close();
		fconn.close();
		fdyna.close();
		brpeer.close();
		brconn.close();
		brdyna.close();
		
		// Stampa parametri dalla rete
		while(true){
			Thread.sleep(Par.MAIN_POLL_STATS_SECONDS * 1000);
			net.printStats();
		}
		
	}
}
