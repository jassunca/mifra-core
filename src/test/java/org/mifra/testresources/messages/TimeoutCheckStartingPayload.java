package org.mifra.testresources.messages;

import org.mifra.core.api.models.domain.payloads.SagaStepPayload;

public class TimeoutCheckStartingPayload implements SagaStepPayload {

    int waitingTime;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
