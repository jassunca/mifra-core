package org.mifra.core.config.server;

import org.mifra.core.config.server.http.HttpServerConfig;

/**
 * Configuration class that contains the necessary values to parametrize general endpoint configurations.
 */
public class ServerConfig {

    private final HttpServerConfig http = new HttpServerConfig();

    public HttpServerConfig getHttp() {
        return http;
    }
}
