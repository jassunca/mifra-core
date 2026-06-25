package org.mifra.core.network;

import org.mifra.core.components.external.assemblers.MifraExternalMessageAssembler;

/**
 * Base Server object that all server implementations are based on.
 */
public abstract class MifraBaseServer implements MifraServer{

    private MifraExternalMessageAssembler assembler;
    private int port;

    public MifraBaseServer(int port, MifraExternalMessageAssembler assembler) {
        this.port = port;
        this.assembler = assembler;
    }

    public MifraExternalMessageAssembler getAssembler() {
        return assembler;
    }

    public int getPort() {
        return port;
    }
}
