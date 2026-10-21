package org.mifra.core.config;

import org.mifra.core.config.core.CoreConfig;
import org.mifra.core.config.server.ServerConfig;

/**
 * Main configuration container that holds all of Mifra's parametrization data.
 */
public class MifraConfig {

    private CoreConfig core = new CoreConfig();
    private ServerConfig server = new ServerConfig();

    public CoreConfig getCore() {
        return core;
    }

    public ServerConfig getServer() {
        return server;
    }
}
