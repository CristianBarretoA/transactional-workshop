package com.transactional.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.transactional.dao.AccountDAO;
import com.transactional.dto.AccountDTO;
import com.transactional.socket.ASocket;

public class ServerS extends ASocket {

	private ServerSocket serverSocket;
	private Socket sc;
	private DataInputStream in;
	private DataOutputStream out;
	private File db;
	private AccountDTO accountDTO;

	public ServerS() {
		this.db = new File(this.FILE_URL);
		this.accountDTO = new AccountDTO(this.db);
	}

	public ServerS(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
		this.db = new File(this.FILE_URL);
		this.accountDTO = new AccountDTO(this.db);
	}

	@Override
	public void socket() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			System.out.println("Servidor Iniciado");
		} catch (IOException e) {
			System.out.println("Error on socket: " + e.getMessage());
		}
	}

	/**
	 * Publish host and port
	 * 
	 * @return host:port
	 */
	public String bind() {
		return this.serverSocket.getLocalSocketAddress().toString();
	}

	/**
	 * Prepare the server to listen petitions
	 */
	public void listen() {
		try {
			while (true) {
				this.accept();
				this.in = new DataInputStream(this.sc.getInputStream());
				this.out = new DataOutputStream(this.sc.getOutputStream());
				System.out.println("Conexion con cliente hash " + this.sc.hashCode() + " aceptada");
				String response = this.read();
				this.write(response);
				System.out.println("Conexion con cliente hash " + this.sc.hashCode() + " cerrada \n\n");
			}
		} catch (IOException e) {
			System.out.println("Error on listen: " + e.getMessage());
		}
	}

	/**
	 * Accepts the connection
	 * 
	 * @throws IOException
	 */
	private void accept() throws IOException {
		this.sc = serverSocket.accept();
	}

	@Override
	public String read() throws IOException {
		String[] request = in.readUTF().split(";");
		switch (request[0]) {
		case "SAVE":
			System.out.println("Se recibe peticion de SAVE");
			AccountDAO accountDAO = accountDTO.extractValues(request[1]);
			accountDTO.store(accountDAO);
			return "OK";
		case "GET":
			System.out.println("Se recibe peticion de GET");
			return "Saldo en la cuenta N° " + request[1] + ":" + accountDTO.getValueByAccount(request[1]);
		default:
			return "Error on request";
		}
	}

	@Override
	public void write(String requestMessage) throws IOException {
		System.out.println("Enviando respuesta al cliente...");
		out.writeUTF(requestMessage);
	}

}
