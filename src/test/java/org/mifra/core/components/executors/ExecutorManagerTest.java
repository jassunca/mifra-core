package org.mifra.core.components.executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalReplyBody;
import org.mifra.core.api.models.external.payloads.ExternalTimeoutReplyBody;
import org.mifra.core.components.coordinators.MifraCoordinator;
import org.mifra.core.components.dispatchers.InProcessSagaDispatcher;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.registries.ParticipantRegistry;
import org.mifra.testresources.messages.SampleExternalRequestBody;
import org.mifra.testresources.messages.TimeoutCheckExternalReplyBody;
import org.mifra.testresources.orchestrators.TimeoutCheckOrchestrator;
import org.mifra.testresources.participants.TimeoutCheckParticipant;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class ExecutorManagerTest {

    private MifraExecutorManager underTest;
    private MifraCoordinator coordinator;
    private OrchestratorInvoker<SampleExternalRequestBody, TimeoutCheckExternalReplyBody> invoker;

    @BeforeEach
    public void setUp() {
        TimeoutCheckParticipant participant = new TimeoutCheckParticipant();
        ParticipantRegistry participantRegistry = new ParticipantRegistry();
        participantRegistry.register("WAITSTEP", participant::process);

        InProcessSagaDispatcher dispatcher = new InProcessSagaDispatcher(participantRegistry);
        this.coordinator = new MifraCoordinator(dispatcher);
        this.invoker = new OrchestratorInvoker<>(SampleExternalRequestBody.class, new TimeoutCheckOrchestrator());

        this.underTest = new MifraExecutorManager(coordinator, 5);
    }

    @Test
    public void testSuccessfulSaga() {
        SampleExternalRequestBody request = new SampleExternalRequestBody();
        request.setStartingValue(1000);

        ExternalReply<? extends ExternalReplyBody> result =
                underTest.executeSaga(invoker, new ExternalRequest<>("TestID", ZonedDateTime.now(), new HashMap<>(), request));

        Assertions.assertInstanceOf(TimeoutCheckExternalReplyBody.class, result.getPayload());
    }

    @Test
    public void testTimeoutSaga() {
        SampleExternalRequestBody request = new SampleExternalRequestBody();
        request.setStartingValue(6000);

        ExternalReply<? extends ExternalReplyBody> result =
                underTest.executeSaga(invoker, new ExternalRequest<>("TestID", ZonedDateTime.now(), new HashMap<>(), request));

        Assertions.assertInstanceOf(ExternalTimeoutReplyBody.class, result.getPayload());
    }
}
