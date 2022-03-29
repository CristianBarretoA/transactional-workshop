package com.transactional.socket;

import java.io.IOException;

public abstract class ASocket {
	
	protected final String FILE_URL = "datos.txt";
	protected String ip = "127.0.0.1";
	protected Integer port = 5000;
	
	/**
	 * Open a Socket with the given ip and port
	 */
	public abstract void socket();
	
	/**
	 * Get a response from the socket
	 * @return
	 * @throws IOException
	 */
	protected abstract String read() throws IOException;
	
	/**
	 * Send a request to the socket
	 * @param requestMessage
	 * @throws IOException
	 */
	protected abstract void write(String requestMessage) throws IOException;
	
	

}
