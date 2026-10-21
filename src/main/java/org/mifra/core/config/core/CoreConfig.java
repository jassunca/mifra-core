package org.mifra.core.config.core;

/**
 * Configuration class that contains the necessary values to parametrize Mifra's core functions.
 */
public class CoreConfig {

    private Long sagaTimeout = 5L;

    public Long getSagaTimeout() {
        return sagaTimeout;
    }
}
