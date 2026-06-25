package org.mifra.core.network.http;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.mifra.core.components.external.assemblers.HttpMessageAssembler;
import org.mifra.core.components.external.assemblers.MifraExternalMessageAssembler;
import org.mifra.core.network.MifraBaseServer;

/**
 * HTTP based server to handle client requests.
 */
public class MifraHttpServer extends MifraBaseServer {

    private Server server;

    public MifraHttpServer(int port, MifraExternalMessageAssembler assembler) {
        super(port, assembler);
        server = new Server();
    }

    @Override
    public void start() throws Exception {

        HttpConfiguration http = new HttpConfiguration();

        HttpConnectionFactory http11 = new HttpConnectionFactory(http);

        ServerConnector connector = new ServerConnector(server, http11);
        connector.setPort(getPort());
        server.addConnector(connector);

        MifraExternalMessageAssembler abstractAssembler = getAssembler();

        // Defensive cast to guard against unexpected assembler types.
        if (abstractAssembler instanceof HttpMessageAssembler concreteAssembler) {
            server.setHandler(new MifraHttpHandler(concreteAssembler));
        } else {
            throw new IllegalStateException(String.format(
                    "Mifra Bootstrap Error: MifraHttpServer requires an instance of '%s' to operate. " +
                            "An incompatible assembler implementation of type '%s' was provided instead.",
                    HttpMessageAssembler.class.getName(),
                    abstractAssembler != null ? abstractAssembler.getClass().getName() : "null"
            ));
        }

        server.start();

    }

    public void join() throws Exception {
        if (server != null) server.join();
    }

    @Override
    public void stop() {

    }
}
