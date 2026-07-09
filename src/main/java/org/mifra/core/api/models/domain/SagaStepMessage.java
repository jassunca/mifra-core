package org.mifra.core.api.models.domain;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

/**
 * Message that is passed to and from participants to function as data input or output. It contains the result state of
 * the saga step and the data payload to pass on.
 * @param <T> Parametrized message payload that must implement SagaStepPayload.
 */
public class SagaStepMessage<T extends SagaStepPayload> {

    private SagaStepOutcome resultState;

    private T payload;

    private boolean locked = false;

    public SagaStepMessage(T payload) {

        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public SagaStepOutcome getResultState() {
        return resultState;
    }

    public void setPayload(T payload) {
        isLocked();
        this.payload = payload;
    }

    public void setResultState(SagaStepOutcome resultState) {
        isLocked();
        this.resultState = resultState;
    }

    /**
     * Internal method called by SagaStepHistory as it adds the message to make it immutable after the fact.
     */
    void lock() {
        this.locked = true;
    }

    void isLocked() {
        if (locked)
            throw new IllegalStateException("Cannot modify a SagaStepMessage after it's been locked.");
    }
}

