package org.mifra.core.components.domain.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.testresources.messages.SampleParticipantAPayload;
import org.mifra.testresources.messages.SampleParticipantBPayload;

public class SagaStepHistoryTest {

    @Test
    public void getExistingMessage() {
        SagaStepHistory history = new SagaStepHistory();

        SagaStepMessage<SampleParticipantAPayload> message = new SagaStepMessage<>(new SampleParticipantAPayload());

        history.addStep(message);

        Assertions.assertTrue(history.has(SampleParticipantAPayload.class));
    }

    @Test
    public void getNonExistingMessage() {
        SagaStepHistory history = new SagaStepHistory();

        SagaStepMessage<SampleParticipantAPayload> message = new SagaStepMessage<>(new SampleParticipantAPayload());

        history.addStep(message);

        Assertions.assertFalse(history.has(SampleParticipantBPayload.class));
    }
}
