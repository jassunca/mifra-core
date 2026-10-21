package org.mifra.core.components.executors;

import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.invokers.OrchestratorInvoker;

/**
 * The wrapper object that serves as the saga's dedicated thread, by reusing the already existent Jetty thread.
 */
public class MifraExecutor {

    private final String sagaId;
    private final MifraCoordinator coordinator;
    private final Thread executionThread;

    public MifraExecutor(String sagaId, MifraCoordinator coordinator) {
        this.sagaId = sagaId;
        this.coordinator = coordinator;
        this.executionThread = Thread.currentThread();
    }

    /**
     * Triggers the saga execution at the coordinator.
     * @param invoker The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @param request The orchestrator invoker that holds the orchestrator responsible for handling the given request.
     * @return The reply object generated at the end of the saga that is sent back to the client.
     * @param <I> The class of the deserialized external request payload, which must implement ExternalRequestBody.
     * @param <O> The class of the reply payload, which must implement ExternalReplyBody.
     */
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> ExternalReply<O> execute(
            OrchestratorInvoker<I, O> invoker, ExternalRequest<I> request) {
        return coordinator.executeSaga(invoker, request);
    }

    public String getSagaId() { return sagaId; }
    //TODO implement thread interruption call at the manager
    /**
     * Interrupt the thread associated with the executor instance.
     */
    public void interrupt() { executionThread.interrupt(); }

}
