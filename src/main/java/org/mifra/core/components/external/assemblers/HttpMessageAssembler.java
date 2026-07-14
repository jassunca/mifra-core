package org.mifra.core.components.external.assemblers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
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
    public ExternalReply<? extends ExternalReplyBody> process(
            String path,
            String rawJson,
            String requestId,
            Map<String, List<String>> headers
    ) {

        OrchestratorInvoker<? extends ExternalRequestBody, ? extends ExternalReplyBody> invoker = this.orchestratorRegistry.getInvoker(path);

        if (invoker == null) {
            return null;
        }

        try {
            return executeCapturedSaga(invoker, rawJson, requestId, headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This is a private helper method that binds <I> and <O> dynamically at runtime, creating a type-safe funnel
     * that links the coordinator's type safe logic to the handler's wildcard requirement.
     */
    @SuppressWarnings("unchecked") //Mandatory warning suppress for the regardless safe invoker cast.
    private <I extends ExternalRequestBody, O extends ExternalReplyBody> ExternalReply<O> executeCapturedSaga(
            OrchestratorInvoker<?, ?> rawInvoker,
            String rawJson,
            String requestId,
            Map<String, List<String>> headers
    ) throws JsonProcessingException {

        OrchestratorInvoker<I, O> invoker = (OrchestratorInvoker<I, O>) rawInvoker;

        Class<I> requestType = invoker.getInputType();

        I requestBody = deserializer.deserialize(rawJson, requestType);

        ExternalRequest<I> externalRequest = new ExternalRequest<>(
                requestId,
                java.time.ZonedDateTime.now(),
                headers,
                requestBody
        );

        return coordinator.executeSaga(invoker, externalRequest);
    }
}
