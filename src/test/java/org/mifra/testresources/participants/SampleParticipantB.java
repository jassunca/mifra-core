package org.mifra.testresources.participants;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.participant.Participant;
import org.mifra.core.components.domain.messages.SagaStepHistoryMap;
import org.mifra.testresources.messages.SampleParticipantAPayload;
import org.mifra.testresources.messages.SampleParticipantBPayload;

public class SampleParticipantB implements Participant {

    public SagaStepMessage<SampleParticipantBPayload> process(SagaStepHistoryMap historyMap) {

        SampleParticipantAPayload source = historyMap.getStep(SampleParticipantAPayload.class).getPayload();

        SampleParticipantBPayload result = new SampleParticipantBPayload();
        result.setResultingCalculation(source.getResultingCalculation() + 7);
        return new SagaStepMessage<>(result);
    }
}
