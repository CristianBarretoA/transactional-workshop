package com.transactional;

import java.io.IOException;
import java.util.Scanner;

import com.transactional.dto.AccountDTO;
import com.transactional.socket.client.ClientS;
import com.transactional.socket.server.ServerS;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String ip = "127.0.0.1";
		int port = 5000;
		
		if(args.length == 2) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Desea ejecutar el 1:servidor o 2:cliente en esta maquina? (Digite el número)");
		Integer option = Integer.parseInt(sc.next());
		switch (option) {
		case 1:
			ServerS server = new ServerS(ip, port);
			server.socket();
			System.out.println("Server port: " + server.bind());
			server.listen();
			break;
		case 2:
			ClientS client;
			String accountNumber;
			String value;
			String message;
			System.out.println("Que accion desea realizar? (Digite el número)");
			System.out.println("1:Guardar Transaccion, 2:Consultar Saldo");
			Integer action = Integer.parseInt(sc.next());

			switch (action) {
			case 1:
				System.out.println("Por favor digite su numero de cuenta");
				accountNumber = sc.next();
				System.out.println("Por favor digite el valor de la transaccion");
				value = sc.next();
				message = AccountDTO.buildStoreMessage(accountNumber, value);
				client = new ClientS(ip, port);
				try {
					client.socket();
					client.write(message);
					System.out.println(client.read());
				} catch (IOException e) {
					System.out.println("Write-error: " + e.getMessage());
				} finally {
					client.close();
				}

				break;
			case 2:
				System.out.println("Por favor digite su numero de cuenta");
				accountNumber = sc.next();
				message = AccountDTO.buildGetMessage(accountNumber);
				client = new ClientS(ip, port);
				try {
					client.socket();
					client.write(message);
					System.out.println(client.read());
				} catch (IOException e) {
					System.out.println("Write-error: " + e.getMessage());
				} finally {
					client.close();
				}
				break;
			default:
				System.out.println("Accion Invalida!");
				break;
			}

			break;

		default:
			System.out.println("Opcion Invalida!");
			break;
		}
		sc.close();
	}

}
