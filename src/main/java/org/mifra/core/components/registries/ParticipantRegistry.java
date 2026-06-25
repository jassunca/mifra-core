package org.mifra.core.components.registries;

import org.mifra.core.components.handlers.ParticipantStepHandler;
import org.mifra.core.components.invokers.ParticipantInvoker;
import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container object which holds the list of invokers called to execute their participant steps.
 */
public class ParticipantRegistry {
    private final Map<String, ParticipantInvoker> routeMap = new ConcurrentHashMap<>();

    /**
     * Register a participant step within the registry inside an invoker wrapper class.
     * @param label The step label that identifies the participant step within the SagaStepMap.
     * @param handler The method which processes the participant step, that is registered in a functional interface.
     * @param <O> The return type of the handler method, which must implement SagaStepPayload.
     */
    public <O extends SagaStepPayload> void register(
            String label,
            ParticipantStepHandler<O> handler) {

        if (label == null || handler == null) {
            throw new IllegalArgumentException("Label and handler cannot be null");
        }

        if (routeMap.containsKey(label)) {
            throw new IllegalArgumentException("Duplicate registration registration detected for step: " + label);
        }

        this.routeMap.put(label, new ParticipantInvoker(handler));
    }

    public ParticipantInvoker getInvoker(String path) {
        return this.routeMap.get(path);
    }
}
