package io.github.vantiv.sdk;

import java.io.IOException;
import java.io.OutputStream;

class EncryptedOutputStream extends OutputStream {
    private OutputStream encOut;
    private OutputStream out;
    private OutputStream pOut;


    EncryptedOutputStream(OutputStream encOut, OutputStream out, OutputStream pOut) {
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
