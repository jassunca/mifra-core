package org.mifra.api.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mifra.core.api.models.domain.SagaStepHistory;
import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.SagaStepOutcome;
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

    @Test
    public void modifyLockedMessageAttempt() {

        SagaStepHistory history = new SagaStepHistory();

        SagaStepMessage<SampleParticipantAPayload> message = new SagaStepMessage<>(new SampleParticipantAPayload());

        message.setResultState(SagaStepOutcome.of("SUCCESS"));
        Assertions.assertEquals("SUCCESS",message.getResultState().getValue());

        history.addStep(message);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            message.setResultState(SagaStepOutcome.of("FAILURE"));
        }, "Expected setResultState to fail with IllegalStateException after being committed to history.");

        // Assert - Verify that attempting to change the payload triggers IllegalStateException
        Assertions.assertThrows(IllegalStateException.class, () -> {
            message.setPayload(new SampleParticipantAPayload());
        }, "Expected setPayload to fail with IllegalStateException after being committed to history.");
    }

    @Test
    public void replaceExistingMessageAttempt() {

        SagaStepHistory history = new SagaStepHistory();

        SagaStepMessage<SampleParticipantAPayload> message = new SagaStepMessage<>(new SampleParticipantAPayload());

        history.addStep(message);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            history.addStep(new SagaStepMessage<>(new SampleParticipantAPayload()));
        }, "Expected addStep to fail with IllegalStateException after trying to add a message of an existing type.");

    }
}
