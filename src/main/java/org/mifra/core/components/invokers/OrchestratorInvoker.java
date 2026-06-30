package org.mifra.core.components.invokers;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.api.orchestrator.Orchestrator;
import org.mifra.core.components.domain.messages.SagaStepHistory;
import org.mifra.core.components.stepmaps.SagaStepMap;

/**
 * An invoker class to keep an association between a provided orchestrator and the payloads it is parametrized to
 * receive and send to and from clients.
 * @param <I> The parametrized type of the payload deserialized from the client request's body.
 * @param <O> The parametrized type of the payload to serialize for the reply to the client.
 */
public class OrchestratorInvoker<I extends ExternalRequestBody, O extends ExternalReplyBody> {
    private final Orchestrator<I, O> orchestrator;
    private final Class<I> inputType;
    private final SagaStepMap stepMap;

    public OrchestratorInvoker(Class<I> inputType, Orchestrator<I, O> orchestrator) {
        this.inputType = inputType;
        this.orchestrator = orchestrator;
        this.stepMap = new SagaStepMap();
        //Fill the step map in the invoker with the one defined in the orchestrator.
        orchestrator.defineStepMap(stepMap);
    }

    public Class<I> getInputType() {
        return inputType;
    }

    public SagaStepMap getStepMap() { return stepMap; }

    /**
     * Send the entire client request to the orchestrator to begin handling.
     * @param request The request received from the client.
     * @return The object that is serialized and returned to the client as the reply.
     */
    @SuppressWarnings("unchecked")
    public SagaStepMessage<?> delegate(ExternalRequest<?> request) {
        return orchestrator.handleOncomingRequest((ExternalRequest<I>) request);
    }

    public ExternalReply<O> prepareExternalReply(SagaStepHistory sagaStepHistory) {
        return orchestrator.prepareExternalReply(sagaStepHistory);
    }
}
