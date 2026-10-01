package org.mifra.core.components.executors;

import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.registries.OrchestratorRegistry;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Monitors and manages the lifecycle of all running sagas. It associates each executor object to a thread, by reusing
 * the thread already created by Jetty to handle the client request.
 */
public class MifraExecutorManager {

    private MifraCoordinator coordinator;
    private long timeoutSeconds;

    private final Map<String, MifraExecutor> activeExecutors = new ConcurrentHashMap<>();

    private final ThreadFactory threadFactory = Thread.ofVirtual()
            .name("mifra-saga-worker-", 0)
            .factory();

    private static final System.Logger logger = System.getLogger(OrchestratorRegistry.class.getName());

    public MifraExecutorManager(MifraCoordinator coordinator, long timeoutSeconds) {
        this.coordinator = coordinator;
        this.timeoutSeconds = timeoutSeconds;
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
    public <I extends ExternalRequestBody, O extends ExternalReplyBody> ExternalReply<? extends ExternalReplyBody> executeSaga(
            OrchestratorInvoker<I, O> invoker, ExternalRequest<I> request
    ) {

        String sagaId = request.requestId();

        CompletableFuture<ExternalReply<? extends ExternalReplyBody>> sagaResultFuture = new CompletableFuture<>();

        Thread workerThread = threadFactory.newThread(() -> {
            MifraExecutor executor = new MifraExecutor(sagaId, coordinator);

            activeExecutors.put(sagaId, executor);

            try {
                ExternalReply<O> successReply = executor.execute(invoker, request);
                sagaResultFuture.complete(successReply);
            } catch (Exception e) {
                sagaResultFuture.completeExceptionally(e);
            }
        });

        workerThread.start();

        try {
            return sagaResultFuture.orTimeout(timeoutSeconds, TimeUnit.SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {

            if (e.getCause() instanceof TimeoutException) {
                logger.log(System.Logger.Level.WARNING, "Saga execution timed out for saga: " + sagaId);
                return invoker.prepareTimeoutReply(request, sagaId);
            }

            if (e.getCause() instanceof RuntimeException) throw (RuntimeException) e.getCause();
            throw new RuntimeException(e.getCause());
        }

    }

    public Map<String, MifraExecutor> getActiveExecutors() {
        return activeExecutors;
    }
}
