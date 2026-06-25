package org.mifra.core.components.external.assemblers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.registries.OrchestratorRegistry;
import org.mifra.core.components.serializers.HttpEndpointRequestDeserializer;

import java.util.List;
import java.util.Map;

/**
 * HTTP specific message assembler that deserializes the incoming JSON client request and delegates the saga start to the
 * coordinator.
 */
public class HttpMessageAssembler implements MifraExternalMessageAssembler{

    private final MifraCoordinator coordinator;
    private final OrchestratorRegistry orchestratorRegistry;
    private final HttpEndpointRequestDeserializer deserializer;

    public HttpMessageAssembler(
            MifraCoordinator coordinator,
            OrchestratorRegistry orchestratorRegistry,
            HttpEndpointRequestDeserializer deserializer
    ) {
        this.coordinator = coordinator;
        this.orchestratorRegistry = orchestratorRegistry;
        this.deserializer = deserializer;
    }

    /**
     * Method triggered by the HTTP handler listener to initiated message processing.
     * @param path The endpoint path suffix.
     * @param rawJson The raw JSON of the request body.
     * @param requestId The id of the client request used for identification.
     * @param headers The client request headers
     * @return An ExternalReply object produced at the end of the saga.
     */
    public ExternalReply<?> process(
            String path,
            String rawJson,
            String requestId,
            Map<String, List<String>> headers
    ) {

        OrchestratorInvoker<?, ?> invoker = this.orchestratorRegistry.getInvoker(path);

        if (invoker == null) {
            return null;
        }

        try {
            Class<? extends ExternalRequestBody> requestType = invoker.getInputType();

            ExternalRequestBody requestBody = deserializer.deserialize(rawJson, requestType);

            ExternalRequest<ExternalRequestBody> externalRequest = new ExternalRequest<>(
                    requestId,
                    java.time.ZonedDateTime.now(),
                    headers,
                    requestBody
            );

            return coordinator.executeSaga(invoker, externalRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
