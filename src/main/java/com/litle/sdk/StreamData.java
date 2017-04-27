package com.litle.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * This class is used to connect to a socket and read and write to it
 */

public class StreamData {
	private Socket socket;

	/**
	 * Standard no-arg constructor
	 */
	public StreamData() {
	}

	/**
	 * This method will initialize the class for use connecting to some machine on some port
	 * 
	 * @param hostname The name of the machine ( either dns name or full ip address )
	 * @param port The port on the machine that you want to connect to.
	 * @param timeOut The time ( in mills ) that the socket should wait on reading before timing out.
	 * @throws IOException when any exception occurs trying to connect to the machine/port.
	 */
	public void init(String hostname, String port, int timeOut, boolean SSL) throws IOException {
		try {
			if (SSL) {
				SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
				socket = ssf.createSocket(hostname, Integer.parseInt(port));
			} else {
				socket = new Socket(hostname, Integer.parseInt(port));
			}
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(timeOut);
		}
		catch (IOException e) {
			throw new IOException("Error connecting to host <" + hostname + "> and port <" + port + ">" + e);
		}
	}

	/**
	 * Initialization method for use when dealing strictly with a pre-constructed socket.
	 * 
	 * @param   socket  Socket to read data from and write data to
	 */
	public void init(Socket socket) {
		this.socket = socket;
	}


	/**
	 * This method will write the string on the socket.
	 * 
	 * @param data The string to write
	 * @throws IOException when a write exception occurs
	 */
	public void dataOut(String data) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
		osw.write(data);
		osw.flush();
	}

	/**
	 * This method will write to the Response file from the socket.
	 * 
	 * @param xmlResponseFile {@link File} Empty response file to write to
	 * @throws IOException when a read exception occurs
	 */
	public void dataIn(File xmlResponseFile) throws IOException {
		//StringBuffer data = new StringBuffer();
		FileWriter outputStream = null;
		int value;
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		outputStream = new FileWriter(xmlResponseFile.getAbsolutePath());
		InputStreamReader isr = new InputStreamReader(bis,"UTF-8");
		while ((value = isr.read()) != -1) {
			//data.append((char) value);
			outputStream.write((char) value);
		}
		outputStream.close();
	}

	/**
	 * This method will close the socket
	 * 
	 * @throws IOException when a close exception occurs
	 */
	public void closeSocket() throws IOException {
		if (socket != null)
		{
			socket.close();
		}
	}

	/**
	 * This method will disable the output stream for the socket
	 * 
	 * @throws IOException If an I/O error occurs when shutting down the socket
	 */
	public void shutdownOutputStream() throws IOException {
		socket.shutdownOutput();
	}

	/**
	 * This method will read from the request file and write to the socket.
	 * 
	 * @param file {@link File} Empty response file to write to
	 * @throws IOException when a read exception occurs
	 */
	public void dataOut(File file) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
		FileReader fr = new FileReader(file);
		try {
			int c = -1;
			while ((c = fr.read()) != -1) {
				osw.write(c);
			}
			osw.flush();
		}
		finally {
			fr.close();
		}
	}

}
