package org.mifra.core.components.executors;

import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.invokers.OrchestratorInvoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Monitors and manages the lifecycle of all running sagas. It associates each executor object to a thread, by reusing
 * the thread already created by Jetty to handle the client request.
 */
public class MifraExecutorManager {

    private MifraCoordinator coordinator;

    private final Map<String, MifraExecutor> activeExecutors = new ConcurrentHashMap<>();

    public MifraExecutorManager(MifraCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    //TODO implement a timeout monitoring feature
    /**
     * Create a new executor to initiate the saga and terminate it when done.
     * @param invoker The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @param request The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @return The reply object generated at the end of the saga that is sent back to the client.
     * @param <I> The class of the deserialized external request payload, which must implement ExternalRequestBody.
     * @param <O> The class of the reply payload, which must implement ExternalReplyBody.
     */
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> ExternalReply<O> executeSaga(
            OrchestratorInvoker<I, O> invoker, ExternalRequest<I> request
    ) {

        String sagaId = request.requestId();

        MifraExecutor executor = new MifraExecutor(sagaId, coordinator);

        activeExecutors.put(sagaId, executor);

        try {
            return executor.execute(invoker, request);
        } finally {
            activeExecutors.remove(sagaId);
        }

    }

    public Map<String, MifraExecutor> getActiveExecutors() {
        return activeExecutors;
    }
}
