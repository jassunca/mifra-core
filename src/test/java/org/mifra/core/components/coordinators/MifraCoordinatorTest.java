package org.mifra.core.components.coordinators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mifra.core.api.models.external.ExternalReply;
import org.mifra.core.api.models.external.ExternalRequest;
import org.mifra.core.api.models.external.payloads.ExternalRequestBody;
import org.mifra.core.components.dispatchers.InProcessSagaDispatcher;
import org.mifra.core.components.invokers.OrchestratorInvoker;
import org.mifra.core.components.registries.ParticipantRegistry;
import org.mifra.testresources.messages.SampleExternalReplyBody;
import org.mifra.testresources.messages.SampleExternalRequestBody;
import org.mifra.testresources.orchestrators.SampleOrchestrator;
import org.mifra.testresources.participants.SampleParticipantA;
import org.mifra.testresources.participants.SampleParticipantB;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class MifraCoordinatorTest {

    @Test
    public void testSagaSequence() {

        SampleParticipantA participantA = new SampleParticipantA();
        SampleParticipantB participantB = new SampleParticipantB();

        ParticipantRegistry participantRegistry = new ParticipantRegistry();
        participantRegistry.register("FIRSTSTEP", participantA::process);
        participantRegistry.register("SECONDSTEP", participantB::process);

        InProcessSagaDispatcher dispatcher = new InProcessSagaDispatcher(participantRegistry);

        MifraCoordinator underTest = new MifraCoordinator(dispatcher);

        OrchestratorInvoker<SampleExternalRequestBody, SampleExternalReplyBody> invoker = new OrchestratorInvoker<>(SampleExternalRequestBody.class, new SampleOrchestrator());

        SampleExternalRequestBody request = new SampleExternalRequestBody();
        //Participant A doubles this value, and participant B adds 7.
        request.setStartingValue(6);

        ExternalReply<?> result = underTest.executeSaga(invoker, new ExternalRequest<>("TestID", ZonedDateTime.now(),new HashMap<>(),request));

        SampleExternalReplyBody payload = (SampleExternalReplyBody) result.getPayload();

        Assertions.assertEquals(19, payload.getResultingCalculation());
    }
}
