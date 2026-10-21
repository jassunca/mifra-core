package org.mifra.core.config.server.http;

/**
 * Configuration class that contains the necessary values to parametrize an http-based endpoint.
 */
public class HttpServerConfig {

    private final Integer endpointPort = 8080;

    public Integer getEndpointPort() {
        return endpointPort;
    }
}
