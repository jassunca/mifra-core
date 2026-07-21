package org.mifra.testresources.messages;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

public class SampleStartingPayload implements SagaStepPayload {

    public int getStartingValue() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.startingValue = startingValue;
    }

    int startingValue;
}
