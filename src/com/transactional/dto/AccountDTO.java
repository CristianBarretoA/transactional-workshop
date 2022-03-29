package com.transactional.dto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.transactional.dao.AccountDAO;

public class AccountDTO {

	private File db;

	public AccountDTO(File db) {
		this.db = db;
	}

	public AccountDAO extractValues(String request) {
		AccountDAO account = new AccountDAO();
		String[] values = request.split(",");
		account.setAccountNumber(Long.parseLong(values[0]));
		account.setValue(Long.parseLong(values[1]));
		return account;
	}

	/**
	 * Store a transaction
	 * @param account
	 */
	public void store(AccountDAO account) {
		FileWriter dbWriter = null;
		try {
			dbWriter = new FileWriter(this.db, true);

			dbWriter.write(account.getAccountNumber() + "," + account.getValue() + " \n");

		} catch (IOException e) {
			System.out.println("Error on saving : " + e.getMessage());
		} finally {
			try {
				dbWriter.close();
			} catch (IOException e) {
				System.out.println("Error on saving : " + e.getMessage());
			}

		}
	}

	/**
	 * Get the value of the transaction by account number
	 * @param account
	 * @return
	 */
	public String getValueByAccount(String account) {
		String message = "";
		Stream<String> stream = null;
		try {
			stream = Files.lines(Paths.get("datos.txt"));
			message = stream.filter(x -> x.split(",")[0].equals(account)).findFirst().orElse("FAIL,La cuenta no se encuentra registrada");
		} catch (IOException e) {
			System.out.println("Error on reading the db : " + e.getMessage());
		}finally {
			stream.close();
		}
		return message.split(",")[1];
	}

	/**
	 * Message structure 'SAVE;0000000,00000000'
	 * 
	 * @param account
	 * @param value
	 * @return
	 */
	public static String buildStoreMessage(String account, String value) {
		return "SAVE;" + account + "," + value;
	}

	/**
	 * Message structure 'GET;0000000'
	 * 
	 * @param account
	 * @return
	 */
	public static String buildGetMessage(String account) {
		return "GET;" + account;
	}

}
