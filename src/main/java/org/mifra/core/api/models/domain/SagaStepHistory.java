package org.mifra.core.api.models.domain;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container with all saga step messages produced so far in a given saga. The class type of the step functions as the key
 * itself to access its instantiation generated.
 */
public class SagaStepHistory {

    private final Map<Class<? extends SagaStepPayload>, SagaStepMessage<? extends SagaStepPayload>> historyMap;

    public SagaStepHistory() {
        historyMap = new ConcurrentHashMap<>();
    }

    /**
     * Get the saga step message associated to a given class.
     * @param payloadClass The class type key that identifies a saga step.
     * @return The step message of a given class type.
     * @param <T> The type the payload within the step message, which must extend SagaStepPayload.
     */
    public <T extends SagaStepPayload> SagaStepMessage<T> getStep(Class<T> payloadClass) {
        if (payloadClass == null) {
            throw new IllegalArgumentException("Payload class token cannot be null.");
        }

        SagaStepMessage<? extends SagaStepPayload> message = this.historyMap.get(payloadClass);
        if (message == null) {
            return null;
        }

        /*
        This cast is safe as the data input from addStep() guarantees matching classes between the key and payload type
         */
        @SuppressWarnings("unchecked")
        SagaStepMessage<T> typesafeMessage = (SagaStepMessage<T>) message;

        return typesafeMessage;
    }

    /**
     *
     * Safely stores a saga step message using its concrete payload class token as the key.
     * @param message The step message to save.
     */
    public <T extends SagaStepPayload> void addStep(SagaStepMessage<T> message) {
        if (message == null || message.getPayload() == null) {
            throw new IllegalArgumentException("Cannot add a null saga step or a saga step with a null payload to history.");
        }

        //Safe cast since the method input already matches the parametrized type
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) message.getPayload().getClass();
        SagaStepMessage<? extends SagaStepPayload> saved = this.historyMap.putIfAbsent(type, message);

        if (saved != null) {
            throw new IllegalStateException(
                    String.format("Saga history violation: A step with payload type [%s] has already been recorded, the saga history is append-only and cannot be overwritten.", type.getSimpleName())
            );
        }

        //Lock the message after it's successfully appended to the history
        message.lock();
    }

    /**
     * Check if a certain SagaStepMessage is present in the history map.
     * @param key the payload class of the SagaStepMessage to check.
     * @return true/false if it exists or not.
     */
    public boolean has(Class<? extends SagaStepPayload> key) {
        return key != null && this.historyMap.containsKey(key);
    }
}
