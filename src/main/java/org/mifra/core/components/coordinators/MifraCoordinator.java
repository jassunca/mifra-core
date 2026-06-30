package org.mifra.core.components.coordinators;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.domain.SagaStepOutcome;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.dispatchers.InProcessSagaDispatcher;
import org.mifra.core.components.domain.messages.SagaStepHistory;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.stepmaps.SagaStepMap;

/**
 * Main saga coordinator that handles orchestrator-participant and inter-participant step handling.
 */
public class MifraCoordinator {

    private InProcessSagaDispatcher dispatcher;

    public MifraCoordinator(InProcessSagaDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     *
     * @param invoker The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @param request The deserialized client request.
     * @return The reply object generated at the end of the saga that is sent back to the client.
     * @param <I> The class of the deserialized external request payload, which must implement ExternalRequestBody.
     */
    @SuppressWarnings("unchecked")
    public <I extends ExternalRequestBody> ExternalReply<?> executeSaga(OrchestratorInvoker<I, ?> invoker, ExternalRequest<?> request){

        SagaStepHistory sagaState = new SagaStepHistory();

        // Safe type bridge within the framework engine boundary
        ExternalRequest<I> typedRequest = (ExternalRequest<I>) request;

        SagaStepMessage<?> initialMessage = invoker.delegate(typedRequest);
        sagaState.addStep(initialMessage);

        SagaStepMap stepMap = invoker.getStepMap();
        String currentStepLabel = stepMap.getInitialStep();
        SagaStepOutcome lastOutcome = null;

        while (currentStepLabel != null) {

            SagaStepMessage<?> message = dispatcher.dispatch(currentStepLabel, sagaState);
            sagaState.addStep(message);
            lastOutcome = message.getResultState();
            SagaStepMap.StepNode currentNode = stepMap.getStep(currentStepLabel);

            if (currentNode == null) break;

            currentStepLabel = currentNode.getNextStep(lastOutcome.getValue());
        }

        return invoker.prepareExternalReply(sagaState);

    }

}
