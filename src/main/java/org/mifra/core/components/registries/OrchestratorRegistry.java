package org.mifra.core.components.registries;

import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.api.orchestrator.Orchestrator;
import org.mifra.core.components.invokers.OrchestratorInvoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for the registered orchestrators in the application. It contains a map of the endpoint path that a set
 * orchestrator receives requests from, and the invoker object that contains that orchestrator and its parametrized
 * input and output payloads.
 */
public class OrchestratorRegistry {
    private final Map<String, OrchestratorInvoker<?, ?>> routeMap = new ConcurrentHashMap<>();

    /**
     * Register a given orchestrator in the orchestrator registry.
     * @param path The endpoint path the orchestrator receives requests on, in the format "/{path}", excluding
     *             hostname and port.
     * @param inputType The class of the request payload for the orchestrator.
     * @param outputType The class of the reply payload for the orchestrator.
     * @param orchestrator The orchestrator instance to register.
     * @param <I> The parametrized type of the request payload. Must implement ExternalRequestBody.
     * @param <O> The parametrized type of the reply payload. Must implement ExternalReplyBody.
     */
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> void register(
            String path,
            Class<I> inputType,
            Class<O> outputType,
            Orchestrator<I, O> orchestrator) {

        if (routeMap.containsKey(path)) {
            throw new IllegalArgumentException("Duplicate route registration detected for path: " + path);
        }

        this.routeMap.put(path, new OrchestratorInvoker<>(inputType, orchestrator));
    }

    public OrchestratorInvoker<?, ?> getInvoker(String path) {
        return this.routeMap.get(path);
    }
}
