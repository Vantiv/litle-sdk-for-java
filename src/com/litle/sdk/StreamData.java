package com.litle.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

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
     * @throws Exception when any exception occurs trying to connect to the machine/port.
     */
    public void init(String hostname, int port, int timeOut, boolean SSL) throws Exception {
        try {
            if (SSL) {
                // dynamically register sun's ssl provider
                //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                SSLSocketFactory sslFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = (SSLSocket) sslFact.createSocket(hostname, port);
            } else {
                socket = new Socket(hostname, port);
            }
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(timeOut);
        }
        catch (Exception e) {
            Exception ex = new Exception("Error connecting to host <" + hostname + "> and port <" + port + ">" + e);
            throw ex;
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
    
//    public void sendStreamDataToIBC() {
//    	char [] passphrase = "sasquatch".toCharArray();
//    	KeyStore keyStore = KeyStore.getInstance("JKS");
//    	keyStore.load(stream, password));
//    	TrustManagerFactoy tmf = TrustManagerFactory.getInstance("SunX509");
//    	
//    }
    
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
     * This method will read a string from the socket.
     * 
     * @return The string read from the socket
     * @throws IOException when a read exception occurs
     */
    public String dataIn() throws IOException {
        StringBuffer data = new StringBuffer();
        int value;
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        InputStreamReader isr = new InputStreamReader(bis,"UTF-8");
        while ((value = isr.read()) != -1) {
            data.append((char) value);
        }
        return data.toString();
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

    public void dataOut(File file) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
        FileReader fr = new FileReader(file);
        int c = -1;
        while ((c = fr.read()) != -1) {
            osw.write(c);
        }
        osw.flush();
    }

}
