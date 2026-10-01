package org.mifra.testresources.messages;

import org.mifra.core.api.models.external.payloads.ExternalRequestBody;

public class TimeoutCheckExternalRequestBody implements ExternalRequestBody {

    int waitingTime;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
