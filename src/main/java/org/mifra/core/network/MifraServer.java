package org.mifra.core.network;

/**
 * Interface that serves as the minimum common base for all server endpoint types.
 */
public interface MifraServer {

    public void start() throws Exception;

    public void stop();
}
