package com.transactional.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.transactional.socket.ASocket;

public class ClientS extends ASocket {
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	public ClientS() {

	}

	public ClientS(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void socket() {
		try {
			this.socket = new Socket(this.ip, this.port);
			System.out.println("Cliente Iniciado...");			
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());		

		} catch (UnknownHostException e) {
			System.out.println("Socket-Host error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Socket-Error: " + e.getMessage());
		}
	}

	@Override
	public String read() throws IOException {
		return " ----\n Server Response: "+ this.in.readUTF() + "\n ----";
	}

	@Override
	public void write(String requestMessage) throws IOException {
		System.out.println("Enviando peticion...");
		this.out.writeUTF(requestMessage);
	}
	
	/**
	 * Close the socket
	 */
	public void close() {
		try {
			System.out.println("...Cliente cerrado");	
			this.socket.close();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
