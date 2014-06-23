/*Copyright (c) 2013 - Nicola Corti

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.  See the file
  COPYING included with this distribution for more information.
*/
package it.ncorti.pingpong;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe per la generazione di grafi casuali, partendo dalla dimensione
 * e dalla probabilita' della presenza di un arco (tra 0 e 1)
 * 
 * Deve essere invocato con 2 parametri: il primo e' la dimensione del grafo (intero),
 * il secondo e' la probabilita'.
 * 
 * Genera i file 'connect_gen' e 'peer_gen' per essere utilizzati con la rete PingPong
 * 
 * @author Nicola Corti
 */
public class GraphGenerator {
	
	public static void main(String[] args) throws IOException {
		
		// Nomi dei files da generare
		String PEER_FILE_NAME = "peer_gen";
		String CONNECT_FILE_NAME = "connect_gen";
		
		
		if (args.length <= 1 ){
			throw new RuntimeException("Wrong Invocation!");
		}
			
		int size = Integer.parseInt(args[0]);
		double coeff = Double.parseDouble(args[1]);
		
		// Generazione dei files
		File nodes = new File(PEER_FILE_NAME);
		FileWriter fnodes = new FileWriter(nodes.getAbsoluteFile());
		BufferedWriter bnodes = new BufferedWriter(fnodes);
		File connect = new File(CONNECT_FILE_NAME);
		FileWriter fconnect = new FileWriter(connect.getAbsoluteFile());
		BufferedWriter bconnect = new BufferedWriter(fconnect);
		
		// Generazione nodi ed archi
		for (int i = 0; i < size; i++){
			bnodes.write("p" + i + "\n");
		}
		for (int i = 0; i < size; i++){
			for(int j = i; j < size; j++){
				if (i == j)
					continue;
				if(Math.random() < coeff)
					bconnect.write("p" + i + ";p" + j + "\n");
			}
		}
		
		bnodes.close();
		bconnect.close();	
		fnodes.close();
		fconnect.close();
		
		System.out.println("- Peer File Generated: " + PEER_FILE_NAME);
		System.out.println("- Connect File Generated: " + CONNECT_FILE_NAME);
	
	}
}
