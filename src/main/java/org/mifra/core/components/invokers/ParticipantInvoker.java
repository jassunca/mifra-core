package org.mifra.core.components.invokers;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;
import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.components.domain.messages.SagaStepHistoryMap;
import org.mifra.core.components.handlers.ParticipantStepHandler;

/**
 * Wrapper class that holds the handler associated to a participant step.
 */
public class ParticipantInvoker {

    private final ParticipantStepHandler<? extends SagaStepPayload> handler;

    public ParticipantInvoker(ParticipantStepHandler<?> handler) {
        this.handler = handler;
    }

    /**
     * Caller method that delegates the step execution to the handler.
     * @param historyMap The container which holds all the resulting objects from each executed step so far in the saga.
     * @return The result of the participant step execution.
     */
    public SagaStepMessage<?> delegate(SagaStepHistoryMap historyMap) {
        try {
            return this.handler.execute(historyMap);
        } catch (Throwable t) {
            throw new RuntimeException("Step execution failed", t);
        }
    }
}
