package org.mifra.testresources.messages;

import org.mifra.core.api.models.external.payloads.ExternalReplyBody;

public class TimeoutCheckExternalReplyBody implements ExternalReplyBody {

    private final String message;

    public TimeoutCheckExternalReplyBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
