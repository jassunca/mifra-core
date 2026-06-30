package org.mifra.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.api.orchestrator.Orchestrator;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.dispatchers.InProcessSagaDispatcher;
import org.mifra.core.components.external.assemblers.HttpMessageAssembler;
import org.mifra.core.components.external.assemblers.MifraExternalMessageAssembler;
import org.mifra.core.components.handlers.ParticipantStepHandler;
import org.mifra.core.components.registries.OrchestratorRegistry;
import org.mifra.core.components.registries.ParticipantRegistry;
import org.mifra.core.components.serializers.HttpEndpointRequestDeserializer;
import org.mifra.core.network.MifraBaseServer;
import org.mifra.core.network.http.MifraHttpServer;

/**
 * Core framework engine that controls application execution. It handles both the pre-bootstrap orchestrator and
 * participant registry, and initializes the endpoint that receives the oncoming client requests.
 */
public class MifraEngine {

    private MifraBaseServer server;

    private final OrchestratorRegistry orchestratorRegistry;
    private final ParticipantRegistry participantRegistry;

    private final InProcessSagaDispatcher dispatcher;

    private MifraCoordinator coordinator;

    private HttpEndpointRequestDeserializer deserializer = new HttpEndpointRequestDeserializer();

    private MifraExternalMessageAssembler assembler;

    ObjectMapper objectMapper = new ObjectMapper();

    public MifraEngine() {

        //TODO a proper bootstrapper after checked everything working
        orchestratorRegistry = new OrchestratorRegistry();
        participantRegistry = new ParticipantRegistry();
        dispatcher = new InProcessSagaDispatcher(participantRegistry);
        coordinator = new MifraCoordinator(dispatcher);

        deserializer = new HttpEndpointRequestDeserializer();

        assembler = new HttpMessageAssembler(coordinator, orchestratorRegistry, deserializer);
    }

    /**
     * Start the microservice lifecycle.
     */
    public void startAndJoin() throws Exception {

        //TODO polymorphic server interface
        server = new MifraHttpServer(8080, assembler);

        server.start();
        
    }

    /**
     * Safely stop the service.
     */
    //TODO implement the stopping service
    public void stop() throws Exception {
        this.server.stop();
    }

    /**
     * Registers a specific orchestrator to the orchestrator registry.
     * @param path The endpoint suffix that is expected to receive client requests.
     * @param inputType The class type of the object matching the request's body for deserialization.
     * @param outputType The class type of the body sent to the client as reply.
     * @param orchestrator The orchestrator to register at the endpoint provided.
     * @param <I> The class defined for the client request body which must implement ExternalRequestBody.
     * @param <O> The class defined for the client reply body which must implement ExternalReplyBody.
     */
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> void registerOrchestrator(
            String path,
            Class<I> inputType,
            Class<O> outputType,
            Orchestrator<I, O> orchestrator) {
        orchestratorRegistry.register(path, inputType, outputType, orchestrator);
    }

    /**
     * Registers a specific saga step method pointer from a participant service.
     *
     * @param label   The unique string tracking key for the graph node configuration.
     * @param handler A method reference or lambda matching (SagaStepHistory) -> SagaStepMessage<T>.
     * @param <T>     The concrete payload type which must implement SagaStepPayload.
     */
    public <T extends SagaStepPayload> void registerParticipantStep(
            String label,
            ParticipantStepHandler<T> handler) {

        this.participantRegistry.register(label, handler);
    }
}
