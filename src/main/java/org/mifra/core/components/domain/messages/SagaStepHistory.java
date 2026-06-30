package org.mifra.core.components.domain.messages;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container with all saga step messages produced so far in a given saga. The class type of the step functions as the key
 * itself to access its instantiation generated.
 */
public class SagaStepHistory {

    private final Map<Class<? extends SagaStepPayload>, SagaStepMessage<?>> historyMap;
    private String currentStep;

    public SagaStepHistory() {
        historyMap = new ConcurrentHashMap<>();
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * Get the saga step message associated to a given class.
     * @param payloadClass The class type key that identifies a saga step.
     * @return The step message of a given class type.
     * @param <T> The type the payload within the step message, which must extend SagaStepPayload.
     */
    @SuppressWarnings("unchecked")
    public <T extends SagaStepPayload> SagaStepMessage<T> getStep(Class<T> payloadClass) {
        return (SagaStepMessage<T>) this.historyMap.get(payloadClass);
    }

    /**
     *
     * Safely stores a saga step message using its concrete payload class token as the key.
     * @param message The step message to save.
     */
    public void addStep(SagaStepMessage<?> message) {
        if (message == null || message.getPayload() == null) {
            throw new IllegalArgumentException("Cannot add a null saga step or a saga step with a null payload to history.");
        }

        this.historyMap.put(message.getPayload().getClass(), message);
    }
}
