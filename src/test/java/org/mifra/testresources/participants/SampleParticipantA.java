package org.mifra.testresources.participants;

import org.mifra.core.api.models.domain.SagaStepMessage;
import org.mifra.core.api.models.domain.SagaStepOutcome;
import org.mifra.core.api.participant.Participant;
import org.mifra.core.components.domain.messages.SagaStepHistory;
import org.mifra.testresources.messages.SampleParticipantAPayload;
import org.mifra.testresources.messages.SampleStartingPayload;

public class SampleParticipantA implements Participant {

    public SagaStepMessage<SampleParticipantAPayload> process(SagaStepHistory historyMap) {

        SampleStartingPayload source = historyMap.getStep(SampleStartingPayload.class).getPayload();

        SampleParticipantAPayload payload = new SampleParticipantAPayload();
        payload.setResultingCalculation(source.getStartingValue() * 2);
        SagaStepMessage<SampleParticipantAPayload> result = new SagaStepMessage<>(payload);
        result.setResultState(SagaStepOutcome.of("SUM"));
        return result;
    }

}
