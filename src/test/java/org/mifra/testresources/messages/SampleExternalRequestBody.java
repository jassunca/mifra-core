package org.mifra.testresources.messages;

import org.mifra.core.api.models.external.payloads.ExternalRequestBody;

public class SampleExternalRequestBody implements ExternalRequestBody {

    public int getStartingValue() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.startingValue = startingValue;
    }

    int startingValue;
}
