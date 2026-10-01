package org.mifra.testresources.participants;

import org.mifra.core.api.models.domain.SagaStepHistory;
import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.participant.Participant;
import org.mifra.testresources.messages.TimeoutCheckParticipantPayload;
import org.mifra.testresources.messages.TimeoutCheckStartingPayload;

public class TimeoutCheckParticipant implements Participant {

    public SagaStepMessage<TimeoutCheckParticipantPayload> process(SagaStepHistory historyMap) throws InterruptedException {
        TimeoutCheckStartingPayload request = historyMap.getStep(TimeoutCheckStartingPayload.class).getPayload();

        Thread.sleep(request.getWaitingTime());

        return new SagaStepMessage<TimeoutCheckParticipantPayload>(new TimeoutCheckParticipantPayload());
    }
}
