package org.mifra.core.components.handlers;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;
import org.mifra.core.components.domain.messages.SagaStepHistory;

/**
 * Functional Interface that represents a given method in a generic format assignable at runtime. By standard definition,
 * it receives a SagaStepHistory with all generated saga step results so far and returns its own generated result,
 * wrapped in a SagaStepMessage object.
 * @param <O> The class of the payload inside the SagaStepMessage, which must implement SagaStepPayload.
 */
@FunctionalInterface
public interface ParticipantStepHandler<O extends SagaStepPayload> {
    SagaStepMessage<O> execute(SagaStepHistory sagaStepHistory) throws Throwable;
}
