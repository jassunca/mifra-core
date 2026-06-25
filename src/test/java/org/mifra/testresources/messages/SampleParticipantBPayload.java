package org.mifra.testresources.messages;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

public class SampleParticipantBPayload implements SagaStepPayload {

    public int getResultingCalculation() {
        return resultingCalculation;
    }

    public void setResultingCalculation(int resultingCalculation) {
        this.resultingCalculation = resultingCalculation;
    }

    int resultingCalculation;
}
