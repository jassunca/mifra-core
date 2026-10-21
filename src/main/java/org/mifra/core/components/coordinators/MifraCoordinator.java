package org.mifra.core.components.coordinators;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.domain.SagaStepOutcome;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.dispatchers.InProcessSagaDispatcher;
import org.mifra.core.api.models.domain.SagaStepHistory;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.registries.OrchestratorRegistry;
import org.mifra.core.components.stepmaps.SagaStepMap;

/**
 * Main saga coordinator that handles orchestrator-participant and inter-participant step handling.
 */
public class MifraCoordinator {

    private InProcessSagaDispatcher dispatcher;

    private static final System.Logger logger = System.getLogger(OrchestratorRegistry.class.getName());

    public MifraCoordinator(InProcessSagaDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     *
     * @param invoker The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @param request The deserialized client request.
     * @return The reply object generated at the end of the saga that is sent back to the client.
     * @param <I> The class of the deserialized external request payload, which must implement ExternalRequestBody.
     * @param <O> The class of the reply payload, which must implement ExternalReplyBody.
     */
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> ExternalReply<O> executeSaga(OrchestratorInvoker<I, O> invoker, ExternalRequest<I> request){

        SagaStepHistory sagaState = new SagaStepHistory();

        logger.log(System.Logger.Level.DEBUG, "Starting saga for request %s", request.toString());

        SagaStepMessage<? extends SagaStepPayload> initialMessage = invoker.delegate(request);
        sagaState.addStep(initialMessage);

        SagaStepMap stepMap = invoker.getStepMap();
        String currentStepLabel = stepMap.getInitialStep();
        SagaStepOutcome lastOutcome = null;

        while (currentStepLabel != null) {

            logger.log(System.Logger.Level.DEBUG,String.format("Executing participant step %s with history %s.", currentStepLabel, sagaState));
            SagaStepMessage<? extends SagaStepPayload> message = dispatcher.dispatch(currentStepLabel, sagaState);
            logger.log(System.Logger.Level.DEBUG,String.format("Received from participant step %s the message %s.", currentStepLabel, message.toString()));
            sagaState.addStep(message);
            lastOutcome = message.getResultState();
            SagaStepMap.StepNode currentNode = stepMap.getStep(currentStepLabel);

            if (currentNode == null) break;

            currentStepLabel = currentNode.getNextStep(lastOutcome.getValue());
        }

        return invoker.prepareExternalReply(sagaState);

    }

}
