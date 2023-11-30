package io.github.vantiv.sdk;

import java.io.IOException;
import java.io.InputStream;

class DecryptedInputStream extends InputStream {
    private InputStream literalDataInputStream;
    private InputStream fileInputStream;
    private InputStream clearStream;

    DecryptedInputStream(InputStream literalDataInputStream, InputStream fileInputStream, InputStream clearStream){
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