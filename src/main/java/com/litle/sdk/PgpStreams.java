package com.litle.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class EncryptedOutputStream extends OutputStream {
    private OutputStream encOut;
    private OutputStream out;
    private OutputStream pOut;


    public EncryptedOutputStream(OutputStream encOut, OutputStream out, OutputStream pOut) {
        this.encOut = encOut;
        this.out = out;
        this.pOut = pOut;
    }


    public void write(byte[] clearData) throws IOException {
        pOut.write(clearData);
    }

    public void write(int i) throws IOException{
        pOut.write(i);
    }

    public void close()throws IOException{
        pOut.close();
        encOut.close();
        out.close();
    }

    public void flush()throws IOException{
        pOut.flush();
    }
}



class DecryptedInputStream extends InputStream {
    private InputStream literalDataInputStream;
    private InputStream fileInputStream;
    private InputStream clearStream;

    public DecryptedInputStream(InputStream literalDataInputStream, InputStream fileInputStream, InputStream clearStream){
        this.literalDataInputStream = literalDataInputStream;
        this.fileInputStream = fileInputStream;
        this.clearStream = clearStream;
    }

    public int read() throws IOException {
        return literalDataInputStream.read();
    }

    public int read(byte[] buffer) throws IOException{
        return literalDataInputStream.read(buffer);
    }

    public void close() throws IOException{
        literalDataInputStream.close();
        fileInputStream.close();
        clearStream.close();
    }
}